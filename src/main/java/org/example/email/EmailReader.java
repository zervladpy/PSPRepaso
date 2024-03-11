package org.example.email;

import jakarta.mail.*;
import org.example.worker.MainServerWorker;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EmailReader {
    private final MainServerWorker worker;
    private Session session;
    private Authenticator authenticator;
    private Properties properties;
    private boolean connected;
    private Store store;
    private Folder folder;
    public EmailReader(MainServerWorker worker) throws IOException {
        FileInputStream input = new FileInputStream("src/main/resources/imap.properties");
        properties = new Properties();
        properties.load(input);
        this.worker = worker;
    }

    public void start() throws IOException, MessagingException {
        worker.send("Email: ");
        String email = worker.read();

        worker.send("Password: ");
        String password = worker.read();

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        };

        session = Session.getInstance(properties, authenticator);
        store = session.getStore("imaps");

        connected = true;
        store.connect(properties.getProperty("mail.imap.host"), email, password);

        while (connected) {
            String option = menu();
            switch (option) {
                case "0" -> connected = false;
                case "1" -> listEmails();
                default -> worker.send("Invalid option");
            }
        }

        stop();
    }

    private String menu() throws IOException {
        String menu = """
                0 - Exit
                1 - List Emails and Content
                """;
        worker.send(menu);

        return worker.read();
    }

    private void listEmails() throws MessagingException, IOException {
        folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);
        Message[] messages = folder.getMessages();
        StringBuilder emailList = new StringBuilder();
        emailList.append("0 - Exit\n");

        for (int i = 0; i < messages.length; i++) {
            emailList.append(i + 1).append(" - Identificador").append("\n");
            emailList.append("From: ").append(messages[i].getFrom()[0]).append("\n");
            emailList.append("Subject: ").append(messages[i].getSubject()).append("\n");
            emailList.append("Date: ").append(messages[i].getSentDate()).append("\n");
            emailList.append("-----------------------------------").append("\n");
            emailList.append("\n");
        }

        emailList.append("Total: ").append(messages.length).append("\n");
        emailList.append("Select an email:");
        worker.send(emailList.toString());

        String selection = worker.read();

        if (selection.equals("0")) {
            return;
        }

        int index = Integer.parseInt(selection) - 1;

        StringBuilder emailContent = new StringBuilder();

        emailContent.append("From: ").append(messages[index].getFrom()[0]).append("\n");
        emailContent.append("Subject: ").append(messages[index].getSubject()).append("\n");
        emailContent.append("Date: ").append(messages[index].getSentDate()).append("\n");
        emailContent.append("Content: ").append(messages[index].getContent().toString()).append("\n");
        emailContent.append("0 - Exit\n");

        worker.send(emailContent.toString());
        worker.read();
    }

    public void stop() throws MessagingException {
        folder.close(false);
        store.close();
    }

}
