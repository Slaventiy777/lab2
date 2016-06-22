package com.backgammon.model;

import lab2.protocol.model.BackgammonModel;
import org.apache.log4j.Logger;

import java.util.Observable;

public class GameModel extends Observable {

    private static Logger log = Logger.getLogger(GameModel.class.getName());

    private BackgammonModel backgammonModel;
    private String playerMe;
    private String playerOpponent;

    public GameModel(BackgammonModel backgammonModel, String playerMe, String playerOpponent) {

        log.debug("Creating GameModel. Player me - " + playerMe + " opponent - " + playerOpponent);

        this.backgammonModel = backgammonModel;
        this.playerMe = playerMe;
        this.playerOpponent = playerOpponent;

    }

    public void updateBackgammonModel(String nickname, int firstCheckerMoveFrom, int firstCheckerMoveInto,
                                      int secondCheckerMoveFrom,  int secondCheckerMoveInto) {

        backgammonModel.afterDidMove(nickname, firstCheckerMoveFrom, firstCheckerMoveInto, secondCheckerMoveFrom, secondCheckerMoveInto);
        setChangesAndNotify();

    }

    public BackgammonModel getBackgammonModel() {
        return backgammonModel;
    }

    public void setBackgammonModel(BackgammonModel backgammonModel) {
        this.backgammonModel = backgammonModel;
    }

    public String getPlayerMe() {
        return playerMe;
    }

    public void setPlayerMe(String playerMe) {
        this.playerMe = playerMe;
    }

    public String getPlayerOpponent() {
        return playerOpponent;
    }

    public void setPlayerOpponent(String playerOpponent) {
        this.playerOpponent = playerOpponent;
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
