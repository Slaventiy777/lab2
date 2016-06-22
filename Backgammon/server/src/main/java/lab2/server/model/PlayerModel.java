package lab2.server.model;

import lab2.protocol.User;

import java.net.Socket;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

public interface PlayerModel {
    
    void addUser(User user, Socket socket, boolean onlyActiveUser);

    void removeActiveUser(User user);
    
    void setListChanged();
    
    void setList(Set<User> users);
    
    void storageList();
    
    Set<User> loadList();
    
    Set<User> getUsersList();
    

    Set<User> getKeySetActiveUsers();
    
    Set<Socket> getValuesSetActiveUsers();
    
    Set<User> getExpectingUsersList();

    Map<User, Socket> getMapActiveUsers();


    Observable observable();
    
}
