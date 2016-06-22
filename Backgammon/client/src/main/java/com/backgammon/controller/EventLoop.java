package com.backgammon.controller;

import com.backgammon.model.Settings;
import lab2.protocol.envelope.*;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Receives events from the server
 */
public class EventLoop extends Thread {

    static Logger log = Logger.getLogger(EventLoop.class.getName());

    private BackgammonController listener;

    public EventLoop(BackgammonController listener) {

        log.debug(Settings.DBG_CreatingEventLoop);
        this.listener = listener;

    }

    public void run() {

        log.debug(Settings.DBG_StartEventLoop);

        while (! interrupted()) {
            try {
                dispatchEvent(listener.getTransport().receive());
            } catch (IOException e) {
                BackgammomBoardController.showError(Settings.ERR_ConnectionReset);
                interrupt();
            }
        }

    }

    /**
     * Dispatches events and reports the controller what to do
     * @param event event from server
     */
    protected void dispatchEvent(Object event) {

        if (event == null) {
            log.debug(Settings.DBG_NULL_EVENT);
            return;
        }


        if (event.getClass().getSimpleName().equals("MsgListUsers")) {
            MsgListUsers msgListUsers = (MsgListUsers) event;
            if (msgListUsers.getUsers() != null) {
                listener.updatePlayerSet(msgListUsers.getUsers());
            }
        }

        if (event.getClass().getSimpleName().equals("MsgNicknameException")) {
            MsgNicknameException msgNicknameException = (MsgNicknameException) event;
            listener.showError(msgNicknameException.getMessage());

            listener.showAuthorizationWindow();
        }

        if (event.getClass().getSimpleName().equals("MsgPlayRequest")) {
            MsgPlayRequest playRequest = (MsgPlayRequest) event;
            listener.showConfirm(Settings.ANS_PlayWith + playRequest.getNickname() + "?", playRequest.getNickname());
        }

        if (event.getClass().getSimpleName().equals("MsgMove")) {
            MsgMove msgMove = (MsgMove) event;
            listener.updateBackgammonModel(msgMove.getPlayer(), msgMove.getFirstCheckerMoveFrom(), msgMove.getFirstCheckerMoveInto(),
                    msgMove.getSecondCheckerMoveFrom(), msgMove.getSecondCheckerMoveInto());
        }

        if (event.getClass().getSimpleName().equals("Msg")) {
            Msg msg = (Msg) event;

            if (msg.getMessage().equals("Connect")) {
                listener.showAuthorizationWindow();
            } else if (msg.getMessage().equals("NoGame")) {
                listener.showError(Settings.ERR_PlayerRefused);
            } else if (msg.getMessage().equals("Lost")) {
                listener.lostAction();
            } else if (msg.getMessage().equals("Won")) {
                listener.wonAction();
            } else {
                listener.showInfoMessage(msg.getMessage());
                listener.closeGameWindow();
                listener.showPlayerList();
            }

        }

    }

}
