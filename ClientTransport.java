package ua.sumdu.java2ee.mikhailishinNikolay.transport;

import org.xml.sax.SAXException;
import ua.sumdu.java2ee.mikhailishinNikolay.controller.ClientController;

import javax.xml.parsers.ParserConfigurationException;

import java.io.*;
import java.net.Socket;

public class ClientTransport extends Transport {

    ClientController clientController;

    public ClientTransport(Socket socket) throws IOException {
        super(socket);
        startEventLoop();
    }

    public void setClientController(ClientController cc){
        this.clientController = cc;
    }

    @Override
    protected void receive() throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException {

        Object message = input.readObject();

        if (message instanceof String) {
            clientController.processMessage((String) message);
        }

    }

}
