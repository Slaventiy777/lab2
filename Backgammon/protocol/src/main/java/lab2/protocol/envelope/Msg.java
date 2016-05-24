package lab2.protocol.envelope;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Msg extends Envelope {

    private String message;

    public Msg(String message) {
        this.message = message;
    }

    public Msg(Document doc) {
        readXml(doc);
    }

    public void readXml(Document doc){
        message = GetData(doc, "Message");
    }

    public Document writeXml(Document doc){
        Element root = doc.createElement("Msg");
        doc.appendChild(root);
        writeDataXml(doc, root, "Message", message);
        return doc;
    }

    public String getLogMessage(){
        return " Send Msg '" + message + "'";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
