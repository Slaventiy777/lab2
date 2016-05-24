package lab2.protocol.envelope;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MsgPlayRequest extends Envelope {

    private String nickname;

    public MsgPlayRequest(String nickname) {
        this.nickname = nickname;
    }

    public MsgPlayRequest(Document doc) {
        readXml(doc);
    }

    public void readXml(Document doc){
        nickname = GetData(doc,"nick");
    }

    public Document writeXml(Document doc){
        Element root = doc.createElement("MsgReg");
        doc.appendChild(root);
        writeDataXml(doc, root, "nickname", nickname);

        return doc;
    }

    public String getLogMessage() {
        return " Send PlayRequest to - " + nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
