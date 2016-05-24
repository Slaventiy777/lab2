package lab2.protocol.envelope;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.Serializable;

public abstract class Envelope implements Serializable {

    private static final long serialVersionUID = 1L;

    public abstract Document writeXml(Document doc);
    public abstract void readXml(Document doc);
    public abstract String getLogMessage();

    public Element writeDataXml(Document doc, Element root, String teg, String data) {
        Element E1 = doc.createElement(teg);
        E1.appendChild(doc.createTextNode(data));
        root.appendChild(E1);
        return E1;
    }

    public String GetData(Document doc, String teg) {
        return doc.getElementsByTagName(teg).item(0).getFirstChild().getNodeValue();
    }

}
