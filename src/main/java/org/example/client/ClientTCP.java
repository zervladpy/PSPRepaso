package org.example.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import static org.example.utils.ConnParams.HOST;
import static org.example.utils.ConnParams.PORT;

public class ClientTCP extends Client {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ClientTCP() {
        super();
    }

    public static void main(String[] args) {
        new ClientTCP().start();
    }

    @Override
    public void start() {

        try {
            Logger.getGlobal().info(getClass().getName() + " Client Connecting to " + HOST + ":" + PORT);
            socket = new Socket(HOST, PORT);

            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            while (this.isRunning()) {
                this.read();
                String message = this.getScanner().nextLine();
                this.send(message);
            }

        } catch (IOException e) {
            Logger.getGlobal().severe(e.getMessage());
        }

    }

    @Override
    public void stop() throws IOException {
        socket.close();
        setRunning(false);
    }

    @Override
    public void send(String message) throws IOException {
        out.writeUTF(message);
    }

    @Override
    public void read() throws IOException {
        String message = in.readUTF();
        Logger.getGlobal().info("Server Response:");
        System.out.println(message);
    }
}
