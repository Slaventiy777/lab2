package ua.sumdu.java2ee.mikhailishinNikolay.transport;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

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

    protected File receive() throws IOException {
        long fileSize = input.readLong();
        String fileName = input.readUTF();
        System.out.println("File name: " + fileName);
        System.out.println("File size: " + fileSize + " byte\n");

        byte[] buffer = new byte[64*1024];
        File newFile = new File(fileName);
        FileOutputStream outF = new FileOutputStream(newFile);
        int count, total = 0;

        while ((count = input.read(buffer)) != -1){
            total += count;
            outF.write(buffer, 0, count);

            if(total == fileSize){
                break;
            }
        }
        outF.flush();
        outF.close();

        System.out.println("File \""+fileName+"\" has been received");

        return newFile;
    }
}
