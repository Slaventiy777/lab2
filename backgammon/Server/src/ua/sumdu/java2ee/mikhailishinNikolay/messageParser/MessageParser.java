package ua.sumdu.java2ee.mikhailishinNikolay.messageParser;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ua.sumdu.java2ee.mikhailishinNikolay.controller.Controller;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class MessageParser {

    public static void saveRegisteredList(String registeredFilePath, HashMap registeredList) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element registeredListElement = doc.createElement("users");
        doc.appendChild(registeredListElement);

        for (Object key : registeredList.keySet()) {
            Element userElement = doc.createElement("user");
            userElement.setAttribute("nickname", (String)key);
            userElement.setAttribute("password", (String)registeredList.get(key));
            registeredListElement.appendChild(userElement);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(registeredFilePath));
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
    }

    public static HashMap getRegistrationListFromFile(String registeredFilePath) throws ParserConfigurationException, IOException, SAXException {
        HashMap registeredList = new LinkedHashMap<String,String>();

        File file = new File(registeredFilePath);
        if (!file.exists()){
            //file does not exist
            System.out.println("File does not exist. It will be created ");
        }

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        DocumentBuilder builder = f.newDocumentBuilder();
        Document doc = builder.parse(new File(registeredFilePath));

        return getRegistrationListFromDocument(doc);
    }

    public static HashMap getRegistrationListFromMessage(String message) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(message)));

        return getRegistrationListFromDocument(document);
    }

    private static HashMap getRegistrationListFromDocument(Document document){
        HashMap regList = new LinkedHashMap<String,String>();

        Node firstNode = document.getFirstChild();

        NodeList paramNodes = firstNode.getChildNodes();
        for(int j = 0; j < paramNodes.getLength()-1; j++) {
            Node regInfo = paramNodes.item(j);

            if (regInfo.hasAttributes()) {
                NamedNodeMap regInfoAttributes = regInfo.getAttributes();
                Node nicknameNode = regInfoAttributes.getNamedItem("nickname");
                String nickname = nicknameNode.getNodeValue();
                Node passwordNode = regInfoAttributes.getNamedItem("password");
                String password = passwordNode.getNodeValue();

                if(!nickname.isEmpty()){
                    regList.put(nickname, password);
                }
            }
        }

        return regList;
    }

    public static String createErrorMessage(String errorType, String errorDescription) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element operationElement = doc.createElement("operation");
        operationElement.setAttribute("name", "error");
        doc.appendChild(operationElement);

        Element errorDescriptionElement = doc.createElement("error");
        errorDescriptionElement.setAttribute("name", errorType.toString());
        errorDescriptionElement.setAttribute("description", errorDescription);
        operationElement.appendChild(errorDescriptionElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        ByteArrayOutputStream baotc = new ByteArrayOutputStream();

        StreamResult result = new StreamResult(baotc);
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);

        return baotc.toString();
    }

    public static String getOperationName(String message) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(message)));

        Node firstNode = document.getFirstChild();
        if(!firstNode.getNodeName().equals("operation") || !firstNode.hasAttributes()){
            System.err.println("Incorrect message format");
            return "";
        }

        NamedNodeMap firstNodeAttributes = firstNode.getAttributes();
        Node nameOfFirstNode = firstNodeAttributes.getNamedItem("name");

        return nameOfFirstNode.getNodeValue();
    }

    public static String createRegistrationMessage(String nickname, String password) throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element operationElement = doc.createElement("operation");
        operationElement.setAttribute("name", "registration");
        doc.appendChild(operationElement);

        Element reginfoElement = doc.createElement("reginfo");
        reginfoElement.setAttribute("nickname", nickname);
        reginfoElement.setAttribute("password", password);
        operationElement.appendChild(reginfoElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        ByteArrayOutputStream baotc = new  ByteArrayOutputStream();

        StreamResult result = new StreamResult(baotc);
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);

        return baotc.toString();
    }

    public static LinkedList<Controller.ErrorType> getErrorTypes(String message) throws ParserConfigurationException, IOException, SAXException {
        LinkedList<Controller.ErrorType> errorTypes = new LinkedList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(message)));

        Node firstNode = document.getFirstChild();
        if(!firstNode.getNodeName().equals("operation") || !firstNode.hasAttributes()){
            System.err.println("Incorrect message format");
            return null;
        }

        NodeList paramNodes = firstNode.getChildNodes();
        for(int j = 0; j < paramNodes.getLength()-1; j++) {
            Node regInfo = paramNodes.item(j);

            if (regInfo.hasAttributes()) {
                NamedNodeMap regInfoAttributes = regInfo.getAttributes();

                Node errorDescriptionNode = regInfoAttributes.getNamedItem("description");
                String errorDescription = errorDescriptionNode.getNodeValue();
                System.err.println(errorDescription);

                Node errorNameNode = regInfoAttributes.getNamedItem("name");
                Controller.ErrorType errorType = Controller.ErrorType.valueOf(errorNameNode.getNodeValue());
                errorTypes.add(errorType);
            }
        }

        return errorTypes;
    }
}
