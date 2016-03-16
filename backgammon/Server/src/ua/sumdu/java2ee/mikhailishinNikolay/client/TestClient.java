package ua.sumdu.java2ee.mikhailishinNikolay.client;


import ua.sumdu.java2ee.mikhailishinNikolay.transport.ClientTransport;
import ua.sumdu.java2ee.mikhailishinNikolay.transport.Transport;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
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
        try {
            File regFile = clientTransport.createRegistrationFile("player2", "123");
            clientTransport.send(regFile);
        } catch (ParserConfigurationException e) {
            System.err.println("File creation error");
        } catch (TransformerException e) {
            System.err.println("File creation error");
        }


    }

}
