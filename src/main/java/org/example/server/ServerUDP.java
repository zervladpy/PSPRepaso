package org.example.server;

import org.example.utils.ConnParams;
import org.example.worker.UDPServerWorker;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Logger;

import static org.example.utils.ConnParams.*;

public class ServerUDP extends Server {

    @Override
    public void start() {
        Logger.getGlobal().info("Server starting on 0.0.0.0:" + PORT + 1);
        try(DatagramSocket socket = new DatagramSocket(PORT + 1)) {
            Logger.getGlobal().info("Server started");
            while (this.isRunning()) {
                byte[] buffer = new byte[1024];

                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);
                int port = request.getPort();
                InetAddress address = request.getAddress();
                Logger.getGlobal().info(getClass().getName() + ": Client connected");

                new Thread(new UDPServerWorker(this, buffer, socket, port, address)).start();
            }

        } catch (IOException e) {
            Logger.getGlobal().severe(e.getMessage());
        }
    }

    @Override
    public void stop() {
        this.setRunning(false);
    }
}
