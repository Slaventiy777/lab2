package lab2.protocol.envelope;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MsgNicknameException extends Envelope {

    private String message;

    public MsgNicknameException(String message) {
        this.message = message;
    }

    public MsgNicknameException(Document doc) {
        readXml(doc);
    }

    public void readXml(Document doc){
        message = GetData(doc, "Message");
    }

    public Document writeXml(Document doc){
        Element root = doc.createElement("MsgNicknameException");
        doc.appendChild(root);
        writeDataXml(doc, root, "Message", message);
        return doc;
    }

    public String getLogMessage(){
        return " Send MsgNicknameException '" + message + "'";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
