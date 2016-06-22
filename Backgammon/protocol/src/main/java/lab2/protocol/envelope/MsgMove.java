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

    public MsgMove(String player, int firstCheckerMoveFrom, int secondCheckerMoveFrom, int firstCheckerMoveInto, int secondCheckerMoveInto) {
        this.player = player;
        this.firstCheckerMoveFrom = firstCheckerMoveFrom;
        this.secondCheckerMoveFrom = secondCheckerMoveFrom;
        this.firstCheckerMoveInto = firstCheckerMoveInto;
        this.secondCheckerMoveInto = secondCheckerMoveInto;
    }

    public MsgMove(Document doc) {
        readXml(doc);
    }

    public void readXml(Document doc){
        player = GetData(doc, "player");
        firstCheckerMoveFrom = Integer.parseInt(GetData(doc, "firstCheckerMoveFrom"));
        secondCheckerMoveFrom = Integer.parseInt(GetData(doc, "secondCheckerMoveFrom"));
        firstCheckerMoveInto = Integer.parseInt(GetData(doc, "firstCheckerMoveInto"));
        secondCheckerMoveInto = Integer.parseInt(GetData(doc, "secondCheckerMoveInto"));
    }

    public Document writeXml(Document doc){
        Element root = doc.createElement("MsgMove");
        doc.appendChild(root);
        writeDataXml(doc, root, "player", player);
        writeDataXml(doc, root, "firstCheckerMoveFrom", String.valueOf(firstCheckerMoveFrom));
        writeDataXml(doc, root, "secondCheckerMoveFrom", String.valueOf(secondCheckerMoveFrom));
        writeDataXml(doc, root, "firstCheckerMoveInto", String.valueOf(firstCheckerMoveInto));
        writeDataXml(doc, root, "secondCheckerMoveInto", String.valueOf(secondCheckerMoveInto));

        return doc;
    }

    public String getLogMessage(){
        return " Send MsgMove '" + this.toString() + "'";
    }

    public String toString() {
        return "player: [" + this.player + "] firstCheckerMoveFrom: [" + this.firstCheckerMoveFrom
                + "] secondCheckerMoveFrom: [" + this.secondCheckerMoveFrom + "] firstCheckerMoveInto: ["
                + this.firstCheckerMoveInto + "] secondCheckerMoveInto: [" + this.secondCheckerMoveInto + "]";
    }

}
