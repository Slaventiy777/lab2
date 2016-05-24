package lab2.protocol.envelope;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MsgReg extends Envelope {

    private String nickname;
    private String password;

    public MsgReg(Document doc) {
        readXml(doc);
    }

    public void readXml(Document doc){
        nickname    = GetData(doc,"nickname");
        password    = GetData(doc,"password");
    }

    public Document writeXml(Document doc){
        Element root = doc.createElement("MsgReg");
        doc.appendChild(root);
        writeDataXml(doc, root, "nickname", nickname);
        writeDataXml(doc, root, "password", password);

        return doc;
    }

    public String getLogMessage() {
        return " Send message regestration (" + this.toString() + ")";
    }

    public String toString() {
        return "Nickname: [" + this.nickname + "] Password: [" + this.password + "]";
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nick) {
        this.nickname = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
