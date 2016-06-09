package lab2.server.controller;

import lab2.protocol.Transport;
import lab2.protocol.TransportXML;
import lab2.protocol.User;
import lab2.protocol.envelope.*;
import lab2.server.model.Model;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

class Connection implements Runnable, Observer {

    private static final Logger log = Logger.getLogger(lab2.server.controller.Connection.class);

    private Socket socket;
    private Transport protocol;

    private Model userModel;

    private boolean newUser = true;
    private Thread threadConnection;
    private User thisUser;
    private Set<Connection> connections;

    public Connection(Socket socket, Model userModel, Set<Connection> connections) throws IOException {

        this.userModel = userModel;
        this.socket = socket;
        protocol = new TransportXML(socket);

        connections.add(this);
        this.connections = connections;

        threadConnection = new Thread(this);
        threadConnection.start();
    }

    public void run() {

        try {
            protocol.send(new Msg("Connect"));
        } catch (IOException e) {
            try {
                protocol.close();
            } catch (IOException ex) {
                log.error("Error exit! Closed socket", ex);
            }

            log.error("Error! Send 'Connect' (IOException).", e);

            connections.remove(this);
            userModel.observable().deleteObserver(this);
        }

        log.info("Send 'Connect' from socket: " + socket.getPort());

        try {
            while (true)
                dispatchEvent();
        } catch (SocketException e) {
            exitThread();
        } catch (IOException e) {
            exitThread();
        }

    }

    private void dispatchEvent() throws IOException {

        Object event = protocol.receive();

        if(event == null)
            return;

        if (event instanceof MsgReg) {
            MsgReg msgReg = (MsgReg) event;

            registration(new User(msgReg.getNickname(), msgReg.getPassword()));
        }

        if (event instanceof MsgAuth) {
            MsgAuth msgAuth = (MsgAuth) event;

            authorization(new User(msgAuth.getNickname(), msgAuth.getPassword()));
        }

        if (event instanceof Msg) {
            Msg msg = (Msg) event;

            if (msg.getMessage().equals("Exit")) {
                exitThread();
            } else if (msg.getMessage().equals("ExitGame")) {
                disconnectDuringGame(thisUser);
            } else if (msg.getMessage().equals("YesBattle")) {
                answerForRequest(new Msg("YesBattle"));
            } else if (msg.getMessage().equals("NoBattle")) {
                answerForRequest(new Msg("NoBattle"));
            }
        }


        if (event instanceof MsgPlayRequest){
            MsgPlayRequest playWith = (MsgPlayRequest) event;
            sendPlayRequest(playWith.getNickname());
        }



        if (event instanceof MsgMove){
            MsgMove msgMove = (MsgMove)event;
            setMessageInRoom(msgMove);
        }

    }

    private void registration(User user) {

        Set<User> listUsers = userModel.getUsersList();
        String nickname = user.getNickname();

        newUser = true;
        for (User tempUser : listUsers) {
            if (tempUser.getNickname().equals(nickname)) {
                newUser = false;

                try {
                    protocol.send(new MsgNicknameException("User with a nickname '" + nickname + "' has already been registred."));
                } catch (IOException ex) {
                    log.error("Error (IOException)! Send NickException (registration user).", ex);
                    connections.remove(this);
                    userModel.observable().deleteObserver(this);
                }

                log.info("User with a nickname '" + nickname + "' has already been registred.");
                break;
            }
        }

        if (newUser) {
            userModel.addUser(user, socket, false);
            thisUser = user;
            userModel.observable().addObserver(this);
            userModel.setListChanged();
        }

    }

    private void authorization(User user) {

        newUser = true;
        for (User tempUser : userModel.getUsersList()) {

            if (tempUser.equalsAuthorization(user) && tempUser.getUserStatus().equals(User.Status.Disconnected)) {
                newUser = false;

                userModel.addUser(tempUser, socket, true);
                thisUser = tempUser;
                userModel.observable().addObserver(this);
                userModel.setListChanged();

                log.info("User '" + user.toString() + "' is joined.");

                return;
            } else if (tempUser.equalsAuthorization(user) && (tempUser.getUserStatus().equals(User.Status.Expects) || tempUser.getUserStatus() == User.Status.Plays)) {
                newUser = false;

                try {
                    protocol.send(new MsgNicknameException("User is authorizated."));
                } catch (IOException ex) {
                    log.error("Error (IOException)! Send NickException (authorization user).", ex);
                    connections.remove(this);
                    userModel.observable().deleteObserver(this);
                }

                log.info("User with the nick '" + user.toString() + "' is authorizated.");

                return;
            }

        }

        if (newUser) {
            try {
                protocol.send(new MsgNicknameException("This User is not registred."));
            } catch (IOException ex) {
                log.error("Error (IOException)! Send NickException (authorization user).", ex);
                connections.remove(this);
                userModel.observable().deleteObserver(this);
            }

            log.info("User with the nick '" + user.toString() + "' is not registred.");
        }

    }

    public void exitThread() {
        
        try {
            Map<User, Socket> map = userModel.getMapActiveUsers();

            if (thisUser != null) {
                if (thisUser.getUserStatus().equals(User.Status.Plays))
                    disconnectDuringGame(thisUser);

                thisUser.setUserStatus(User.Status.Disconnected);
                map.remove(thisUser);

                log.info("User '" + thisUser.getNickname() + "' disconnected.");
            }

            protocol.close();
            connections.remove(this);
            userModel.observable().deleteObserver(this);

            if (!map.isEmpty())
                userModel.setListChanged();

            userModel.storageList();
        } catch (IOException ex) {
            log.error("Exit error (closed socket)", ex);
        }

    }

