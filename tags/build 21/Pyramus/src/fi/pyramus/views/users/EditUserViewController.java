package fi.pyramus.views.users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import fi.pyramus.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.BaseDAO;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.UserDAO;
import fi.pyramus.UserRole;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.ContactURLType;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.plugin.auth.AuthenticationProvider;
import fi.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.pyramus.plugin.auth.InternalAuthenticationProvider;
import fi.pyramus.util.StringAttributeComparator;
import fi.pyramus.views.PyramusViewController;

/**
 * The controller responsible of the Edit User view of the application.
 * 
 * @see fi.pyramus.json.users.EditUserJSONRequestController
 */
public class EditUserViewController implements PyramusViewController, Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response. 
   * 
   * @param requestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    // TODO loggedUserRole vs. user role
    BaseDAO baseDAO = DAOFactory.getInstance().getBaseDAO();
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    User user = userDAO.getUser(pageRequestContext.getLong("userId"));
    String username = "";
    
    List<AuthenticationProviderInfoBean> authenticationProviders = new ArrayList<AuthenticationProviderInfoBean>();
    for (String authenticationProviderName : AuthenticationProviderVault.getAuthenticationProviderClasses().keySet()) {
      boolean active = AuthenticationProviderVault.getInstance().getAuthenticationProvider(authenticationProviderName) != null;
      boolean canUpdateCredentials;
      
      AuthenticationProvider authenticationProvider = AuthenticationProviderVault.getInstance().getAuthenticationProvider(authenticationProviderName);
      
      if (authenticationProvider instanceof InternalAuthenticationProvider) {
        InternalAuthenticationProvider internalAuthenticationProvider = (InternalAuthenticationProvider) authenticationProvider;
        canUpdateCredentials = internalAuthenticationProvider.canUpdateCredentials();

        if (internalAuthenticationProvider.getName().equals(user.getAuthProvider())) {
          username = internalAuthenticationProvider.getUsername(user.getExternalId());
        }
      } else {
        canUpdateCredentials = false;
      }
      
      authenticationProviders.add(new AuthenticationProviderInfoBean(authenticationProviderName, active, canUpdateCredentials));
    }
    
    StringBuilder tagsBuilder = new StringBuilder();
    Iterator<Tag> tagIterator = user.getTags().iterator();
    while (tagIterator.hasNext()) {
      Tag tag = tagIterator.next();
      tagsBuilder.append(tag.getText());
      if (tagIterator.hasNext())
        tagsBuilder.append(' ');
    }

    List<ContactURLType> contactURLTypes = baseDAO.listContactURLTypes();
    Collections.sort(contactURLTypes, new StringAttributeComparator("getName"));

    List<ContactType> contactTypes = baseDAO.listContactTypes();
    Collections.sort(contactTypes, new StringAttributeComparator("getName"));

    pageRequestContext.getRequest().setAttribute("tags", tagsBuilder.toString());
    pageRequestContext.getRequest().setAttribute("user", user);
    pageRequestContext.getRequest().setAttribute("username", username);
    pageRequestContext.getRequest().setAttribute("contactTypes", contactTypes);
    pageRequestContext.getRequest().setAttribute("contactURLTypes", contactURLTypes);
    pageRequestContext.getRequest().setAttribute("variableKeys", userDAO.listUserVariableKeys());
    pageRequestContext.getRequest().setAttribute("authenticationProviders", authenticationProviders);
    
    pageRequestContext.setIncludeJSP("/templates/users/edituser.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Available for only those
   * with {@link Role#MANAGER} or {@link Role#ADMINISTRATOR} privileges.
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
    return Messages.getInstance().getText(locale, "users.editUser.pageTitle");
  }

}