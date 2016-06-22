package lab2.protocol.envelope;


import lab2.protocol.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashSet;
import java.util.Set;

public class MsgListUsers extends Envelope {

    private Set<User> users;

    public MsgListUsers(Set<User> users) {
        this.users = users;
    }

    public MsgListUsers(Document doc) {
        readXml(doc);
    }

    public void readXml(Document doc){

        users = new HashSet<>();

        User user;
        String nickname;
        String password;
        int rank;
        User.Status userStatus;
        String opponent;
        long roomID;

        NodeList nodeList = doc.getElementsByTagName("User");
        for(int i = 0; i < nodeList.getLength(); i++) {
            Element el = (Element)nodeList.item(i);

            nickname = el.getElementsByTagName("nickname").item(0).getFirstChild().getNodeValue();
            password = el.getElementsByTagName("password").item(0).getFirstChild().getNodeValue();
            rank = Integer.parseInt(el.getElementsByTagName("rank").item(0).getFirstChild().getNodeValue());
            userStatus = User.Status.valueOf(el.getElementsByTagName("status").item(0).getFirstChild().getNodeValue());
            opponent = el.getElementsByTagName("opponent").item(0).getFirstChild().getNodeValue();
            roomID = Long.parseLong(el.getElementsByTagName("roomID").item(0).getFirstChild().getNodeValue());

            user = new User(nickname, password);
            user.setRank(rank);
            user.setUserStatus(userStatus);
            user.setOpponent(opponent);
            user.setRoomID(roomID);

            users.add(user);
        }

    }

    public Document writeXml(Document doc){

        Element root = doc.createElement("ListUsers");
        doc.appendChild(root);
        for (User user: users){
            writeUserXml(doc, root, user);
        }

        return doc;

    }

    public Document writeUserXml(Document doc, Element root, User user){

        Element El = doc.createElement("User");

        root.appendChild(El);

        writeDataXml(doc, El, "nickname", user.getNickname());
        writeDataXml(doc, El, "password", user.getPassword());
        writeDataXml(doc, El, "rang", Integer.toString(user.getRank()));
        writeDataXml(doc, El, "state", ""+(user.getUserStatus()));
        writeDataXml(doc, El, "playWith", user.getOpponent());
        writeDataXml(doc, El, "roomID", Long.toString(user.getRoomID()));

        return doc;

    }

    public String getLogMessage() {
        return " Send 'Users list'";
    }


    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}
