package org.example.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.example.worker.MainServerWorker;


import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class UploadFTP {
    private final MainServerWorker worker;
    private final String host = "192.168.56.1";
    private final int port = 21;
    private final FTPClient ftpClient;

    public UploadFTP(MainServerWorker worker) {
        this.worker = worker;
        this.ftpClient = new FTPClient();
    }

    public void start() throws IOException {

        worker.send("Username: ");
        String username = worker.read();

        worker.send("Password: ");
        String password = worker.read();

        ftpClient.connect(host, port);
        readFTP();

        boolean success = ftpClient.login(username, password);
        readFTP();

        if (!success) {
            worker.send("Could not login to the server");
            worker.read();
            return;
        }

        worker.send("File to upload: ");
        String path = worker.read();

        File file = new File(path);

        success = ftpClient.storeFile(file.getName(), new DataInputStream(file.toURI().toURL().openStream()));
        readFTP();

        if (success) {
            worker.send("File uploaded successfully");
        } else {
            worker.send("Could not upload the file");
        }

        worker.read();
        stop();
    }

    public void readFTP() {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                Logger.getGlobal().info("SERVER: " + aReply);
            }
        }
    }

    public void stop() throws IOException {
        ftpClient.logout();
        ftpClient.disconnect();
    }

}
