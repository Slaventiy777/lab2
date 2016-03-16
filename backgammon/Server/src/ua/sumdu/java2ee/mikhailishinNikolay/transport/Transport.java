package ua.sumdu.java2ee.mikhailishinNikolay.transport;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.net.Socket;
import java.util.*;

public abstract class Transport {
    protected Socket socket;
    protected DataInputStream input;
    protected DataOutputStream output;

    protected final Thread eventLoop = new Thread()
    {
        public void run()
        {
            while (! interrupted()) {
                try {
                    File file = receive();
                } catch (IOException e) {
                    //e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    //e.printStackTrace();
                } catch (SAXException e) {
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
        output = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(socket.getInputStream());
    }

    protected void startEventLoop()
    {
        eventLoop.start();
    }

    abstract protected File receive() throws IOException, ParserConfigurationException, SAXException;

    public void send(File file) throws IOException {
        output.writeLong(file.length());
        output.writeUTF(file.getName());

        FileInputStream in = new FileInputStream(file);
        byte[] buffer = new byte[64*1024];
        int count;

        while((count = in.read(buffer)) != -1){
            output.write(buffer, 0, count);
        }
        output.flush();
        output.close();
    }

}
