package com.backgammon.protocol;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Msg  extends Envelope {

    public String message;
    
    /**
     * 
     * empty constructor, used only for FactoryMethod
     */ 
    public Msg() {}

    public Msg(String message) {
        this.message = message;
    }
    
    public String getLogMessage(){
    	return " Send Msg '" + message + "'";
    }
    
    /**
     * method for pack Message into incoming Document
     * @param doc - incoming document
     * @return Document with Message packed in
     */
    public Document writeXml(Document doc){
		Element root = doc.createElement("Msg");
    	doc.appendChild(root);
    	writeDataXml(doc, root, "Message", message);
    	return doc;
	}
 
    /**
	 * 
     * method for unpack Message from incoming Document
     * @param doc - incoming document
     * @return Message unpacked from incoming document
     */
    public Msg readXml(Document doc){
    	return (new Msg(GetData(doc,"Message"))); //з	
    }
 
}
