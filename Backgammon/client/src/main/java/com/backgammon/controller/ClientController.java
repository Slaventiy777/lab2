package com.backgammon.controller;

import com.transport.ClientTransport;
import com.transport.Transport;
import org.xml.sax.SAXException;
import com.messageParser.MessageParser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class ClientController extends Controller {

    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    //private ClientTransport clientTransport;

    /*public ClientController(Socket socket) throws IOException {
        clientTransport = new ClientTransport(socket);
        clientTransport.setClientController(this);
    }*/

    @Override
    protected void processOperation(String operationName, String message) {
        switch (operationName){
            case "error":
                try {
                    processError(message);
                } catch (ParserConfigurationException e) {
                    System.err.println("Internal error");
                } catch (IOException e) {
                    System.err.println("Internal error");
                } catch (SAXException e) {
                    System.err.println("Internal error");
                }
                break;
            case "status":
                try {
                    processStatus(message);
                } catch (ParserConfigurationException e) {
                    System.err.println("Internal error");
                } catch (IOException e) {
                    System.err.println("Internal error");
                } catch (SAXException e) {
                    System.err.println("Internal error");
                }
                break;
        }
    }

    @Override
    protected void processError(String message) throws IOException, SAXException, ParserConfigurationException {
        LinkedList<Transport.ErrorType> errorTypes = MessageParser.getErrorTypes(message);

        for (Transport.ErrorType errorType : errorTypes) {
            switch (errorType) {
                case REGISTRATION:
                    try {
                        openMainQuestion();
                    } catch (ParserConfigurationException e) {
                        System.err.println("Registration error!");
                    } catch (TransformerException e) {
                        System.err.println("Registration error!");
                    } catch (IOException e) {
                        System.err.println("Registration error!");
                    }
                    break;
                case CONNECTION:
                    try {
                        openMainQuestion();
                    } catch (TransformerException e) {
                        System.err.println("Connection error!");
                    }
                    break;
            }
        }
    }

    @Override
    protected void processStatus(String message) throws ParserConfigurationException, IOException, SAXException {
        LinkedList<Transport.StatusType> statusTypes = MessageParser.getStatusTypes(message);

        for (Transport.StatusType statusType : statusTypes) {
            switch (statusType) {
                case REGISTRATION:
                    try {
                        openMainQuestion();
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    }
                    break;
                case CONNECTION:
                    try {
                        openConnectionQuestion();
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public void openMainQuestion() throws ParserConfigurationException, TransformerException, IOException {
        System.out.println("\nChoose operation:");
        System.out.println("1. Registration");
        System.out.println("2. Connection");
        System.out.println("0. Exit");

        int answer = 0;
        try {
            answer = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            System.out.println("Invalid operation!");
            openMainQuestion();
            return;
        }
        catch (NumberFormatException e){
            System.out.println("Invalid operation!");
            openMainQuestion();
            return;
        }

        switch (answer) {
            case 1: registerOnServer();
                break;
            case 2: connectToServer();
                break;
            case 0:  exit();
                break;
            default:
                System.out.println("Invalid operation!");
                openMainQuestion();
                break;
        }
    }

    public void openConnectionQuestion() throws IOException, TransformerException, ParserConfigurationException {
        System.out.println("\nChoose operation:");
        System.out.println("1. Get gamer list");
        System.out.println("0. Exit");

        int answer = 0;
        try {
            answer = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            System.out.println("Invalid operation!");
            openConnectionQuestion();
            return;
        }
        catch (NumberFormatException e){
            System.out.println("Invalid operation!");
            openConnectionQuestion();
            return;
        }

        switch (answer) {
            case 1: getFreeUsers();
                break;
            case 0:  exit();
                break;
            default:
                System.out.println("Invalid operation!");
                openConnectionQuestion();
                break;
        }
    }

    public void registerOnServer() throws IOException, TransformerException, ParserConfigurationException {
        System.out.println("\n-----------------Registration-----------------");
        System.out.println("\nEnter username: ");
        String username = br.readLine();
        System.out.println("\nEnter password: ");
        String password = br.readLine();

        String message = createRegistrationMessage(username, password);
        clientTransport.send(message);
    }

    public String createRegistrationMessage(String nickname, String password) throws ParserConfigurationException, TransformerException {
        HashMap param = new LinkedHashMap();
        param.put("nickname", nickname);
        param.put("password", password);
        return MessageParser.createMessage("registration", param);
};

    public void connectToServer() throws IOException, TransformerException, ParserConfigurationException {
        System.out.println("\n-----------------Connection-----------------");
        System.out.println("\nEnter username: ");
        String username = br.readLine();
        System.out.println("\nEnter password: ");
        String password = br.readLine();

        String message = createConnectionMessage(username, password);
        clientTransport.send(message);
    }

    public void getFreeUsers() throws IOException, TransformerException, ParserConfigurationException {
        String message = createGetFreeUsersMessage();
        clientTransport.send(message);
    }

    public String createConnectionMessage(String nickname, String password) throws TransformerException, ParserConfigurationException {
        HashMap param = new LinkedHashMap();
        param.put("nickname", nickname);
        param.put("password", password);
        return MessageParser.createMessage("connection", param);
    }

    public String createGetFreeUsersMessage() throws TransformerException, ParserConfigurationException {
        return MessageParser.createMessage("getFreeUsers", new LinkedHashMap());
    }

    public void exit(){
        System.exit(1);
    }
}
