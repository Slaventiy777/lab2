package ua.sumdu.java2ee.mikhailishinNikolay.messageParser;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ua.sumdu.java2ee.mikhailishinNikolay.controller.Controller;
import ua.sumdu.java2ee.mikhailishinNikolay.server.User;

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
            User user = (User) registeredList.get(key);

            Element userElement = doc.createElement("user");
            userElement.setAttribute("nickname", user.getNickname());
            userElement.setAttribute("password", user.getPassword());
            userElement.setAttribute("rank", String.valueOf(user.getRank()));
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

    public static HashMap<String,User> getRegistrationListFromFile(String registeredFilePath) throws ParserConfigurationException, IOException, SAXException {
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

    private static HashMap<String,User> getRegistrationListFromDocument(Document document){
        HashMap<String,User> regList = new LinkedHashMap<String,User>();

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

                Node rankNode = regInfoAttributes.getNamedItem("rank");
                int rank = 2000;
                if (rankNode != null){
                    rank = Integer.parseInt(rankNode.getNodeValue());
                }

                if(!nickname.isEmpty()){
                    regList.put(nickname, new User(nickname, password, rank));
                }
            }
        }

        return regList;
    }

    public static String createStatusMessage(String messageType, String statusType, String statusDescription) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element operationElement = doc.createElement("operation");
        operationElement.setAttribute("name", messageType);
        doc.appendChild(operationElement);

        Element errorDescriptionElement = doc.createElement(messageType);
        errorDescriptionElement.setAttribute("name", statusType.toString());
        errorDescriptionElement.setAttribute("description", statusDescription);
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

    public static String createMessage(String operation, HashMap param) throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element operationElement = doc.createElement("operation");
        operationElement.setAttribute("name", operation);
        doc.appendChild(operationElement);

        Element reginfoElement = doc.createElement("info");
        for (Object key:param.keySet()){
            reginfoElement.setAttribute((String)key, (String)param.get(key));
        }
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
        LinkedList<Controller.ErrorType> errorTypes = getStatusTypesFromMessage("error", message);
        return errorTypes;
    }

    public static LinkedList<Controller.StatusType> getStatusTypes(String message) throws ParserConfigurationException, IOException, SAXException {
        LinkedList<Controller.StatusType> statusTypes = getStatusTypesFromMessage("status", message);
        return statusTypes;
    }

    private static LinkedList getStatusTypesFromMessage(String messageType, String message) throws ParserConfigurationException, IOException, SAXException {
        LinkedList statusTypes = new LinkedList();

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

                Node statusDescriptionNode = regInfoAttributes.getNamedItem("description");
                String statusDescription = statusDescriptionNode.getNodeValue();

                Node statusNameNode = regInfoAttributes.getNamedItem("name");
                if (messageType.equals("error")){
                    System.err.println(statusDescription);
                    Controller.ErrorType errorType = Controller.ErrorType.valueOf(statusNameNode.getNodeValue());
                    statusTypes.add(errorType);
                }
                else if(messageType.equals("status")){
                    System.out.println(statusDescription);
                    Controller.StatusType statusType = Controller.StatusType.valueOf(statusNameNode.getNodeValue());
                    statusTypes.add(statusType);
                }
            }
        }

        return statusTypes;
    }
}
