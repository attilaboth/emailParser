package com.tsystems.parser.email.example;

import com.independentsoft.exchange.ServiceException;
import com.tsystems.parser.email.model.EmailMessage;
import com.tsystems.parser.email.model.EmailParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class FilterOldEmailsExample {

    private static final String EMAIL_FOLDER_NAME = "attila";
    private static final int NEW_EMAIL_LIMIT_IN_DAYS = 5;
    private static Logger logger = LoggerFactory.getLogger(AttachmentExample.class);

    public static void main(String[] args) {
        EmailParser emailParser = EmailParser.getEmailParserInstance();

        try {
            Optional<List<EmailMessage>> emailsOfFolder = emailParser.getEmailMessagesOf(EMAIL_FOLDER_NAME);

            if (emailsOfFolder.isPresent()) {
                List<EmailMessage> emailMessages = emailsOfFolder.get();
                System.out.println("Number of mails found: " + emailMessages.size());

                List<EmailMessage> filteredMsgs = emailParser.filterEmails(emailMessages, NEW_EMAIL_LIMIT_IN_DAYS);

                System.out.println("Filtering Email messages: ");
                for (EmailMessage msg : filteredMsgs) {
                    System.out.println(msg.getSubject());
                }

                //Deletes old emails
                emailParser.deleteListOfEmailMessage(filteredMsgs);

            } else {
                logger.info("No email messages found in folder: " + EMAIL_FOLDER_NAME);
            }
        } catch (ServiceException serviceEx) {
            serviceEx.printStackTrace();
            logger.error(serviceEx.getMessage());
        }
    }


}