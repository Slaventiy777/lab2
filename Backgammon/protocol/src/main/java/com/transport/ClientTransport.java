package com.transport;

import org.xml.sax.SAXException;
//import com.controller.ClientController;

import javax.xml.parsers.ParserConfigurationException;

import java.io.*;
import java.net.Socket;

public class ClientTransport extends Transport {

    //ClientController clientController;

    public ClientTransport(Socket socket) throws IOException {
        super(socket);
        //startEventLoop();
    }

    /*public void setClientController(ClientController cc){
        this.clientController = cc;
    }*/

    @Override
    public Object receive() throws IOException, ClassNotFoundException  {

        Object message = input.readObject();

        return message;

        /*if (message instanceof String) {
            clientController.processMessage((String) message);
        }*/

    }

}
