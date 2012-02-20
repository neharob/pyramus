package fi.pyramus.views.users;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.UserRole;
import fi.pyramus.PyramusViewController;

public class SearchUsersDialogViewController extends PyramusViewController {

  public void process(PageRequestContext requestContext) {
    requestContext.setIncludeJSP("/templates/users/searchusersdialog.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.USER, UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
  
}
