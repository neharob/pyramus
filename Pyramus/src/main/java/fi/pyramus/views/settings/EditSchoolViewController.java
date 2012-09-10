package fi.pyramus.views.settings;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.ContactTypeDAO;
import fi.pyramus.dao.base.ContactURLTypeDAO;
import fi.pyramus.dao.base.SchoolDAO;
import fi.pyramus.dao.base.SchoolFieldDAO;
import fi.pyramus.dao.base.SchoolVariableKeyDAO;
import fi.pyramus.domainmodel.base.Address;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.ContactURLType;
import fi.pyramus.domainmodel.base.Email;
import fi.pyramus.domainmodel.base.PhoneNumber;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.SchoolVariableKey;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.JSONArrayExtractor;
import fi.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Edit School view of the application.
 * 
 * @see fi.pyramus.json.settings.EditSchoolJSONRequestController
 */
public class EditSchoolViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    SchoolFieldDAO schoolFieldDAO = DAOFactory.getInstance().getSchoolFieldDAO();
    SchoolVariableKeyDAO schoolVariableKeyDAO = DAOFactory.getInstance().getSchoolVariableKeyDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    ContactURLTypeDAO contactURLTypeDAO = DAOFactory.getInstance().getContactURLTypeDAO();

    Long schoolId = NumberUtils.createLong(pageRequestContext.getRequest().getParameter("school"));
    School school = schoolDAO.findById(schoolId);
    
    StringBuilder tagsBuilder = new StringBuilder();
    Iterator<Tag> tagIterator = school.getTags().iterator();
    while (tagIterator.hasNext()) {
      Tag tag = tagIterator.next();
      tagsBuilder.append(tag.getText());
      if (tagIterator.hasNext())
        tagsBuilder.append(' ');
    }
    
    List<ContactURLType> contactURLTypes = contactURLTypeDAO.listUnarchived();
    Collections.sort(contactURLTypes, new StringAttributeComparator("getName"));

    List<SchoolVariableKey> schoolUserEditableVariableKeys = schoolVariableKeyDAO.listUserEditableVariableKeys();
    Collections.sort(schoolUserEditableVariableKeys, new StringAttributeComparator("getVariableName"));

    List<ContactType> contactTypes = contactTypeDAO.listUnarchived();
    Collections.sort(contactTypes, new StringAttributeComparator("getName"));
    
    String jsonContactTypes = new JSONArrayExtractor("id", "name").extractString(contactTypes);
    
    List<Address> addresses = school.getContactInfo().getAddresses();
    JSONArray jsonAddresses = new JSONArrayExtractor("id",
                                                   "name",
                                                   "streetAddress",
                                                   "postalCode",
                                                   "city",
                                                   "country").extract(addresses);
    for (int i=0; i<jsonAddresses.size(); i++) {
      JSONObject jsonAddress = jsonAddresses.getJSONObject(i);
      if (addresses.get(i).getContactType() != null) {
        jsonAddress.put("contactTypeId", addresses.get(i).getContactType().getId());
      }
    }
    
    List<Email> emails = school.getContactInfo().getEmails();
    JSONArray jsonEmails = new JSONArrayExtractor("id", "defaultAddress", "address").extract(emails);
    for (int i=0; i<jsonEmails.size(); i++) {
      JSONObject jsonEmail = jsonEmails.getJSONObject(i);
      if (emails.get(i).getContactType() != null) {
        jsonEmail.put("contactTypeId", emails.get(i).getContactType().getId());
      }
    }
    
    List<PhoneNumber> phoneNumbers = school.getContactInfo().getPhoneNumbers();
    JSONArray jsonPhoneNumbers = new JSONArrayExtractor("id", "defaultNumber", "number").extract(phoneNumbers);
    for (int i=0; i<jsonPhoneNumbers.size(); i++) {
      JSONObject jsonPhoneNumber = jsonPhoneNumbers.getJSONObject(i);
      if (phoneNumbers.get(i).getContactType() != null) {
        jsonPhoneNumber.put("contactTypeId", emails.get(i).getContactType().getId());
      }
    }
    
    String jsonVariableKeys = new JSONArrayExtractor("key", "name", "type").extractString(schoolUserEditableVariableKeys);
    
    this.setJsDataVariable(pageRequestContext, "addresses", jsonAddresses.toString());
    this.setJsDataVariable(pageRequestContext, "emails", jsonEmails.toString());
    this.setJsDataVariable(pageRequestContext, "phoneNumbers", jsonPhoneNumbers.toString());
    this.setJsDataVariable(pageRequestContext, "contactTypes", jsonContactTypes);
    this.setJsDataVariable(pageRequestContext, "variableKeys", jsonVariableKeys);

    pageRequestContext.getRequest().setAttribute("tags", tagsBuilder.toString()); 
    pageRequestContext.getRequest().setAttribute("school", school);
    pageRequestContext.getRequest().setAttribute("variableKeys", schoolUserEditableVariableKeys);
    pageRequestContext.getRequest().setAttribute("schoolFields", schoolFieldDAO.listUnarchived());

    pageRequestContext.setIncludeJSP("/templates/settings/editschool.jsp");
  }

  /**
   * Returns the roles allowed to access this page.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "settings.editSchool.pageTitle");
  }

}
