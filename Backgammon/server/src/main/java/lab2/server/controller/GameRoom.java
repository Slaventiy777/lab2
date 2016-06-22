package lab2.server.controller;

import lab2.protocol.Transport;
import lab2.protocol.User;
import lab2.protocol.envelope.Msg;
import lab2.protocol.envelope.MsgMove;
import lab2.protocol.model.BackgammonModel;
import lab2.server.model.PlayerModel;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Set;

public class GameRoom implements Runnable {

    private static final Logger log = Logger.getLogger(lab2.server.controller.Connection.class);

    User player1, player2;
    Transport protocol1, protocol2;

    Set<Connection> connections;
    BackgammonModel backgammonModel;

    private PlayerModel playerModel;
    private Thread thread;

    private MsgMove msgMove = null;


    public GameRoom(User player1, Transport protocol1, User player2, Transport protocol2, PlayerModel playerModel, Set<Connection> connections) {
        this.playerModel = playerModel;
        this.player1 = player1;
        this.player2 = player2;
        this.protocol1 = protocol1;
        this.protocol2 = protocol2;
        this.connections = connections;

        backgammonModel = new BackgammonModel(player1, player2);
        thread = new Thread(this);
        log.info(" Room is created. ");
        thread.start();
    }

    public void run() {

        while (!thread.isInterrupted()) {
            dispatchEvent();
        }

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

        if (msgMove == null) {
            return;
        }

        log.info(" Recieve msgMove ");
        String playerCurrent;
        String nickname = msgMove.getPlayer();
        String opponentNickname;
        if (nickname.equals(player1.getNickname())) {
            playerCurrent = nickname;
            opponentNickname = player2.getNickname();
            sendMoveOpponent(protocol2, opponentNickname, msgMove);
        } else {
            playerCurrent = nickname;
            opponentNickname = player1.getNickname();
            sendMoveOpponent(protocol1, opponentNickname, msgMove);
        }

        backgammonModel.afterDidMove(playerCurrent, msgMove.getFirstCheckerMoveFrom(), msgMove.getFirstCheckerMoveInto(),
                                        msgMove.getSecondCheckerMoveFrom(), msgMove.getSecondCheckerMoveInto());

        if (backgammonModel.isEndGame()) {
            endGame();
            close();
        }

        msgMove = null;

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

    public void setMoveMessage(MsgMove msgMove) {
        this.msgMove = msgMove;
        log.info(" Receive msgMove.");
    }

    public void sendMoveOpponent(Transport protocol, String opponentNickname, MsgMove msgMove) {
        try {
            protocol.send(msgMove);
        } catch (IOException ex) {
            log.error("IOE when Send MsgMove for [" + opponentNickname + "]", ex);
            getConnection(protocol).exitThread();
        }
    }

    public void close() {
        disconnectUser(player1);
        disconnectUser(player2);
        log.info("The Room is closing. ");
        playerModel.setListChanged();
        thread.interrupt();
    }

    private void endGame() {

        if (backgammonModel.getNumCheckerInGameFirstUser() == 0) {

            try {
                protocol1.send(new Msg("Won"));
            } catch (IOException ex) {
                log.error("IOE when Send Won for [" + player1.getNickname() + "]", ex);
                getConnection(protocol1).exitThread();
            }

            try {
                protocol2.send(new Msg("Lost"));
            } catch (IOException ex) {
                log.error("IOE when Send Lost for [" + player2.getNickname() + "]", ex);
                getConnection(protocol2).exitThread();
            }

            player1.setRank(player1.getRank() + 1);
            log.info(" The User [" + player2.getNickname() + "] is Won");

        } else {


            try {
                protocol1.send(new Msg("Lost"));
            } catch (IOException ex) {
                log.error("IOE when Send Lost for [" + player1.getNickname() + "]", ex);
                getConnection(protocol1).exitThread();
            }

            try {
                protocol2.send(new Msg("Won"));
            } catch (IOException ex) {
                log.error("IOE when Send Won for [" + player2.getNickname() + "]", ex);
                getConnection(protocol2).exitThread();
            }

            player2.setRank(player2.getRank() + 1);
            log.info(" The User [" + player2.getNickname() + "] is Won");

        }

    }

}
