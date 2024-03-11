package org.example.worker;

import jakarta.mail.MessagingException;
import org.example.email.EmailReader;
import org.example.email.EmailSender;
import org.example.ftp.DownloadFTP;
import org.example.ftp.UploadFTP;
import org.example.server.Server;

import java.io.IOException;
import java.util.logging.Logger;

import static org.example.utils.Protocol.*;

public abstract class MainServerWorker implements Runnable {

    private final Server server;

    public MainServerWorker(Server server) {
        this.running = true;
        this.server = server;
    }

    private boolean running;
    public void start() throws IOException {
        Logger.getGlobal().info("Worker started");
        String welcomeMessage = """
                0 - Exit
                1 - Send Email
                2 - Read Emails
                3 - Upload File FTP
                4 - Download File FTP
                """;

        try {
            while (this.getRunning()) {
                send(welcomeMessage);
                String message = read();

                switch (message.substring(0, 1)) {
                    case EXIT -> stop();
                    case SEND_EMAIL -> sendEmail();
                    case READ_EMAILS -> readEmails();
                    case UPLOAD_FILE_FTP -> uploadFileFTP();
                    case DOWNLOAD_FILE_FTP -> downloadFileFTP();
                    default -> send(" Invalid option");
                }
            }

        } catch (IOException e) {
            Logger.getGlobal().severe(server.getClass().getName() + e.getMessage());
            stop();
        }
    };
    public abstract void stop() throws IOException;
    public void sendEmail() throws IOException {
        new EmailSender(this).start();
    };
    public void readEmails() throws IOException {
        try {
            new EmailReader(this).start();
        } catch (MessagingException e) {
            Logger.getGlobal().severe(e.getMessage());
        }
    }
    public void uploadFileFTP() throws IOException {
        new UploadFTP(this).start();
    }

    public  void downloadFileFTP() throws IOException {
        new DownloadFTP(this).start();
    }

    public boolean getRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Server getServer() {
        return server;
    }

    @Override
    public void run() {
        try {
            this.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void send(String message) throws IOException;
    public abstract String read() throws IOException;



}
