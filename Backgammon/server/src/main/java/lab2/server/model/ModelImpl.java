package lab2.server.model;

import lab2.protocol.User;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.Serializable;
import java.net.Socket;
import java.util.*;

import static lab2.server.model.ParserXmlFile.readFromXML;
import static lab2.server.model.ParserXmlFile.writeToXML;

public class ModelImpl extends Observable implements Model, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(lab2.server.model.ModelImpl.class);

    private final static String FILE_NAME = "usersList.xml";

    private File file;
    private Set<User> users = new HashSet<>();
    private Map<User, Socket> activeUsers = new HashMap<>();


    public ModelImpl() {
        initModel();
    }

    public void initModel() {
        file = new File(FILE_NAME);
        setList(loadList());
    }

    public void setList(Set<User> users) {
        this.users = users;
    }

    public Set<User> loadList() {

        try {
            if (file.exists()) {
                readFromXML(users, file);
                log.info("File is read completely.");
            }
        } catch (ParserConfigurationException ex) {
            log.error(" ParserConfigurationException ", ex);
        } catch (SAXException ex) {
            log.error(" SAXException ", ex);
        } catch (Exception ex) {
            log.error("Error! Can't load users list. " + ex.getMessage(), ex);
            this.setChanged();
            this.notifyObservers("Error! Can not load users list.");
        }

        for(User user :users){
            System.out.println(user.toString());
        }

        return users;

    }

    public void setListChanged() {
        this.setChanged();
        this.notifyObservers();
    }

    public void storageList() {
        try {
            writeToXML(users, file);
        } catch (Exception ex) {
           log.error("Error! Can't save users list in file. " + ex.getMessage());
           this.setChanged();
           this.notifyObservers("Error! Can't save users list in file.");
        }
    }


    public synchronized void addUser(User user, Socket socket, boolean onlyActiveUser) {
        user.setUserStatus(User.Status.Expects);

        activeUsers.put(user, socket);

        if(!onlyActiveUser) {
            users.add(user);
            log.info( " Added user - " + user.getNickname() + " (socket " + socket.getPort() + ")");
        }
    }

    public synchronized void removeActiveUser(User user) {
        Iterator iter = activeUsers.keySet().iterator();
        while (iter.hasNext()) {
            if (!iter.equals(user)) {
                iter.remove();
            }
        }
    }


    public Set<User> getUsersList() {
        return users;
    }
    
    public Set<User> getExpectingUsersList() {
        Set<User> usersExpecting  = new HashSet<>();

        for(User user : activeUsers.keySet()) {
            if(user.getUserStatus().equals(User.Status.Expects)) {
                usersExpecting.add(user);
            }
        }

        return usersExpecting;
    }

    public Map<User, Socket> getMapActiveUsers() {
        return activeUsers;
    }

    public Set<User> getKeySetActiveUsers() {
        return activeUsers.keySet();
    }
    
    public Set<Socket> getValuesSetActiveUsers() {
        return (Set<Socket>)activeUsers.values();
    }


    public Observable observable() {
        return this;
    }

}
