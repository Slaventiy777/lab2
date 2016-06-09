package lab2.protocol.envelope;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MsgMove extends Envelope {

    private String player;
    private int firstCheckerMoveFrom;
    private int secondCheckerMoveFrom;
    private int firstCheckerMoveInto;
    private int secondCheckerMoveInto;

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getFirstCheckerMoveFrom() {
        return firstCheckerMoveFrom;
    }

    public void setFirstCheckerMoveFrom(int firstCheckerMoveFrom) {
        this.firstCheckerMoveFrom = firstCheckerMoveFrom;
    }

    public int getSecondCheckerMoveFrom() {
        return secondCheckerMoveFrom;
    }

    public void setSecondCheckerMoveFrom(int secondCheckerMoveFrom) {
        this.secondCheckerMoveFrom = secondCheckerMoveFrom;
    }

    public int getFirstCheckerMoveInto() {
        return firstCheckerMoveInto;
    }

    public void setFirstCheckerMoveInto(int firstCheckerMoveInto) {
        this.firstCheckerMoveInto = firstCheckerMoveInto;
    }

    public int getSecondCheckerMoveInto() {
        return secondCheckerMoveInto;
    }

    public void setSecondCheckerMoveInto(int secondCheckerMoveInto) {
        this.secondCheckerMoveInto = secondCheckerMoveInto;
    }



    private String message;

    public MsgMove(String message) {
        this.message = message;
    }

    public MsgMove(Document doc) {
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
