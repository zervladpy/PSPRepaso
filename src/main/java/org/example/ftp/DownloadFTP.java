package org.example.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.example.worker.MainServerWorker;

import java.io.*;
import java.util.logging.Logger;

public class DownloadFTP {

    private final MainServerWorker worker;
    private final String host = "192.168.56.1";
    private final int port = 21;
    private final FTPClient ftpClient;

    public DownloadFTP(MainServerWorker worker) {
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

        ftpClient.enterLocalPassiveMode();
        readFTP();

        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        readFTP();


        if (!success) {
            worker.send("Could not login to the server");
            worker.read();
            return;
        }

        worker.send("Remote path: ");
        String pathToDownload = worker.read();

        worker.send("Local path: ");
        String path = worker.read();

        File file = new File(path);

        OutputStream out = new BufferedOutputStream(new FileOutputStream(file));


        boolean downloaded = ftpClient.retrieveFile(pathToDownload,  out);

        if (downloaded) {
            worker.send("File downloaded successfully");
        } else {
            worker.send("Could not download the file");
        }

        worker.read();
        
        stop();

    }

    public void stop() throws IOException {
        ftpClient.logout();
        ftpClient.disconnect();
    }

    public void readFTP() {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                Logger.getGlobal().info("SERVER: " + aReply);
            }
        }
    }
}
