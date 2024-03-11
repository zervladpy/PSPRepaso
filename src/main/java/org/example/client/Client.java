package org.example.client;

import java.io.IOException;
import java.util.Scanner;

public abstract class Client {

    private final Scanner scanner;
    private boolean running;

    public Client() {
        this.running = true;
        this.scanner = new Scanner(System.in);
    }

    abstract void start();
    abstract void stop() throws IOException;
    abstract void send(String message) throws IOException;
    abstract void read() throws IOException;

    public Scanner getScanner() {
        return scanner;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
