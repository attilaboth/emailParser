package com.tsystems.parser.email.model;

//import com.independentsoft.exchange.Attachment;

import com.independentsoft.exchange.AttachmentInfo;
import com.independentsoft.exchange.ItemId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmailMessage {

    private ItemId itemId;
    private String subject;
    private String from;
    private List<String> toRecipients;
    private String messageBody;
    private Date receivedDate;
    private Date sentDate;
    private List<AttachmentInfo> attachmentsInfo;

    public String getMessageBody() {
        return messageBody;
    }

    public String getSubject() {
        return subject;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public ItemId getItemId() {
        return itemId;
    }

    public String getFrom() {
        return from;
    }

    public List<String> getToRecipients() {
        return toRecipients;
    }

    public Date getSentDate() {
        return sentDate;
    }

    /**
     * Only the builder can instantiate the object
     *
     * @param builder
     */
    private EmailMessage(EmailMessageBuilder builder) {
        this.subject = builder.subject;
        this.from = builder.from;
        this.messageBody = builder.messageBody;
        this.receivedDate = builder.receivedDate;
        this.toRecipients = builder.toRecipients;
        this.itemId = builder.itemId;
        this.sentDate = builder.sentDate;
        this.attachmentsInfo = builder.attachmentsInfo;
    }

    public List<AttachmentInfo> getAttachmentsInfo() {
        return attachmentsInfo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EmailMessage{");
        sb.append("itemId=").append(itemId);
        sb.append(", subject='").append(subject).append('\'');
        sb.append(", from='").append(from).append('\'');
        sb.append(", toRecipients=").append(toRecipients);
        sb.append(", messageBody='").append(messageBody).append('\'');
        sb.append(", receivedDate=").append(receivedDate);
        sb.append(", sentDate=").append(sentDate);
        sb.append(", attachmentsInfo=").append(attachmentsInfo);
        sb.append('}');
        return sb.toString();
    }

    public static class EmailMessageBuilder {

        private ItemId itemId;
        private String subject;
        private String from;
        private List<String> toRecipients;
        private String messageBody;
        private Date receivedDate;
        private Date sentDate;
        private List<AttachmentInfo> attachmentsInfo;

        public EmailMessageBuilder setSubject(String subject) {
            this.subject = checkForNullOrEmpty(subject);
            return this;
        }

        public EmailMessageBuilder setFrom(String from) {
            this.from = checkForNullOrEmpty(from);
            return this;
        }

        public EmailMessageBuilder setToRecipients(List<String> toRecipients) {
            this.toRecipients = (toRecipients == null) ? toRecipients = new ArrayList<>() : toRecipients;
            return this;
        }

        public EmailMessageBuilder setMessageBody(String messageBody) {
            this.messageBody = checkForNullOrEmpty(messageBody);
            return this;
        }

        public EmailMessageBuilder setReceivedDate(Date receivedDate) {
            this.receivedDate = (receivedDate == null) ? receivedDate = new Date() : receivedDate;
            return this;
        }

        public EmailMessageBuilder setItemId(ItemId itemId) {
            this.itemId = itemId;
            return this;
        }

        public EmailMessageBuilder setSentDate(Date sentDate) {
            this.sentDate = (sentDate == null) ? sentDate = new Date() : sentDate;
            return this;
        }

        public EmailMessageBuilder setAttachmentsInfo(List<AttachmentInfo> attachmentsInfo) {
            this.attachmentsInfo = attachmentsInfo;
            return this;
        }

        private String checkForNullOrEmpty(String aStringToCheck) {
            if (aStringToCheck == null || aStringToCheck.length() == 0) {
                aStringToCheck = "";
            }
            return aStringToCheck;
        }

        public EmailMessage build() {
            return new EmailMessage(this);
        }
    }


}
