package com.tsystems.parser.email.example;

import com.independentsoft.exchange.ServiceException;
import com.tsystems.parser.email.model.EmailMessage;
import com.tsystems.parser.email.model.EmailParser;
import com.tsystems.parser.email.util.HtmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class HtmlParserExample {

    private static Logger logger = LoggerFactory.getLogger(HtmlParserExample.class);

    private static final String FILTER_EMAIL_IN_SUBJECT = "RE: Release of bill runs";
    private static final String AUTOMOVE_EMAIL_IN_SUBJECT = "Release of bill runs";

    public static void main(String[] args) {

        EmailParser emailParser = EmailParser.getEmailParserInstance();
        HtmlParser htmlParser = HtmlParser.getHtmlParserInstance();

        String sourceFolder = "Mobile Billing";
        String targetFolder = "Mobile Billing/processed";
        try {
            Optional<List<EmailMessage>> emailsOfFolder = emailParser.getEmailMessagesOf(sourceFolder);
            List<EmailMessage> itemsToBeMoved = new ArrayList<>();

            if (emailsOfFolder.isPresent()) {
                List<EmailMessage> emailMessages = emailsOfFolder.get();


                emailMessages.forEach(aMessage -> {
                    boolean isMessageValid = aMessage.getSubject().startsWith(FILTER_EMAIL_IN_SUBJECT);
                    boolean isAutoMove = aMessage.getSubject().startsWith(AUTOMOVE_EMAIL_IN_SUBJECT);

                    if (isMessageValid) {
                        htmlParser.mapTableContentToStringBuilder(aMessage.getMessageBody());

                        htmlParser.mapTableContentToVector(aMessage.getMessageBody());

                        itemsToBeMoved.add(aMessage);
                    }

                    if (isAutoMove) {
                        itemsToBeMoved.add(aMessage);
                    }

                });

                emailParser.moveListOfItemsToRefFolder(itemsToBeMoved, targetFolder);

            } else {
                logger.info("No email messages found in folder: " + sourceFolder);
            }

        } catch (ServiceException serviceEx) {
            serviceEx.printStackTrace();
            logger.error(serviceEx.getMessage());
        }
    }

}
