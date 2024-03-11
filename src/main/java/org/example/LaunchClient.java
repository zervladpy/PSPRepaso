package org.example;

import org.example.client.ClientTCP;

public class LaunchClient {

    public static void main(String[] args) {
        new ClientTCP().start();
        // new ClientUDP().start();
    }
}
