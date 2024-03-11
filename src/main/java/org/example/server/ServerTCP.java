package org.example.server;

import org.example.utils.ConnParams;
import org.example.worker.TCPServerWorker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import static org.example.utils.ConnParams.PORT;

public class ServerTCP extends Server {

    public static void main(String[] args) {
        new ServerTCP().start();
    }

    private Socket clientSocket;
    @Override
    public void start() {
        Logger.getGlobal().info(getClass().getName() + " Server Starting on " + ConnParams.HOST + ":" + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            Logger.getGlobal().info("Server started");
            while (this.isRunning()) {
                clientSocket = serverSocket.accept();
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                Logger.getGlobal().info("Client connected");

                new Thread(new TCPServerWorker(this, in, out)).start();
            }

        } catch (IOException e){
            Logger.getGlobal().severe(e.getMessage());
        }
    }

    @Override
    public void stop() throws IOException {
        clientSocket.close();

        this.setRunning(false);
    }

}
