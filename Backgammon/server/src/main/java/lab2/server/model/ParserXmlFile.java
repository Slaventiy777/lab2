package lab2.server.model;

import lab2.protocol.User;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Set;


public class ParserXmlFile {

    private static final Logger log = Logger.getLogger(lab2.server.model.ModelImpl.class);

    public static void writeToXML(Set<User> users, File file) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            throw new Exception(ex);
        }

        Document document = documentBuilder.newDocument();

        Element userListEl = document.createElement("listOfUsers");
        document.appendChild(userListEl);

        for (User user : users) {
            Element userEl = document.createElement("user");
            userListEl.appendChild(userEl);

            Element nicknameEl = document.createElement("nickname");
            nicknameEl.appendChild(document.createTextNode(user.getNickname()));
            userEl.appendChild(nicknameEl);

            Element passwordEl = document.createElement("password");
            passwordEl.appendChild(document.createTextNode(user.getPassword()));
            userEl.appendChild(passwordEl);

            Element rankEl = document.createElement("rank");
            rankEl.appendChild(document.createTextNode(Integer.toString(user.getRank())));
            userEl.appendChild(rankEl);
        }

        TransformerFactory factoryTr = TransformerFactory.newInstance();
        try {
            Transformer transformer = factoryTr.newTransformer();
            DOMSource domSources = new DOMSource(document);
            StreamResult streamResult = new StreamResult(file);
            transformer.transform(domSources, streamResult);
        } catch (TransformerException ex) {
            throw new Exception(ex);
        }

    }

    public static void readFromXML(Set<User> users, File file) throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder documentBuilder;
        Document document;

        documentBuilder = factory.newDocumentBuilder();
        document = documentBuilder.parse(file);
        
        String nickname = null;
        String password = null;
        int rank = 0;
        NodeList nodeList = document.getElementsByTagName("user");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element el = (Element) nodeList.item(i);

            nickname = el.getElementsByTagName("nickname").item(0).getChildNodes().item(0).getNodeValue();
            password = el.getElementsByTagName("password").item(0).getChildNodes().item(0).getNodeValue();
            rank = Integer.parseInt(el.getElementsByTagName("rank").item(0).getChildNodes().item(0).getNodeValue());

            User user = new User(nickname, password);
            user.setRank(rank);
            users.add(user);
        }

    }

}
