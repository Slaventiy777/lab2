package com.transport;


import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.net.Socket;

public abstract class Transport {
    public static enum ErrorType {REGISTRATION, CONNECTION, OTHER};
    public static enum StatusType {REGISTRATION, CONNECTION, OTHER};

    protected Socket socket;
    protected ObjectInputStream input;
    protected ObjectOutputStream output;

    public abstract Object receive() throws IOException, ClassNotFoundException;

    /*protected final Thread eventLoop = new Thread()
    {
        public void run()
        {
            while (! interrupted()) {
                try {
                    receive();
                } catch (IOException e) {
                    //Исправлю потом
                    //e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    //Исправлю потом
                    //e.printStackTrace();
                } catch (SAXException e) {
                    //Исправлю потом
                    //e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    //Исправлю потом
                    //e.printStackTrace();
                }
            }
        }
    };*/

    public Transport(Socket socket) throws IOException {
        this.socket = socket;

        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
    }

    /*protected void startEventLoop()
    {
        eventLoop.start();
    }*/

    public void send(Serializable obj) throws IOException {
        output.writeObject(obj);
        output.flush();
    }

}
