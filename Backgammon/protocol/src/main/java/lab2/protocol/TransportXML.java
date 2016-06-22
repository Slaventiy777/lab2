package lab2.protocol;

import lab2.protocol.envelope.*;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;

public class TransportXML implements Transport {

    private Socket socket;

    private DataOutputStream dos;
    private DataInputStream dis;

    public static Document doc;
    private static DocumentBuilder builder;

    private static final Logger log = Logger.getLogger(Transport.class);

    public TransportXML(Socket socket) throws IOException {

        this.socket = socket;

        dos = new DataOutputStream(this.socket.getOutputStream());
        dis = new DataInputStream(this.socket.getInputStream());

        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.newDocument();
        } catch (ParserConfigurationException e) {
            log.error("Error! Create new XML document!", e);
        }

    }

    public Document getDocument()  throws IOException {

        try {
            doc = builder.parse(new InputSource(new StringReader(dis.readUTF())));
            return doc;
        } catch (SAXException e) {
            log.error("Error! Get XML document (SAXException)!", e);
        }

        return null;

    }

    public Envelope getEnvelope(Document doc) {

        String root = doc.getDocumentElement().getNodeName();

        if (root.equals("MsgReg")) {
            return new MsgReg(doc);
        } else if (root.equals("MsgAuth")) {
            return new MsgAuth(doc);
        } else if (root.equals("Msg")) {
            return new Msg(doc);
        } else if (root.equals("MsgListUsers")) {
            return new MsgListUsers(doc);
        } else if (root.equals("MsgMove")) {
            return new MsgMove(doc);
        } else if (root.equals("MsgNicknameException")) {
            return new MsgNicknameException(doc);
        } else if (root.equals("MsgPlayRequest")) {
            return new MsgPlayRequest(doc);
        } else {
            return null;
        }
    }

    public Envelope getEnvelope(Serializable obj) {

        if (obj.getClass().getSimpleName().equals("MsgReg")) {
            return (MsgReg) obj;
        } else if (obj.getClass().getSimpleName().equals("MsgAuth")) {
                return (MsgAuth) obj;
        } else if (obj.getClass().getSimpleName().equals("Msg")) {
            return (Msg) obj;
        } else if (obj.equals("MsgListUsers")) {
            return new MsgListUsers(doc);
        } else if (obj.equals("MsgMove")) {
            return new MsgMove(doc);
        } else if (obj.equals("MsgNicknameException")) {
            return new MsgNicknameException(doc);
        } else if (obj.equals("MsgPlayRequest")) {
            return new MsgPlayRequest(doc);
        } else {
            return null;
        }

    }

    public Document newDocument() {

        try {
            doc = builder.parse(new InputSource(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><xml/>")));
            doc.removeChild(doc.getFirstChild());
        }catch (IOException e) {
            log.error("Error in method newDocument()! Get XML document (IOException)!", e);
        } catch (SAXException e) {
            log.error("Error in method newDocument()! Get XML document (SAXException)!", e);
            return newDocument();
        }

        return doc;

    }

    public static String documentToString(Document document) {

        try {
            Writer sw = new StringWriter();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            transformer.transform(new DOMSource(document), new StreamResult(sw));
            return sw.toString();
        } catch (TransformerFactoryConfigurationError e){
            log.error("Error in method documentToString() (TransformerFactoryConfigurationError)!", e);
        } catch (TransformerException e){
            log.error("Error in method documentToString() (TransformerException)!", e);
        }

        return "";

    }

    public Object receive() throws IOException{

        while (getDocument() == null){} // if reading error, read more

        String root = doc.getDocumentElement().getNodeName();
        log.info("Receive object! " + root + "(" + getSocket().getPort() + "=>" + getSocket().getLocalPort() + ")");

        Envelope envelope = getEnvelope(doc);
        if (envelope != null){
            return envelope;
        } else {
            log.error("Trying to receive unsupported type: - " + root);
            return null;
        }

    }

    public void send(Serializable obj) throws IOException {

        Envelope envelope = getEnvelope(obj);
        if (!envelope.equals(null)) {
            newDocument();
            sendToInternet(envelope.writeXml(doc), envelope.getLogMessage());
        }
        else {
            log.error(" Trying to send unsupported type: - "+obj.getClass().toString());
        }

    }

    private void sendToInternet(Document doc, String eMessage) throws IOException {

        try{
            String mToSend = documentToString(doc);
            dos.writeUTF(mToSend);
            log.info(socket.getLocalPort() + "=>" + socket.getPort() + ") " + eMessage);
        }catch (IOException e){
            throw new IOException(e);
        }

    }

    public Socket getSocket() {
        return socket;
    }

    public void close() throws IOException{

        try {
            dos.close();
        }
        finally {
            try {
                dis.close();
            }
            finally {
                socket.close();
            }
        }

    }

    public void closeSocket(){

        try {
            close();
        }
        catch (IOException e) {
            log.error("Error closing socket (IOException)!", e);
        }

    }

}
