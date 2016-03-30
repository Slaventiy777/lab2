package ua.sumdu.java2ee.mikhailishinNikolay.controller;


import org.xml.sax.SAXException;
import ua.sumdu.java2ee.mikhailishinNikolay.messageParser.MessageParser;
import ua.sumdu.java2ee.mikhailishinNikolay.transport.ServerTransport;

import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.TransformerException;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ServerController extends Controller {
    private final static String registeredFilePath = "registered.xml";

    private HashMap registeredList = new LinkedHashMap<String,String>();

    private ServerTransport serverTransport;

    public ServerController(Socket socket) throws ParserConfigurationException, SAXException, IOException {
        serverTransport = new ServerTransport(socket);
        serverTransport.setServerController(this);
        loadRegisteredList();
    }

    private void saveRegisteredList() throws ParserConfigurationException, TransformerException {
        MessageParser.saveRegisteredList(registeredFilePath, registeredList);
    }

    private void loadRegisteredList() throws ParserConfigurationException, IOException, SAXException {
        registeredList = MessageParser.getRegistrationListFromFile(registeredFilePath);
    }

    @Override
    public void processOperation(String operationName, String message) {
        switch (operationName){
            case "registration":
                try {
                    processRegistration(message);
                } catch (IOException e) {
                    //Исправлю потом
                    //e.printStackTrace();
                } catch (SAXException e) {
                    //Исправлю потом
                    //e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    //Исправлю потом
                    //e.printStackTrace();
                }
                break;
            case "error":
                processError(message);
                break;
        }
    }

    public void processRegistration(String message) throws IOException, SAXException, ParserConfigurationException {
        HashMap regList = MessageParser.getRegistrationListFromMessage(message);

        for (Object key : regList.keySet()) {
            registration((String)key, (String)regList.get(key));
        }
    }

    public void processError(String message){

    }

    private void registration(String nickname, String password){

        if (registeredList.containsKey(nickname)){
            try {
                System.out.println("User "+nickname+" registration error!");
                String errorMessage = MessageParser.createErrorMessage(ErrorType.REGESTRATION.toString(), "This username is already taken");
                serverTransport.send(errorMessage);
            } catch (ParserConfigurationException e) {
                //Исправлю потом
                //e.printStackTrace();
            } catch (TransformerException e) {
                //Исправлю потом
                //e.printStackTrace();
            } catch (IOException e) {
                //Исправлю потом
                //e.printStackTrace();
            }
        }
        else {
            registeredList.put(nickname,password);
            try {
                saveRegisteredList();
            } catch (ParserConfigurationException e) {
                //Исправлю потом
                //e.printStackTrace();
            } catch (TransformerException e) {
                //Исправлю потом
                //e.printStackTrace();
            }
        }
    }
}
