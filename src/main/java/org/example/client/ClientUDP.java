package org.example.client;

import org.example.utils.ConnParams;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import static org.example.utils.ConnParams.HOST;

public class ClientUDP extends Client{

    private DatagramSocket socket;
    private InetAddress address;
    private byte[] buffer;

    public ClientUDP() {
        super();
        this.buffer = new byte[1024];
    }

    public ClientUDP(byte[] buffer) {
        super();
        this.buffer = buffer;
    }

    @Override
    public void start() {

        try {
            address = InetAddress.getByName(HOST);
            socket = new DatagramSocket();

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
    void stop() {
        socket.close();
        setRunning(false);
    }

    @Override
    void send(String message) throws IOException {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, ConnParams.PORT + 1);
        packet.setData(message.getBytes());
        socket.send(packet);
    }

    @Override
    void read() throws IOException {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String message = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
        Logger.getGlobal().info("Server Response:");
        System.out.println(message);
    }
}
