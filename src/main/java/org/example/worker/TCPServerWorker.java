package org.example.worker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.example.server.Server;

public class TCPServerWorker extends MainServerWorker {
    private DataInputStream in;
    private DataOutputStream out;
    public TCPServerWorker(Server server, DataInputStream in, DataOutputStream out) {
        super(server);
        this.in = in;
        this.out = out;
    }

    @Override
    public void stop() throws IOException {
        in.close();
        out.close();
        getServer().stop();
    }

    @Override
    public void send(String message) throws IOException {
        out.writeUTF(message);
    }

    @Override
    public String read() throws IOException {
        return in.readUTF();
    }
}
