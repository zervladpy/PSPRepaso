package org.example.server;

import java.io.IOException;

public abstract class Server implements Runnable {

    private boolean running;

    public Server() {
        this.running = true;
    }

    public abstract void start();
    public abstract void stop() throws IOException;
    public boolean isRunning() {
        return running;
    }
    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        this.start();
    }
}
