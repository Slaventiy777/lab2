package ua.sumdu.java2ee.mikhailishinNikolay.transport;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;

public class ClientTransport extends Transport {
    public ClientTransport(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    protected File receive() throws IOException, ParserConfigurationException, SAXException{
        return new File("");
    }

    public File createRegistrationFile(String nickname, String password) throws ParserConfigurationException, TransformerException {
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
        //String fileName = "transfer.xml";
        File resultFile = new File("transfer.xml");

        StreamResult result = new StreamResult(resultFile);
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);

        return resultFile;
    }
}
