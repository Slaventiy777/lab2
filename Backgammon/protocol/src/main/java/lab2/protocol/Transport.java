package lab2.protocol;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public interface Transport {

    public Object receive() throws IOException;
    public void send(Serializable obj) throws IOException;
    public void close() throws IOException;
    public Socket getSocket();

}
