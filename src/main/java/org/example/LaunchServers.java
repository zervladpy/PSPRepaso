package org.example;

import org.example.server.ServerTCP;
import org.example.server.ServerUDP;

public class LaunchServers {

    public static void main(String[] args) {

        new Thread(new ServerTCP()).start();
        // new Thread(new ServerUDP()).start();

    }
}
