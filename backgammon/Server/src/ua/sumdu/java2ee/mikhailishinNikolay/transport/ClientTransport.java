package ua.sumdu.java2ee.mikhailishinNikolay.transport;

import java.io.*;
import java.net.Socket;

public class ClientTransport extends Transport {
    public ClientTransport(Socket socket) throws IOException {
        super(socket);
    }

    public void send() throws IOException {
        File f = new File("example.xml");

        output.writeLong(f.length());
        output.writeUTF(f.getName());

        FileInputStream in = new FileInputStream(f);
        byte[] buffer = new byte[64*1024];
        int count;

        while((count = in.read(buffer)) != -1){
            output.write(buffer, 0, count);
        }
        output.flush();
        output.close();
    }
}
