package ru.pestov.alexey.plugins.spring.service;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.mail.Email;
import com.atlassian.mail.MailException;
import com.atlassian.mail.server.SMTPMailServer;
import ru.pestov.alexey.plugins.spring.enums.TypeEmailNotifications;

import javax.inject.Named;

@Named
public class EmailService {

    static final String THEME = "Notification from CAB plugin";


    public boolean sendNotification(String emailAddress, TypeEmailNotifications type, String nameSystem, String nameTypeChange, String stage)  {
        String body  = getMessage(type, nameSystem, nameTypeChange, stage);
        SMTPMailServer mailServer = ComponentAccessor.getMailServerManager().getDefaultSMTPMailServer();
        if (mailServer != null) {
            Email email = new Email(emailAddress);
            email.setSubject(THEME);
            email.setBody(body);
            try {
                mailServer.send(email);
                return true;
            } catch (MailException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private String getMessage(TypeEmailNotifications type, String nameSystem, String nameTypeChange, String stage)   {
        String message = "";
        switch (type)   {
            case ADDED: {
                message = "You have been added as assignee to " + nameSystem + " " + nameTypeChange + " " + stage;
                break;
            }
            case DELETE:    {
                message = "You have been deleted as assignee from " + nameSystem + " " + nameTypeChange + " " + stage;
                break;
            }
            case DELETE_DELIVERY:   {
                message = "You have been deleted as delivery from " + nameSystem;
                break;
            }
            case ADDED_DELIVERY: {
                message = "You have been added as delivery to " + nameSystem;
                break;
            }
        }
        return message;
    }
}
