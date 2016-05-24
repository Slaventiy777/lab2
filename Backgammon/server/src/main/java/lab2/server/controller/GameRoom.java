package lab2.server.controller;

import lab2.protocol.Transport;
import lab2.protocol.User;
import lab2.protocol.envelope.Msg;
import lab2.server.model.Model;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Set;

public class GameRoom implements Runnable {

    private static final Logger log = Logger.getLogger(lab2.server.controller.Connection.class);

    User player1, player2;
    Transport protocol1, protocol2;
    Set<Connection> connections;
    private Model userModel;
    private Thread thread;

    public GameRoom(User player1, Transport protocol1, User player2, Transport protocol2, Model userModel, Set<Connection> connections) {
        this.userModel = userModel;
        this.player1 = player1;
        this.player2 = player2;
        this.protocol1 = protocol1;
        this.protocol2 = protocol2;
        this.connections = connections;

        thread = new Thread(this);
        log.info(" Room is created. ");
        thread.start();
    }

    public void run() {

    }
    
    public Transport getConnect(String nickname) {
        Transport transport;

        if (nickname.equals(player1.getNickname())) {
            transport = protocol1;
        } else {
            transport = protocol2;
        }

        return transport;
    }

    private void dispatchEvent() {


    }

    private Connection getConnection(Transport protocol) {
        Connection connect = null;

        for (Connection connection : connections) {
            if (connection.equals(protocol)) {
                connect = connection;
                break;
            }
        }

        return connect;
    }

    private void disconnectUser(User user) {
        user.setOpponent(" ");
        user.setRoomID(0);
        user.setUserStatus(User.Status.Expects);

        log.info(" User '" + user.getNickname() + "' status 'Expects' = " + user.getUserStatus());
    }

    public long getID() {
        return thread.getId();
    }

    public void close() {
        disconnectUser(player1);
        disconnectUser(player2);
        log.info("The Room is closing. ");
        userModel.setListChanged();
        thread.interrupt();
    }

    private void endGame() {

    }

}
