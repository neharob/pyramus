package fi.pyramus.views.settings;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import net.sf.json.*;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.domainmodel.base.EducationSubtype;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.JSONArrayExtractor;
import fi.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Manage Subtypes of Fields of Education view of the application.
 * 
 * @see fi.pyramus.json.settings.SaveEducationSubtypesJSONRequestController
 */
public class EducationSubtypesViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    Collections.sort(educationTypes, new StringAttributeComparator("getName"));
    
    JSONArray jaEducationTypes = new JSONArrayExtractor("name", "id").extract(educationTypes);
    for (int i=0; i<educationTypes.size(); i++) {
      List<EducationSubtype> subtypes = educationTypes.get(i).getUnarchivedSubtypes();
      JSONArray jaSubtypes = new JSONArrayExtractor("id", "name", "code").extract(subtypes);
      jaEducationTypes.getJSONObject(i).put("subtypes", jaSubtypes);
    }

    this.setJsDataVariable(pageRequestContext, "educationTypes", jaEducationTypes.toString());
    pageRequestContext.setIncludeJSP("/templates/settings/educationsubtypes.jsp");
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
    return Messages.getInstance().getText(locale, "settings.educationSubtypes.pageTitle");
  }

}
