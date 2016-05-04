package com.backgammon.controller;

import com.messageParser.MessageParser;

import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public abstract class Controller {

    abstract protected void processOperation(String operationName, String message);
    abstract protected void processError(String message) throws ParserConfigurationException, IOException, SAXException;
    abstract protected void processStatus(String message) throws ParserConfigurationException, IOException, SAXException;

    public void processMessage(String message) throws ParserConfigurationException, IOException, SAXException {

        if (message.equals("")){
            return;
        }

        String operationName = MessageParser.getOperationName(message);

        processOperation(operationName, message);

    }

}
