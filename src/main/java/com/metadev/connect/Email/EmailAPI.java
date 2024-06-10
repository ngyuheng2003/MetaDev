package com.metadev.connect.Email;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.EmailClientBuilder;
import com.azure.communication.email.models.EmailAddress;
import com.azure.communication.email.models.EmailMessage;
import com.azure.communication.email.models.EmailSendResult;
import com.azure.core.util.polling.SyncPoller;

public class EmailAPI {
    public static void sendEmail(String emailSU, String subject, String html){
        //setting connection to service provider
        String connectionString = "";
        EmailClient emailClient = new EmailClientBuilder().connectionString(connectionString).buildClient();

        //configuring email
        EmailMessage emailMessage = new EmailMessage()
                .setSenderAddress("")
                .setToRecipients(new EmailAddress(emailSU))
                .setSubject(subject)
                .setBodyHtml(html);

        //send email
        SyncPoller<EmailSendResult, EmailSendResult> poller = emailClient.beginSend(emailMessage, null);
    }

}