package ru.pestov.alexey.plugins.spring.service;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;

import java.util.*;
import java.util.stream.Collectors;
import javax.inject.Named;

@Named
public class UserService {
    public UserService() {
    }

    public List<String> getActiveUsers() {
        List<String> activeUsers = new ArrayList<>();
        UserManager userManager = ComponentAccessor.getUserManager();
        List<ApplicationUser> users = (List)userManager.getAllApplicationUsers();
        Iterator var4 = users.iterator();
        while(var4.hasNext()) {
            ApplicationUser user = (ApplicationUser)var4.next();
            if (user.isActive()) {
                activeUsers.add(user.getDisplayName() + "=" + user.getEmailAddress());
            }
        }
        activeUsers.stream().sorted().collect(Collectors.toList());
        return activeUsers;
    }
}
