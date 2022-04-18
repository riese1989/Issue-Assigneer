package ru.pestov.alexey.plugins.spring.service;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.user.ApplicationUser;
import ru.pestov.alexey.plugins.spring.configuration.Property;

import java.util.*;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class UserService {
    private final Property property;

    @Inject
    public UserService (Property property)  {
        this.property = property;
    }
    public List<ApplicationUser> getUsersJira() {
        GroupManager groupManager = ComponentAccessor.getGroupManager();
        return  (List<ApplicationUser>) groupManager.getUsersInGroup("jira-users");
    }

    public List<String> getActiveUsers() {
        List<ApplicationUser> users = getUsersJira();
        List<String> activeUsers = new ArrayList<>();
        Iterator var4 = users.iterator();
        while(var4.hasNext()) {
            ApplicationUser user = (ApplicationUser)var4.next();
            if (user.isActive()) {
                activeUsers.add(user.getEmailAddress().replace("@x5.ru","") + "=" + user.getEmailAddress());
            }
        }
        activeUsers.stream().sorted().collect(Collectors.toList());
        return activeUsers;
    }

    public ApplicationUser getCurrentUser() {
        return ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
    }
}
