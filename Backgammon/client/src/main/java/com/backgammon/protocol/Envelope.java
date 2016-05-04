
package com.backgammon.protocol;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.Serializable;

public abstract class Envelope implements Serializable {

	public abstract String getLogMessage();
	public abstract Document writeXml(Document doc);
	public abstract Envelope readXml(Document doc);

	/**
     * method for pack any text data into incoming Document
     * @param doc - incoming document
     * @param root - root element of incoming document to write data
     * @param teg - tag name for xml
     * @param data - text to write
     * @return element of Document with data packed in
     */  
	public Element writeDataXml(Document doc, Element root, String teg, String data){
		Element El = doc.createElement(teg);
		El.appendChild(doc.createTextNode(data));
        root.appendChild(El);
        return El;
	}
	
	/**
	 * 
     * method for unpack any text data from incoming Document
     * (can be used for unique tag)
     * @param doc - incoming document
     * @param teg - tag name from xml
     * @return text data unpacked from incoming document
     */
	public String GetData(Document doc, String teg){ 		
		return (doc.getElementsByTagName(teg).item(0).getFirstChild().getNodeValue());
	}
	
	/**
	 * 
     * method for unpack any text data from incoming Document
     * (can be used for unique tag)
     * @param doc - incoming document
     * @param root - root element of incoming document
     * @param teg - tag name from xml
     * @return text data unpacked from incoming document
     */
	public String GetData(Document doc, Element root, String teg){ 		
		return (root.getFirstChild().getNodeValue());
	}
		
}
