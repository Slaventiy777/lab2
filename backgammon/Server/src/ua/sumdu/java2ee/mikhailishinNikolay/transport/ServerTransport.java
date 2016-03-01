package ua.sumdu.java2ee.mikhailishinNikolay.transport;

import java.io.IOException;
import java.net.Socket;

public class ServerTransport extends Transport {
    public ServerTransport(Socket socket) throws IOException {
        super(socket);
        startEventLoop();
    }
}
