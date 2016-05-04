package com.server;

import org.xml.sax.SAXException;
import com.controller.ServerController;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(1024);
        System.out.println("Server starts...");
        while (! Thread.interrupted()) {
            Socket clientSocket = server.accept();
            System.out.println("\nConnection from "+clientSocket.getRemoteSocketAddress());

            //waiting for event from client
            try {
                new ServerController(clientSocket);
            } catch (ParserConfigurationException e) {
                //Исправлю потом
                //e.printStackTrace();
            } catch (SAXException e) {
                //Исправлю потом
                //e.printStackTrace();
            }
        }
    }
}
