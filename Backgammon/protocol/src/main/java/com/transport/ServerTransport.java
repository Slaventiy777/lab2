package com.transport;

import org.xml.sax.SAXException;
import com.controller.ServerController;

import javax.xml.parsers.*;

import java.io.*;
import java.net.Socket;

public class ServerTransport extends Transport {

    private ServerController serverController;

    public ServerTransport(Socket socket) throws IOException, ParserConfigurationException, SAXException {
        super(socket);
        startEventLoop();
    }

    public void setServerController(ServerController sc){
        this.serverController = sc;
    }

    @Override
    protected void receive() throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException {

        Object message = input.readObject();

        if (message instanceof String) {
            serverController.processMessage((String) message);
        }

    }
}
