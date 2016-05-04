package com.controller;

import com.messageParser.MessageParser;
import com.User;
import com.transport.ServerTransport;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.TransformerException;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class ServerController extends Controller {
    private final static String registeredFilePath = "registered.xml";

    private HashMap<String,User> registeredList = new LinkedHashMap<String,User>();
    private LinkedList<User> freeList = new LinkedList<>();
    private LinkedList<User> busyList = new LinkedList<>();

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
            case "connection":
                try {
                    processConnection(message);
                } catch (IOException e) {
                    //Исправлю потом
                    //e.printStackTrace();
                } catch (SAXException e) {
                    //Исправлю потом
                    //e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    //Исправлю потом
                    //e.printStackTrace();
                } catch (TransformerException e) {
                    //Исправлю потом
                    //e.printStackTrace();
                }
            case "error":
                processError(message);
                break;
        }
    }

    public void processRegistration(String message) throws IOException, SAXException, ParserConfigurationException {
        HashMap<String,User> regList = MessageParser.getRegistrationListFromMessage(message);

        for (Object key : regList.keySet()) {
            User user = regList.get(key);
            registration(user);
        }
    }

    public void processConnection(String message) throws IOException, SAXException, ParserConfigurationException, TransformerException {

        HashMap<String,User> regList = MessageParser.getRegistrationListFromMessage(message);

        for (Object key : regList.keySet()) {
            User user = regList.get(key);
            connection(user);
        }

    }

    public void processError(String message){

    }

    @Override
    protected void processStatus(String message) throws ParserConfigurationException, IOException, SAXException {

    }

    private void registration(User user){

        if (registeredList.containsKey(user.getNickname())){
            try {
                System.out.println("User "+user.getNickname()+" registration error!");
                String errorMessage = MessageParser.createStatusMessage("error", ErrorType.REGISTRATION.toString(), "This username is already taken");
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
            registeredList.put(user.getNickname(),user);
            try {
                saveRegisteredList();
                System.out.println("User "+user.getNickname()+" has been successfully registered!");
                String statusMessage = MessageParser.createStatusMessage("status", StatusType.REGISTRATION.toString(), "Registration completed successfully!");
                serverTransport.send(statusMessage);
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
    }

    private void connection(User user) throws TransformerException, ParserConfigurationException, IOException {
        if (registeredList.containsKey(user.getNickname())) {
            User regUser = (User) registeredList.get(user.getNickname());
            String regPassword = regUser.getPassword();
            if(regPassword.equals(user.getPassword())){
                freeList.add(regUser);
                System.out.println("User "+regUser.getNickname()+" has been successfully connected!");
                String statusMessage = MessageParser.createStatusMessage("status", StatusType.CONNECTION.toString(), "Connection completed successfully!");
                serverTransport.send(statusMessage);
            }
            else{
                System.out.println("Password incorrect!");
                String errorMessage = MessageParser.createStatusMessage("error", ErrorType.CONNECTION.toString(), "Password incorrect!");
                serverTransport.send(errorMessage);
            }
        }
        else {
            System.out.println("Username incorrect!");
            String errorMessage = MessageParser.createStatusMessage("error", ErrorType.CONNECTION.toString(), "Username incorrect!");
            serverTransport.send(errorMessage);
        }
    }
}
