package ua.sumdu.java2ee.mikhailishinNikolay.client;


import ua.sumdu.java2ee.mikhailishinNikolay.transport.ClientTransport;
import ua.sumdu.java2ee.mikhailishinNikolay.transport.Transport;

import java.io.IOException;
import java.net.Socket;

public class TestClient {
    private String nickname;

    public TestClient(String nickname) {
        this.nickname = nickname;
    }

    public static void main(String[] args) throws IOException {
        TestClient client = new TestClient("Test client");
        System.out.println("Connecting...");
        Socket clientSocket = new Socket("localhost", 1024);
        System.out.println("Registering...");
        ClientTransport clientTransport = new ClientTransport(clientSocket);
        clientTransport.send();
    }

}
