package lab2.protocol.envelope;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MsgAuth extends Envelope {

    private String nickname;
    private String password;

    public MsgAuth(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public MsgAuth(Document doc) {
        readXml(doc);
    }

    public void readXml(Document doc){
        nickname    = GetData(doc, "nickname");
        password    = GetData(doc, "password");
    }

    public Document writeXml(Document doc){
        Element root = doc.createElement("MsgAuth");
        doc.appendChild(root);
        writeDataXml(doc, root, "nickname", nickname);
        writeDataXml(doc, root, "password", password);

        return doc;
    }

    public String getLogMessage() {
        return " Send message authorization (" + this.toString() + ")";
    }

    public String toString() {
        return "Nick: [" + this.nickname + "] Password: [" + this.password + "]";
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
