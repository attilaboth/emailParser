package com.tsystems.parser.email.model;

import com.independentsoft.exchange.*;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Collectors;

import static com.independentsoft.exchange.DeleteType.SOFT_DELETE;

public class EmailParser {
    private static Logger logger = LoggerFactory.getLogger(EmailParser.class);

    private static EmailParser emailParserInstance = null;
    private static Service service = null;
    private static List<EmailMessage> emailMessageList = null; // lazy init

    static {
        service = EmailService.getEmailServiceInstance().getService();
    }

    private EmailParser() {
        logger.info("ExchangeService was instantiated: " + service);
    }

    public static EmailParser getEmailParserInstance() {
        if (emailParserInstance == null) {
            emailParserInstance = new EmailParser();
        }
        return emailParserInstance;
    }

    public static void deleteListOfEmailMessage(List<EmailMessage> messagesToBeDeleted) {
        List<ItemId> itemIdList = new ArrayList<>();

        try {
            for (EmailMessage emailMessage : messagesToBeDeleted) {
                itemIdList.add(emailMessage.getItemId());
            }

            service.deleteItem(itemIdList, SOFT_DELETE); //SOFT_DELETE -> trash

        } catch (ServiceException serviceEx) {
            serviceEx.printStackTrace();
            logger.error(serviceEx.getMessage());
        }

    }

    /**
     * @param FOLDER_FROM
     * @param subFolders
     * @return
     * @throws ServiceException
     */
    private Optional<List<Item>> getItemsOfFolder(String FOLDER_FROM, List<Folder> subFolders) throws ServiceException {

        FolderId folderFromId = null;
        for (Folder aFolder : subFolders) {
            if (aFolder.getDisplayName().equals(FOLDER_FROM)) {
                folderFromId = aFolder.getFolderId();
            }
        }
        List<Item> items = service.findItem(folderFromId).getItems();
        return Optional.ofNullable(items);
    }


    /**
     * @return
     */
    private Optional<List<Folder>> getFoldersOfInbox() {
        FindFolderResponse inboxFolder = null;
        List<Folder> folders = null;
        try {
            StandardFolderId inboxFolderId = new StandardFolderId(StandardFolder.INBOX, EmailService.getMailbox());

            inboxFolder = service.findFolder(inboxFolderId);
        } catch (ServiceException serviceEx) {
            serviceEx.printStackTrace();
        }
        if (inboxFolder != null) {
            folders = inboxFolder.getFolders();
        }

        return Optional.ofNullable(folders);
    }

    /**
     * @return
     */
    private List<EmailMessage> getEmailMessageList() {
        if (emailMessageList == null) {
            emailMessageList = new Vector<>();
        }
        return emailMessageList;
    }

