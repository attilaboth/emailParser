package com.tsystems.parser.email.example;

import com.independentsoft.exchange.AttachmentInfo;
import com.independentsoft.exchange.ServiceException;
import com.tsystems.parser.email.model.EmailMessage;
import com.tsystems.parser.email.model.EmailParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class AttachmentExample {

    private static final String EMAIL_FOLDER_NAME = "attila";
    private static final String FILE_PATH = "C:\\Users\\A109593623\\saveAttachments\\";
    private static Logger logger = LoggerFactory.getLogger(AttachmentExample.class);

    public static void main(String[] args) {
        EmailParser emailParser = EmailParser.getEmailParserInstance();

        try {
            Optional<List<EmailMessage>> emailsOfFolder = emailParser.getEmailMessagesOf(EMAIL_FOLDER_NAME);

            if (emailsOfFolder.isPresent()) {
                List<EmailMessage> emailMessages = emailsOfFolder.get();
                System.out.println("Number of mails found: " + emailMessages.size());

                for (EmailMessage aMessage : emailMessages) {
                    List<AttachmentInfo> attachmentsInfoList = aMessage.getAttachmentsInfo();

                    emailParser.saveEmailAttachments(FILE_PATH, attachmentsInfoList);
                }

            } else {
                logger.info("No email messages found in folder: " + EMAIL_FOLDER_NAME);
            }

        } catch (ServiceException serviceEx) {
            serviceEx.printStackTrace();
            logger.error(serviceEx.getMessage());
        }
    }

}