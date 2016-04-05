package ua.sumdu.java2ee.mikhailishinNikolay.transport;


import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.net.Socket;

public abstract class Transport {
    protected Socket socket;
    protected ObjectInputStream input;
    protected ObjectOutputStream output;

    protected abstract void receive() throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException;

    protected final Thread eventLoop = new Thread()
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
    };

    public Transport(Socket socket) throws IOException
    {
        this.socket = socket;
        // so reading will interrupt each 1/2 second
        socket.setSoTimeout(500);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
    }

    protected void startEventLoop()
    {
        eventLoop.start();
    }

    public void send(Serializable obj) throws IOException {

        output.writeObject(obj);
        output.flush();
    }

}
