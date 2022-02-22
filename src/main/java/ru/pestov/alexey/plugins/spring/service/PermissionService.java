package ru.pestov.alexey.plugins.spring.service;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.user.ApplicationUser;
import ru.pestov.alexey.plugins.spring.configuration.Property;
import ru.pestov.alexey.plugins.spring.enums.Role;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class PermissionService {

    private final Property property;

    @Inject
    public PermissionService(Property property) {
        this.property = property;
    }

    public Role isCurrentUserAdminJira()    {
        String nameGroupAdmin = property.getProperty("jira.group.admins");
        if (isUserInGroup(nameGroupAdmin))    {
            return Role.JIRA_ADMIN;
        }
        return Role.USER;
    }

    public Role isCurrentUserSuperUser() {
        String nameGroupSuperUsers = property.getProperty("jira.group.superusers");
        if (isUserInGroup(nameGroupSuperUsers))    {
            return Role.SUPERUSER;
        }
        return Role.USER;
    }

    public Role isCurrentUserDelivery(String idSystem)  {
        String nameGroupDelivery = property.getProperty("jira.group.delivery");
        if (isUserInGroup(nameGroupDelivery))    {
            return Role.DELIVERY;
        }
        return Role.USER;
    }

    private boolean isUserInGroup (ApplicationUser applicationUser, String nameGroup)   {
        GroupManager groupManager = ComponentAccessor.getGroupManager();
        return groupManager.isUserInGroup(applicationUser, nameGroup);
    }
    private boolean isUserInGroup(String nameGroup) {
        ApplicationUser currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        GroupManager groupManager = ComponentAccessor.getGroupManager();
        return groupManager.isUserInGroup(currentUser, nameGroup);
    }
}
