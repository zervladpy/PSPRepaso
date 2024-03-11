package org.example.worker;

import org.example.server.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UDPServerWorker extends MainServerWorker {

    private final DatagramSocket socket;
    private final int port;
    private final InetAddress address;
    private final byte[] buffer;
    public UDPServerWorker(Server server, byte[] buffer, DatagramSocket socket, int port, InetAddress address) {
        super(server);
        this.port = port;
        this.address = address;
        this.socket = socket;
        this.buffer = buffer;
    }

    @Override
    public void stop() {

    }

    @Override
    public void sendEmail() {

    }

    @Override
    public void readEmails() {

    }

    @Override
    public void uploadFileFTP() {

    }

    @Override
    public void downloadFileFTP() {

    }

    @Override
    public void send(String message) throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket response = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(response);
    }

    @Override
    public String read() throws IOException {
        DatagramPacket request = new DatagramPacket(buffer, buffer.length);
        socket.receive(request);
        return new String(request.getData(), 0, request.getLength(), StandardCharsets.UTF_8);
    }
}