    private void disconnectDuringGame(User user) {

        long roomID = user.getRoomID();
        for (GameRoom room : StartServer.listRoom) {
            if (roomID == room.getID()) {
                try {
                    Transport transport = room.getConnect(user.getOpponent());
                    transport.send(new Msg("Opponent closed the game."));
                    log.info("Send Msg from " + socket.getPort() + " => " + transport.getSocket().getPort());
                } catch (IOException ex) {
                    log.error("Error! Send Msg (IOException).", ex);
                    exitThread();
                }

                room.close();
                StartServer.listRoom.remove(room);

                return;
            }
        }
    }

    public void closeAll() {
        
        Map<User, Socket> map = userModel.getMapActiveUsers();
        Set<Map.Entry<User, Socket>> entries = map.entrySet();

        for (Map.Entry<User, Socket> entry : entries) {
            entry.getKey().setUserStatus(User.Status.Disconnected);
            try {
                entry.getValue().close();
            } catch (IOException ex) {
                log.error("Error! Closing socket.", ex);
            }

            userModel.storageList();
        }

    }
    
    public void update(Observable o, Object arg) {

        if(thisUser.getUserStatus().equals(User.Status.Plays))
            return;

        try{
            protocol.send(new MsgListUsers(listWithoutMe(thisUser)));
        } catch(IOException ex){
            log.error("Error! Update Send 'ListUsers' (IOException).", ex);
            exitThread();
        }

        log.info("Update from socket : " + socket.getLocalPort() + "=>" + socket.getPort());

    }

    private Set<User> listWithoutMe(User user) {

        Set<User> withoutMe = new HashSet<>();
        Set<User> list = userModel.getExpectingUsersList();
        for (User tempUser : list) {
            if (!tempUser.equalsAuthorization(user))
                withoutMe.add(tempUser);
        }

        return withoutMe;
    }

    private Transport findProtocol(User user){

        Transport transport = null;
        Socket socket = null;
        Map<User, Socket> map = userModel.getMapActiveUsers();

        if (map.containsKey(user))
            socket = map.get(user);

        for(Connection connection : connections) {
            if(socket.equals(connection.getProtocol().getSocket())) {
                log.info("Socket - connection :" + socket.getPort() + " - " + connection.getProtocol().getSocket().getPort());
                transport = connection.getProtocol();
                break;
            }
        }

        return transport;
    }

    private User getUserByNick(String nickname){

        User user = null;
        Set<User> list = userModel.getKeySetActiveUsers();
        for(User tempUser : list) {
            if(nickname.equals(tempUser.getNickname())) {
                user = tempUser;
                break;
            }
        }

        return user;

    }

    private void answerForRequest(Serializable obj) {
        
        String sOpponent = thisUser.getOpponent();
        User opponentUser = getUserByNick(sOpponent);
        Transport opponentProtocol = findProtocol(opponentUser);

        if (obj instanceof Msg && ((Msg) obj).getMessage().equals("YesBattle")) {
            GameRoom game = new GameRoom(thisUser, protocol, opponentUser, opponentProtocol, userModel, connections);

            StartServer.listRoom.add(game);

            goToGame(thisUser, game.getID());
            goToGame(opponentUser, game.getID());
        } else {
            noGame(thisUser);
            noGame(opponentUser);

            try {
                opponentProtocol.send(obj);
                log.info("Send " + obj.getClass() + " " + socket.getLocalPort() + "=>" + socket.getPort());
            } catch (IOException ex) {
                log.error("Error! Send " + obj.getClass() + "(IOException", ex);
                exitThread();
            }
        }

        userModel.setListChanged();

    }

    private void goToGame(User user, long roomID) {
        user.setRoomID(roomID);
        user.setUserStatus(User.Status.Plays);
    }

    private void noGame(User user) {
        user.setRoomID(0);
        user.setOpponent(" ");
    }

    private void sendPlayRequest(String nick) {
        
        User opponent = getUserByNick(nick);
        if(!opponent.getOpponent().equals(" ")){
            try {
                protocol.send(new MsgListUsers(listWithoutMe(thisUser)));
            } catch (IOException ex) {
                log.error("Error! Send ListUsers (IOException)",ex);
                exitThread();
            }
            return;
        }

        opponent.setOpponent(thisUser.getNickname());
        thisUser.setOpponent(opponent.getNickname());

        try {
            findProtocol(opponent).send(new MsgPlayRequest(thisUser.getNickname()));
        } catch (IOException ex) {
            log.error("Error! Send PlayRequest (IOException).", ex);
            exitThread();
        }

        log.info("Request for user [" + opponent.getNickname() + "]");
    }

    private void setMessageInRoom(MsgMove msgMove) {
        String nick = msgMove.getPlayer();
        User us = getUserByNick(nick);
        long roomID = us.getRoomID();
        for (GameRoom room : StartServer.listRoom) {
            if (roomID == room.getID()) {
                room.setMoveMessage(msgMove);
            }
        }
    }

    private Transport getProtocol() {
        return protocol;
    }
    
}
