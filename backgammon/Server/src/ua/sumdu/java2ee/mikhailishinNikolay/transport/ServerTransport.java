package ua.sumdu.java2ee.mikhailishinNikolay.transport;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;
import java.util.*;

public class ServerTransport extends Transport {

    private final static String registeredFilePath = "registered.xml";

    private LinkedHashMap registeredList = new LinkedHashMap<String,String>();

    public ServerTransport(Socket socket) throws IOException, ParserConfigurationException, SAXException {
        super(socket);
        loadRegisteredList();
        startEventLoop();
    }

    private void saveRegisteredList() throws ParserConfigurationException, TransformerException {
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

    private void loadRegisteredList() throws ParserConfigurationException, IOException, SAXException {
        File file = new File(registeredFilePath);
        if (!file.exists()){
            //file does not exist
            System.out.println("File does not exist. It will be created ");
            return;
        }

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        DocumentBuilder builder = f.newDocumentBuilder();
        Document doc = builder.parse(new File(registeredFilePath));

        Node node = doc.getFirstChild();
        NodeList usersNodes = node.getChildNodes();

        for (int i = 0; i < usersNodes.getLength(); i++) {
            Node userNode = usersNodes.item(i);

            if (userNode.hasAttributes()) {
                NamedNodeMap regInfoAttributes = userNode.getAttributes();
                Node nicknameNode = regInfoAttributes.getNamedItem("nickname");
                String nickname = nicknameNode.getNodeValue();
                Node passwordNode = regInfoAttributes.getNamedItem("password");
                String password = passwordNode.getNodeValue();

                if(!nickname.isEmpty()){
                    registeredList.put(nickname, password);
                }
            }
        }

    }

    @Override
    protected File receive() throws IOException, ParserConfigurationException, SAXException {
        long fileSize = input.readLong();
        String fileName = input.readUTF();
        System.out.println("File name: " + fileName);
        System.out.println("File size: " + fileSize + " byte");

        byte[] buffer = new byte[64*1024];
        File newFile = new File("transfer_received.xml");
        FileOutputStream outF = new FileOutputStream(newFile);
        int count, total = 0;

        while ((count = input.read(buffer)) != -1){
            total += count;
            outF.write(buffer, 0, count);

            if(total == fileSize){
                break;
            }
        }
        outF.flush();
        outF.close();

        System.out.println("File \""+fileName+"\" has been received");

        processFile(newFile);

        return newFile;
    }

    private void processFile(File currentFile) throws ParserConfigurationException, IOException, SAXException {
        if (!currentFile.exists()){
            //file does not exist
            System.err.println("File does not exist");
            return;
        }

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        DocumentBuilder builder = f.newDocumentBuilder();
        Document doc = builder.parse(currentFile);

        Node firstNode = doc.getFirstChild();
        if(!firstNode.getNodeName().equals("operation") || !firstNode.hasAttributes()){
            System.err.println("Incorrect file format");
            return;
        }

        NamedNodeMap firstNodeAttributes = firstNode.getAttributes();
        Node nameOfFirstNode = firstNodeAttributes.getNamedItem("name");
        String operationName = nameOfFirstNode.getNodeValue();
        switch (operationName){
            case "registration":
                processRegistration(firstNode);
        }

    }

    private void processRegistration(Node firstNode){
        HashMap regList = new LinkedHashMap<String,String>();

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

        for (Object key : regList.keySet()) {
            registration((String)key, (String)regList.get(key));
        }
    }

    private void registration(String nickname, String password){

        if (registeredList.containsKey(nickname)){
            //обработать повторную регистрацию
        }
        else {
            registeredList.put(nickname,password);
            try {
                saveRegisteredList();
            } catch (ParserConfigurationException e) {
                //e.printStackTrace();
            } catch (TransformerException e) {
                //e.printStackTrace();
            }
        }
    }
}
