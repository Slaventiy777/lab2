package com.backgammon.controller;

import com.backgammon.model.GameModel;
import com.backgammon.model.PlayerModel;
import com.backgammon.model.Settings;
import com.backgammon.view.AuthorizationWindow;
import com.backgammon.view.BackgammonView;
import com.backgammon.view.PlayerListView;
import lab2.protocol.Transport;
import lab2.protocol.TransportXML;
import lab2.protocol.User;
import lab2.protocol.envelope.Msg;
import lab2.protocol.envelope.MsgAuth;
import lab2.protocol.envelope.MsgPlayRequest;
import lab2.protocol.envelope.MsgReg;
import lab2.protocol.model.BackgammonModel;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Set;

/**
 * Manages events
 */
public class BackgammonController extends MouseAdapter implements ActionListener {

    private static final Logger log = Logger.getLogger(BackgammonController.class.getName());

    // static block, executes when class is loaded first time
    static {
        try {
            // Set system gui skin
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            log.error(e);
            System.exit(2);
        }
    }

    private Transport transport;
    public BackgammonView backgammonView;
    private AuthorizationWindow authorizationWindow;
    private PlayerListView playerListView;
    private PlayerModel playerModel;
    private GameModel gameModel;
    private String myUsername;
    private String opponentUsername;


    public BackgammonController() throws IOException {
        Socket clientSocket = new Socket();
        clientSocket.connect(new InetSocketAddress(Settings.HOST, Settings.PORT), Settings.SO_TIMEOUT);
        transport = new TransportXML(clientSocket);
	}

    public Transport getTransport() {
        return transport;
    }

    private void createBackgammon() {

        gameModel = new GameModel(new BackgammonModel(playerModel.getPlayer(myUsername), playerModel.getPlayer(opponentUsername)), myUsername, opponentUsername);

        backgammonView = new BackgammonView(gameModel);
        backgammonView.addActionListener(this);
        backgammonView.addMouseListener(this);

    }


    public void updateBackgammonModel(String nickname, int firstCheckerMoveFrom, int firstCheckerMoveInto,
                                      int secondCheckerMoveFrom,  int secondCheckerMoveInto) {

        if (backgammonView == null || backgammonView.isClosed()) {
            createBackgammon();
        } else {
            gameModel.updateBackgammonModel(nickname, firstCheckerMoveFrom, firstCheckerMoveInto,
                                            secondCheckerMoveFrom, secondCheckerMoveInto);
        }

        closePlayerList();
        showGameWindow();

    }


    public void showGameWindow() {
        backgammonView.showView();
    }

    public void closeGameWindow() {
        backgammonView.closeView();
    }


    public void sendRequest(User opponent) {
        opponentUsername = opponent.getNickname();
        try {
            getTransport().send(new MsgPlayRequest(opponentUsername));
        } catch (IOException e) {
            showError(Settings.ERR_ConnectionErr);
        }
    }


	@Override
	public void actionPerformed(ActionEvent event) {

        if (event.getActionCommand().equals(Settings.ACTION_LEAVE_GAME)) {
            log.debug(Settings.DBG_LeavingGame);

            try {
                getTransport().send(new Msg("ExitGame"));
            } catch (IOException e) {

            }

            backgammonView.showInfoMessage(Settings.INF_LeavingGame);
            closeGameWindow();
            showPlayerList();
		}

		if (event.getActionCommand().equals(Settings.ACTION_EXIT) ||
            event.getActionCommand().equals(Settings.EXIT)) {

            log.debug(Settings.DBG_ExitProgram);
            try {
                getTransport().send(new Msg("Exit"));
            } catch (IOException e) {}

            try {
                Settings.writeSettingsIntoXML();
            } catch (Exception e) {
                showError(Settings.ERR_SaveSettingsErr);
            }
            System.exit(0);
		}

	}

	public void mousePressed(MouseEvent e) {

	}


    public void createPlayerListView(Set<User> playerSet) {

        playerModel = new PlayerModel(playerSet, myUsername);

        playerListView = new PlayerListView(playerModel);
        playerListView.setListener(this);

    }

    public void showPlayerList() {
        playerListView.showView();
        playerListView.setEnabledConnect(true);
    }

    public void closePlayerList() {
        playerListView.closeView();
    }

    public void updatePlayerSet(Set<User> playerSet) {

        if (playerListView == null) {
            createPlayerListView(playerSet);
        } else {
            playerModel.updatePlayerSet(playerSet);
        }

        showPlayerList();

    }


    public void showAuthorizationWindow() {

        if (authorizationWindow == null) {
            authorizationWindow = new AuthorizationWindow(this);
        }

        authorizationWindow.setDefaultUser();

        if (authorizationWindow.showDialog(null, Settings.INF_DialogTitle)) {

            log.debug("myUsername " + authorizationWindow.getLogin() + ", password: " + authorizationWindow.getPassword());

            myUsername = authorizationWindow.getLogin();
            String password = authorizationWindow.getPassword();

            if (authorizationWindow.isSelectedRegistration()) {
                log.debug("Registration: username - " + myUsername + " password - " + password);
                try {
                    getTransport().send(new MsgReg(myUsername, password));
                } catch (IOException e) {
                    showError(Settings.ERR_ConnectionErr);
                    System.exit(0);
                }
            } else {
                log.debug("Authorization: username - " + myUsername + " password - " + password);
                try {
                    getTransport().send(new MsgAuth(myUsername, password));
                } catch (IOException e) {
                    showError(Settings.ERR_ConnectionErr);
                    System.exit(0);
                }
            }

        }

    }

    public static void showError(String message) {
        log.error(message);
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showConfirm(String message, String nick) {

        int answer = JOptionPane.showConfirmDialog(playerListView.getFrame(), message);
        try {
            if (answer == JOptionPane.YES_OPTION) {
                opponentUsername = nick;
                getTransport().send(new Msg("YesGame"));
            } else {
                getTransport().send(new Msg("NoGame"));
            }
        } catch (IOException e) {
            showError(Settings.ERR_ConnectionErr);
        }
    }

    public void lostAction() {
        backgammonView.showInfoMessage(Settings.INF_Looser);
        closeGameWindow();
        showPlayerList();
    }

    public void wonAction() {
        backgammonView.showInfoMessage(Settings.INF_Winner);
        closeGameWindow();
        showPlayerList();
    }

    public void showInfoMessage(String message) {
        backgammonView.showInfoMessage(message);
    }

}
