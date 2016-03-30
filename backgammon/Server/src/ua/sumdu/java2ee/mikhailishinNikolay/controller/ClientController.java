package ua.sumdu.java2ee.mikhailishinNikolay.controller;

import org.xml.sax.SAXException;
import ua.sumdu.java2ee.mikhailishinNikolay.messageParser.MessageParser;
import ua.sumdu.java2ee.mikhailishinNikolay.transport.ClientTransport;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class ClientController extends Controller {

    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private ClientTransport clientTransport;

    public ClientController(Socket socket) throws IOException {
        clientTransport = new ClientTransport(socket);
        clientTransport.setClientController(this);
    }

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
        }
    }

    @Override
    protected void processError(String message) throws ParserConfigurationException, IOException, SAXException {
        LinkedList<ErrorType> errorTypes = MessageParser.getErrorTypes(message);

        for (ErrorType errorType : errorTypes) {
            switch (errorType) {
                case REGESTRATION:
                    try {
                        registerOnServer();
                    } catch (ParserConfigurationException e) {
                        System.err.println("Registration error");
                    } catch (TransformerException e) {
                        System.err.println("Registration error");
                    } catch (IOException e) {
                        System.err.println("Registration error");
                    }
                    break;
            }
        }
    }

    public String createRegistrationMessage(String nickname, String password) throws ParserConfigurationException, TransformerException {

        return MessageParser.createRegistrationMessage(nickname, password);
    };

    public void registerOnServer() throws IOException, TransformerException, ParserConfigurationException {
        System.out.println("\n-----------------Registration-----------------");
        System.out.println("\nEnter username: ");
        String username = br.readLine();
        System.out.println("\nEnter password: ");
        String password = br.readLine();

        String message = createRegistrationMessage(username, password);
        clientTransport.send(message);
    }
}
