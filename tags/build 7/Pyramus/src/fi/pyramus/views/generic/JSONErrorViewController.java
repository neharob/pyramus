package fi.pyramus.views.generic;

import fi.pyramus.PageRequestContext;
import fi.pyramus.UserRole;
import fi.pyramus.views.PyramusViewController;

public class JSONErrorViewController implements PyramusViewController {

  public void process(PageRequestContext requestContext) {
    requestContext.getRequest().setAttribute("errorCode", requestContext.getRequest().getParameter("errorCode"));
    requestContext.getRequest().setAttribute("errorMessage", requestContext.getRequest().getParameter("errorMessage"));
    requestContext.getRequest().setAttribute("errorLevel", requestContext.getRequest().getParameter("errorLevel"));
    requestContext.getRequest().setAttribute("isHttpError", requestContext.getRequest().getParameter("isHttpError"));
    
    requestContext.setIncludeJSP("/templates/generic/jsonerror.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}