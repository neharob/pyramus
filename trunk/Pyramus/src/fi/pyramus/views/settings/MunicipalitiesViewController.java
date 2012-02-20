package fi.pyramus.views.settings;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.PyramusViewController;
import fi.pyramus.UserRole;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.MunicipalityDAO;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Manage Municipalities view of the application.
 * 
 * @see fi.pyramus.json.settings.SaveEducationTypesJSONRequestController
 */
public class MunicipalitiesViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    MunicipalityDAO municipalityDAO = DAOFactory.getInstance().getMunicipalityDAO();
    
    List<Municipality> municipalities = municipalityDAO.listUnarchived();
    Collections.sort(municipalities, new StringAttributeComparator("getName"));
    pageRequestContext.getRequest().setAttribute("municipalities", municipalities);
    pageRequestContext.setIncludeJSP("/templates/settings/municipalities.jsp");
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
    return Messages.getInstance().getText(locale, "settings.municipalities.pageTitle");
  }

}
