package com.backgammon.model;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class Settings {

    public static final File FILE = new File("Settings.xml");
    static Logger log = Logger.getLogger(Settings.class.getName());

    // user
    private static String username = "";
    private static String password = "";


    public static void writeSettingsIntoXML() {

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("setting");
            doc.appendChild(rootElement);

            Element usernameElement = doc.createElement("username");
            usernameElement.appendChild(doc.createTextNode(getUsername()));
            rootElement.appendChild(usernameElement);

            Element passElement = doc.createElement("password");
            passElement.appendChild(doc.createTextNode(getPassword()));
            rootElement.appendChild(passElement);

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(FILE);
            transformer.transform(source, result);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    public static void readSettingsFromXML() {

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(FILE);

            NodeList nodeList = doc.getElementsByTagName("settings");

            Element rootElement = (Element) nodeList.item(0);
            setUsername(rootElement.getElementsByTagName("username").item(0).getChildNodes().item(0).getNodeValue());
            setPassword(rootElement.getElementsByTagName("password").item(0).getChildNodes().item(0).getNodeValue());

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // getter`s

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    // setter`s

    public static void setUsername(String username) {
        Settings.username = username;
    }

    public static void setPassword(String password) {
        Settings.password = password;
    }
}
