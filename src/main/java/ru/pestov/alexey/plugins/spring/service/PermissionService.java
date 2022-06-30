package ru.pestov.alexey.plugins.spring.service;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.user.ApplicationUser;
import ru.pestov.alexey.plugins.spring.configuration.Property;
import ru.pestov.alexey.plugins.spring.enums.Role;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Locale;

@Named
public class PermissionService {

    private final Property property;
    private final DBService dbService;
    private final SystemService systemService;

    @Inject
    public PermissionService(Property property, DBService dbService, SystemService systemService) {
        this.property = property;
        this.dbService = dbService;
        this.systemService = systemService;
    }

    public Role isCurrentUserAdminJira() {
        String nameGroupAdmin = property.getProperty("jira.group.admins");
        if (isUserInGroup(nameGroupAdmin)) {
            return Role.JIRA_ADMIN;
        }
        return Role.USER;
    }

    public Boolean isCurrentUserSuperUser() {
        String nameGroupSuperUsers = property.getProperty("jira.group.superusers");
        String adminGroup = property.getProperty("jira.group.admins");
        return isUserInGroup(nameGroupSuperUsers) || isUserInGroup(adminGroup);
    }

    public Boolean isCurrentUserDelivery(Integer idSystem) {
        ApplicationUser currentUser = getCurrentUser();
        return dbService.isCurrentUserDelivery(idSystem, currentUser);
    }

    private boolean isUserInGroup(ApplicationUser applicationUser, String nameGroup) {
        GroupManager groupManager = ComponentAccessor.getGroupManager();
        return groupManager.isUserInGroup(applicationUser, nameGroup);
    }

    private boolean isUserInGroup(String nameGroup) {
        ApplicationUser currentUser = getCurrentUser();
        GroupManager groupManager = ComponentAccessor.getGroupManager();
        return groupManager.isUserInGroup(currentUser, nameGroup);
    }

    private ApplicationUser getCurrentUser() {
        return ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
    }

    public Boolean isEnable(Integer idSystem, Integer idTypeChange) {
        return (idTypeChange != null) && (isCurrentUserDelivery(idSystem) || isCurrentUserSuperUser() || isCurrentUserAuthorize(idSystem, idTypeChange));
    }

    private Boolean isCurrentUserAuthorize(Integer idSystem, Integer idTypeChange) {
        ApplicationUser currentUser = getCurrentUser();
        String assigneesStageSystem = systemService.getAssigneesStageSystem(idSystem, idTypeChange, "authorize");
        if (!assigneesStageSystem.equals("")) {
            Integer idAuthorize = Integer.parseInt(assigneesStageSystem);
            String nameAuthorize = dbService.getUserById(idAuthorize);
            return currentUser.getUsername().toLowerCase(Locale.ROOT).equals(nameAuthorize.toLowerCase());
        } else {
            return false;
        }
    }
}