    /**
     * @param itemsToBeMoved
     * @param moveToFolderNameStr
     * @return
     */
    public int moveListOfItemsToRefFolder(List<EmailMessage> itemsToBeMoved, String moveToFolderNameStr) {
        List<ItemId> itemIdsList = new Vector<>();

        try {

            itemsToBeMoved.forEach(anEmail -> itemIdsList.add(anEmail.getItemId()));

            Optional<FolderId> folderIdForFolderNameInInbox = findFolderIdForFolderNameInInbox(moveToFolderNameStr);

            if (folderIdForFolderNameInInbox.isPresent() && !itemsToBeMoved.isEmpty()) {

                List<ItemInfoResponse> itemInfoResponses = service.moveItem(itemIdsList, folderIdForFolderNameInInbox.get());

                logger.info(itemInfoResponses.size() + " messages were moved to " + moveToFolderNameStr);

                return itemInfoResponses.size();
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error("ServiceException occurred - " + e);
        }
        return 0;
    }

    /**
     * @param folderName
     * @return
     * @throws ServiceException
     */
    public Optional<FolderId> findFolderIdForFolderNameInInbox(final String folderName) throws ServiceException {
        FolderId folderIdtoReturn = null;

        Optional<List<Folder>> allSubFoldersForInbox = getFoldersOfInbox();

        if (allSubFoldersForInbox.isPresent()) {

            for (Folder folder : allSubFoldersForInbox.get()) {
                String displayName = folder.getDisplayName();

                if (folderName.contains("/") && folderName.split("/")[0].equals(displayName)) {

                    FindFolderResponse subFolder = service.findFolder(folder.getFolderId());

                    for (Folder allSubFolders : subFolder.getFolders()) {
                        if (allSubFolders.getDisplayName().equals(folderName.split("/")[1])) {
                            folderIdtoReturn = allSubFolders.getFolderId();
                        }
                    }
                } else if (displayName.equals(folderName.trim())) {
                    folderIdtoReturn = folder.getFolderId();
                }
            }

        }
        return Optional.ofNullable(folderIdtoReturn);
    }

    public static List<EmailMessage> filterEmails(List<EmailMessage> emailMessages, final int days) {
        List<EmailMessage> filteredMessages = new ArrayList();

        for (EmailMessage aMessage : emailMessages) {
            long emailReceivedInMilliseconds = aMessage.getReceivedDate().getTime();
            LocalDateTime receivedDate = Instant.ofEpochMilli(emailReceivedInMilliseconds).atZone(ZoneId.systemDefault()).toLocalDateTime();
            final long between = ChronoUnit.DAYS.between(receivedDate, LocalDateTime.now());

            if (between >= days) {
                filteredMessages.add(aMessage);
            }
        }

        return filteredMessages;
    }

    /**
     * Returns a List of EmailMessages found in the FOLDER_FROM
     *
     * @param FOLDER_FROM
     * @return
     * @throws ServiceException
     */
    public Optional<List<EmailMessage>> getEmailMessagesOf(String FOLDER_FROM) throws ServiceException {
        logger.info("Finding items in " + FOLDER_FROM);
        FindFolderResponse allFolders = null;

        try {

            Optional<List<Folder>> foldersOfInbox = getFoldersOfInbox();

            if (foldersOfInbox.isPresent()) {
                Optional<List<Item>> items = getItemsOfFolder(FOLDER_FROM, foldersOfInbox.get());

                //TODO: ? we can make it faster with stream API, then Builder needs refatoring
                //items.stream().map(service.getMessage().collect(Collectors.toList());

                if (items.isPresent()) {
                    for (Item anItem : items.get()) {
                        Message message = service.getMessage(anItem.getItemId());

                        EmailMessage emailMessage = new EmailMessage.EmailMessageBuilder()
                                .setMessageBody((null != message.getBodyHtmlText()) ? message.getBodyHtmlText() : message.getBody().getText())
                                //.setMessageBody(message.getBodyPlainText())
                                .setFrom(message.getFrom().getEmailAddress())
                                .setReceivedDate(message.getReceivedTime())
                                .setSubject(message.getSubject())
                                .setToRecipients(message.getToRecipients().stream().map(Mailbox::getEmailAddress).collect(Collectors.toList()))
                                .setItemId(message.getItemId())
                                .setAttachmentsInfo(message.getAttachments())
                                .build();

                        getEmailMessageList().add(emailMessage);
                    }
                }

            } else {
                logger.info("No items found in " + FOLDER_FROM);
            }

        } catch (ServiceException serviceEx) {
            serviceEx.printStackTrace();
            logger.error("ServiceException occured - " + serviceEx);

        }

        return Optional.ofNullable(emailMessageList);
    }

    public void saveEmailAttachments(String filePath, List<AttachmentInfo> attachmentsInfoList) throws ServiceException {
        if (attachmentsInfoList != null && attachmentsInfoList.size() > 0) {
            for (AttachmentInfo attachmentInfo : attachmentsInfoList) {
                Attachment attachment = service.getAttachment(attachmentInfo);

                if (attachment instanceof FileAttachment) {
                    FileAttachment fileAttachment = (FileAttachment) attachment;
                    String fName = fileAttachment.getName();
                    String fPath = filePath + fName;

                    logger.info("Attachment was found: " + fileAttachment.getName());

                    try {
                        fileAttachment.save(fPath, true);

                        if (fName.endsWith("zip")) {
                            extractThenDeleteZippedFiles(fPath);
                        }

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        logger.error("Couldn't save e-mail attachment - " + ioException);
                    }
                }
            }
        }
    }

    private void extractThenDeleteZippedFiles(String fPath) {
        try {
            ZipFile zipFile = new ZipFile(fPath);

            //Creates a folder to extract files there
            File newFolder = new File(fPath.substring(0, fPath.length() - 4));
            zipFile.extractAll(newFolder.getPath());

            //Deletes the original zipped directory
            File directory = new File(fPath);
            directory.delete();

        } catch (ZipException zipException) {
            zipException.printStackTrace();
            logger.error(" Couldn't extract e-mail attachment - " + zipException);
        }
    }
}
