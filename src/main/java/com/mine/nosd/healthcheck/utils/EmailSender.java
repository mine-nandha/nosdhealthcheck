package com.mine.nosd.healthcheck.utils;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailSender {

    public static void send(String username, String password, String to, String cc, String issuesCount, String name,
                            String htmlContent, String signature) {
        final String nosd = "NetworkOpsSupportDesk@virginmedia.ie";
        final String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        session.setDebug(true);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(nosd));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            message.setSubject("DTV Daily Health Check Report | No of Issues:" + issuesCount + " |  " + date);

            Multipart multipart = new MimeMultipart();

            BodyPart bodyPart = new MimeBodyPart();

            bodyPart.setContent(htmlContent + signature, "text/html");

            multipart.addBodyPart(bodyPart);

            // Set the Multipart as the content of the message
            message.setContent(multipart);

            // Send the email
            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
