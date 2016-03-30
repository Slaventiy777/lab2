package ua.sumdu.java2ee.mikhailishinNikolay.controller;

import org.xml.sax.SAXException;
import ua.sumdu.java2ee.mikhailishinNikolay.messageParser.MessageParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public abstract class Controller {
    public static enum ErrorType {REGESTRATION, OTHER};

    abstract protected void processOperation(String operationName, String message);
    abstract protected void processError(String message) throws ParserConfigurationException, IOException, SAXException;

    public void processMessage(String message) throws ParserConfigurationException, IOException, SAXException {

        if (message.equals("")){
            return;
        }

        String operationName = MessageParser.getOperationName(message);

        processOperation(operationName, message);
    }
}
