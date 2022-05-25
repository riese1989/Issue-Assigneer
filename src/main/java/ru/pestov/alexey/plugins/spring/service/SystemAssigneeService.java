package ru.pestov.alexey.plugins.spring.service;

import ru.pestov.alexey.plugins.spring.model.SystemAssignees;
import ru.pestov.alexey.plugins.spring.model.User;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
public class SystemAssigneeService {

    public List<User> getUsers(List<SystemAssignees> list1) {
        List<User> users = new ArrayList<>();
        list1.forEach(l -> users.add(l.getUser()));
        return users;
    }
}
