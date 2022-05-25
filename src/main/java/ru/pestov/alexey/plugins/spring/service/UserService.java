package ru.pestov.alexey.plugins.spring.service;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.user.ApplicationUser;
import ru.pestov.alexey.plugins.spring.configuration.Property;
import ru.pestov.alexey.plugins.spring.model.SystemAssignees;
import ru.pestov.alexey.plugins.spring.model.User;

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
        return  (List<ApplicationUser>) groupManager.getUsersInGroup(property.getProperty("jira.group.users"));
    }

    public List<String> getActiveUsers() {
        List<ApplicationUser> users = getUsersJira();
        List<String> activeUsers = new ArrayList<>();
        Iterator var4 = users.iterator();
        while(var4.hasNext()) {
            ApplicationUser user = (ApplicationUser)var4.next();
            if (user.isActive()) {
                activeUsers.add(user.getUsername() + "=");
            }
        }
        activeUsers.stream().sorted().collect(Collectors.toList());
        return activeUsers;
    }

    public ApplicationUser getCurrentUser() {
        return ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
    }

    public boolean compareLists(List<User> list1, List<User> list2)   {
        List<User> list1Buf = new ArrayList<>(list1), list2Buf = new ArrayList<>(list2);
        if (list1.size() != list2.size())   {
            return false;
        }
        list1Buf.removeAll(list2);
        list2Buf.removeAll(list1);
        return list1Buf.size() == 0 && list1Buf.size() == 0;
    }
}
