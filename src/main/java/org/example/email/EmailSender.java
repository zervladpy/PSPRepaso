package org.example.email;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.example.worker.MainServerWorker;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class EmailSender {
    private final MainServerWorker worker;
    private final Email email;
    private final Properties properties;
    private Session session;
    private Authenticator authenticator;

    public EmailSender(MainServerWorker worker) throws IOException {
        InputStream input = new FileInputStream("src/main/resources/smtp.properties");
        this.worker = worker;
        properties = new Properties();
        properties.load(input);
        session = null;
        email = new Email();
    }

    public MainServerWorker getWorker() {
        return worker;
    }

    public void sendEmail() {

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(email.getFrom());
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));
            message.setSubject(email.getSubject());
            message.setText(email.getBody());
            Transport.send(message);
        } catch (MessagingException e) {
            Logger.getGlobal().severe(e.getMessage());
        }

    }

    public void start() throws IOException {
        getWorker().send("To: ");
        String emailTo = getWorker().read();
        getEmail().setTo(emailTo);

        getWorker().send("From: ");
        String emailFrom = getWorker().read();

        getWorker().send("Password: ");
        String password = getWorker().read();

        getEmail().setFrom(emailFrom);

        getWorker().send("Subject: ");
        String subject = getWorker().read();

        getEmail().setSubject(subject);

        getWorker().send("Message: ");
        String message = getWorker().read();
        getEmail().setBody(message);

        setAuthenticator(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom, password);
            }

        });

        Session session = Session.getInstance(getProperties(), getAuthenticator());
        setSession(session);

        sendEmail();
    };

    public Email getEmail() {
        return email;
    }
    public Properties getProperties() {
        return properties;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public void setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
    }
}
