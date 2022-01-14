package ru.pestov.alexey.plugins.spring.service;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.inject.Named;

@Named
public class UserService {
    public UserService() {
    }

    public Map<String, String> getActiveUsers() {
        Map<String, String> usersMap = new HashMap();
        UserManager userManager = ComponentAccessor.getUserManager();
        List<ApplicationUser> users = (List)userManager.getAllApplicationUsers();
        Iterator var4 = users.iterator();

        while(var4.hasNext()) {
            ApplicationUser user = (ApplicationUser)var4.next();
            if (user.isActive()) {
                usersMap.put(user.getDisplayName(), user.getEmailAddress());
            }
        }

        return usersMap;
    }
}
