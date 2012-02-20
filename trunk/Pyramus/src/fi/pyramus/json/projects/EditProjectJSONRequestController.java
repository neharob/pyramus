package fi.pyramus.json.projects;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.StaleObjectStateException;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.JSONRequestController;
import fi.pyramus.UserRole;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.dao.modules.ModuleDAO;
import fi.pyramus.dao.projects.ProjectDAO;
import fi.pyramus.dao.projects.ProjectModuleDAO;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.projects.Project;
import fi.pyramus.domainmodel.projects.ProjectModule;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.persistence.usertypes.ProjectModuleOptionality;

public class EditProjectJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    ProjectDAO projectDAO = DAOFactory.getInstance().getProjectDAO();
    ProjectModuleDAO projectModuleDAO = DAOFactory.getInstance().getProjectModuleDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();

    // Project

    Long projectId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("project"));
    Project project = projectDAO.findById(projectId);
    
    // Version check
    Long version = jsonRequestContext.getLong("version"); 
    if (!project.getVersion().equals(version))
      throw new StaleObjectStateException(Project.class.getName(), project.getId());
    
    String name = jsonRequestContext.getRequest().getParameter("name");
    String description = jsonRequestContext.getRequest().getParameter("description");
    User user = userDAO.findById(jsonRequestContext.getLoggedUserId());
    Long optionalStudiesLengthTimeUnitId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter(
        "optionalStudiesLengthTimeUnit"));
    EducationalTimeUnit optionalStudiesLengthTimeUnit = educationalTimeUnitDAO.findById(optionalStudiesLengthTimeUnitId);
    Double optionalStudiesLength = NumberUtils.createDouble(jsonRequestContext.getRequest().getParameter(
        "optionalStudiesLength"));
    String tagsText = jsonRequestContext.getString("tags");
    
    Set<Tag> tagEntities = new HashSet<Tag>();
    if (!StringUtils.isBlank(tagsText)) {
      List<String> tags = Arrays.asList(tagsText.split("[\\ ,]"));
      for (String tag : tags) {
        if (!StringUtils.isBlank(tag)) {
          Tag tagEntity = tagDAO.findByText(tag.trim());
          if (tagEntity == null)
            tagEntity = tagDAO.create(tag);
          tagEntities.add(tagEntity);
        }
      }
    }
    
    projectDAO.update(project, name, description, optionalStudiesLength, optionalStudiesLengthTimeUnit, user);

    // Tags

    projectDAO.updateTags(project, tagEntities);

    // Project modules

    Set<Long> existingIds = new HashSet<Long>();
    int rowCount = NumberUtils.createInteger(
        jsonRequestContext.getRequest().getParameter("modulesTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "modulesTable." + i;
      int optionality = new Integer(jsonRequestContext.getRequest().getParameter(colPrefix + ".optionality"))
          .intValue();
      Long projectModuleId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter(
          colPrefix + ".projectModuleId"));
      if (projectModuleId == -1) {
        Long moduleId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter(colPrefix + ".moduleId"));
        Module module = moduleDAO.findById(moduleId);
        projectModuleId = projectModuleDAO.create(project, module,
            ProjectModuleOptionality.getOptionality(optionality)).getId();
      }
      else {
        projectModuleDAO.update(projectModuleDAO.findById(projectModuleId), ProjectModuleOptionality
            .getOptionality(optionality));
      }
      existingIds.add(projectModuleId);
    }
    List<ProjectModule> projectModules = projectModuleDAO.listByProject(project);
    for (ProjectModule projectModule : projectModules) {
      if (!existingIds.contains(projectModule.getId())) {
        projectModuleDAO.delete(projectModule);
      }
    }
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
