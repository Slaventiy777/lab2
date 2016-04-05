package ua.sumdu.java2ee.mikhailishinNikolay.client;


import ua.sumdu.java2ee.mikhailishinNikolay.controller.ClientController;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.Socket;

public class TestClient {
    private String nickname;

    public TestClient(String nickname) {
        this.nickname = nickname;
    }

    public static void main(String[] args) throws IOException {
        TestClient client = new TestClient("Test client");
        System.out.println("\nConnecting...");
        Socket clientSocket = new Socket("localhost", 1024);
        ClientController clientController = new ClientController(clientSocket);
        try {
            clientController.openMainQuestion();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

}
