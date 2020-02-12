package com.tsystems.parser.email.model;

import com.independentsoft.exchange.Mailbox;
import com.independentsoft.exchange.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailService {

    private static EmailService emailServiceInstance = null;
    private static Service service = null;
    private static MailboxProperties mailboxProperties = null;

    {
        Properties properties = new Properties();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("mailbox.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mailboxProperties = new MailboxProperties.MailboxPropertiesBuilder(properties).build();
    }

    private EmailService() {
        service = new Service(mailboxProperties.getExchangeHost(), mailboxProperties.getUserName(), mailboxProperties.getPassword(), mailboxProperties.getDomain());
    }

    public static EmailService getEmailServiceInstance() {
        if (emailServiceInstance == null) {
            emailServiceInstance = new EmailService();
        }
        return emailServiceInstance;
    }

    public static Mailbox getMailbox() {
        return new Mailbox(mailboxProperties.getEmailAddress());
    }

    public Service getService() {
        return service;
    }

}
