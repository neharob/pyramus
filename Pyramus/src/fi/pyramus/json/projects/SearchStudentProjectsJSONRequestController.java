package fi.pyramus.json.projects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import fi.pyramus.JSONRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.ProjectDAO;
import fi.pyramus.domainmodel.projects.StudentProject;
import fi.pyramus.UserRole;
import fi.pyramus.json.JSONRequestController;
import fi.pyramus.persistence.search.SearchResult;

/**
 * The controller responsible of searching projects.
 * 
 * @see fi.pyramus.views.modules.SearchProjectsViewController
 */
public class SearchStudentProjectsJSONRequestController implements JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    ProjectDAO projectDAO = DAOFactory.getInstance().getProjectDAO();

    Integer resultsPerPage = NumberUtils.createInteger(requestContext.getRequest().getParameter("maxResults"));
    if (resultsPerPage == null) {
      resultsPerPage = 10;
    }

    Integer page = NumberUtils.createInteger(requestContext.getRequest().getParameter("page"));
    if (page == null) {
      page = 0;
    }

    // Gather the search terms

    String projectText = requestContext.getRequest().getParameter("project");
    String studentText = requestContext.getRequest().getParameter("student");

    // Search via the DAO object

    SearchResult<StudentProject> searchResult = projectDAO.searchStudentProjects(resultsPerPage, page, projectText,
        projectText, studentText, true, false);

    List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
    List<StudentProject> studentProjects = searchResult.getResults();
    for (StudentProject studentProject : studentProjects) {
      Map<String, Object> studentProjectInfo = new HashMap<String, Object>();
      studentProjectInfo.put("id", studentProject.getId());
      studentProjectInfo.put("studentProjectName", studentProject.getName());
      studentProjectInfo.put("studentFirstName", studentProject.getStudent().getFirstName());
      studentProjectInfo.put("studentLastName", studentProject.getStudent().getLastName());
      results.add(studentProjectInfo);
    }

    String statusMessage = "";
    Locale locale = requestContext.getRequest().getLocale();
    if (searchResult.getTotalHitCount() > 0) {
      statusMessage = Messages.getInstance().getText(
          locale,
          "projects.searchStudentProjects.searchStatus",
          new Object[] { searchResult.getFirstResult() + 1, searchResult.getLastResult() + 1,
              searchResult.getTotalHitCount() });
    }
    else {
      statusMessage = Messages.getInstance().getText(locale, "projects.searchStudentProjects.searchStatusNoMatches");
    }
    requestContext.addResponseParameter("results", results);
    requestContext.addResponseParameter("statusMessage", statusMessage);
    requestContext.addResponseParameter("pages", searchResult.getPages());
    requestContext.addResponseParameter("page", searchResult.getPage());
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
