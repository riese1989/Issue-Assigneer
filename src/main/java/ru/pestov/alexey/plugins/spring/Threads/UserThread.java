package ru.pestov.alexey.plugins.spring.Threads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.pestov.alexey.plugins.spring.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class UserThread extends Thread{
    public static List<String> nameUsersWithID = Collections.synchronizedList(new ArrayList<>());
    private User user;
    @Override
    public void run() {
        String name = user.getName();
        Integer id = user.getID();
        nameUsersWithID.add(name + "=" + id);
    }
}
