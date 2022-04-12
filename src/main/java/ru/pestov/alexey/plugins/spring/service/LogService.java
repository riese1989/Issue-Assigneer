package ru.pestov.alexey.plugins.spring.service;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.user.ApplicationUser;
import ru.pestov.alexey.plugins.spring.configuration.Property;

import javax.inject.Named;
import java.io.FileInputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Named
public class LogService {
    private static Logger LOGGER = null;

    static {
//        try(FileInputStream ins = new FileInputStream(new Property().getProperty("file.settings.log"))){
//            LogManager.getLogManager().readConfiguration(ins);
//            LOGGER = Logger.getLogger(LogService.class.getName());
//        }   catch (Exception ignore){
//            ignore.printStackTrace();
//        }
    }

    public void log(String message) {
//        ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
//        LOGGER.info("User " + user.getEmailAddress() + " was changed " + message);
    }
}
