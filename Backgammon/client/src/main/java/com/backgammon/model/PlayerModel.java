package com.backgammon.model;

import lab2.protocol.User;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

public class PlayerModel extends Observable {

    private static Logger log = Logger.getLogger(PlayerModel.class.getName());

    private Set<User> playerSet = new HashSet<User>();
    private String myNickname;

    public PlayerModel(Set<User> userSet, String myNickname) {
        this.myNickname = myNickname;
        updatePlayerSet(userSet);
    }

    public void updatePlayerSet(Set<User> userSet) {
        this.playerSet = userSet;
        setChangesAndNotify();
    }

    public Set<User> getPlayerSet() {
        return playerSet;
    }

    public User getPlayer(String nickname) {

        User playerTemp = null;
        for (User player : playerSet) {
            if (player.getNickname().equals(nickname)) {
                playerTemp = player;
                break;
            }
        }

        return playerTemp;

    }

    public String getMyNickname() {
        return myNickname;
    }

    public Observable observable() {
        return this;
    }

    public void setChangesAndNotify() {
        log.debug(Settings.DBG_SetChanges);

        super.setChanged();
        super.notifyObservers();
    }

}
