package ua.sumdu.java2ee.mikhailishinNikolay.server;

import ua.sumdu.java2ee.mikhailishinNikolay.transport.ServerTransport;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(1024);
        System.out.println("Server starts...");
        while (! Thread.interrupted()) {
            Socket clientSocket = server.accept();
            System.out.println("Connection from "+clientSocket.getRemoteSocketAddress());

            //waiting for event from client
            new ServerTransport(clientSocket);
        }
    }
}
