package lab2.server.controller;

import lab2.server.model.Model;
import lab2.server.model.ModelImpl;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class StartServer {

    private static final Integer NUMBER_PORT = 7777;

    private static final Logger log = Logger.getLogger(lab2.server.controller.StartServer.class);

    private static ServerSocket serverSocket;
    private static Connection connection;
    private static Set<Connection> connections = new HashSet<>();

    private static Model userModel = new ModelImpl();
    static volatile List<GameRoom> listRoom = new LinkedList<>();
    
    public static void main(String[] args) throws IOException {
        
        try {
            serverSocket = new ServerSocket(NUMBER_PORT);
            System.out.println("Start listening...");
            while (true) {
                Socket socket = serverSocket.accept();
                connection = new Connection(socket, userModel, connections);
            }
        } catch (IOException e) {
            log.error("Error process server (IOException)!");
            close();
        }

    }
    
    private static void close() {

        try{
            connection.closeAll();
            serverSocket.close();
        } catch(IOException e) {
            log.error("Error closing the socket (IOException)!", e);
        } finally {
            userModel.storageList();
        }

    }

}
