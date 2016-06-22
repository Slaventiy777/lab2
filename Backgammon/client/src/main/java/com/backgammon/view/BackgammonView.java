package com.backgammon.view;

import com.backgammon.model.GameModel;
import com.backgammon.model.Settings;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class BackgammonView extends AbstractView implements Observer {

    static Logger log = Logger.getLogger(BackgammonView.class.getName());

    private static final String FRAME_TITLE = "Backgammon game";
    private ArrayList<ActionListener> actionListeners = new ArrayList<ActionListener>();
    private ArrayList<MouseListener> mouseListeners = new ArrayList<MouseListener>();

    private GameModel gameModel;

    private JFrame frame;

    public BackgammonView(GameModel gameModel) {

        this.gameModel = gameModel;
        this.gameModel.observable().addObserver(this);
        initializeUI();

        log.debug(Settings.DBG_CreatingView);

    }


    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    public void addMouseListener(MouseListener listener) {
        mouseListeners.add(listener);
    }

    @Override
    public JFrame getFrame() {
        return frame;
    }

    @Override
    protected void initializeUI() {

        frame = new JFrame(FRAME_TITLE + " [" + gameModel.getPlayerMe() + "]");

    }

    @Override
    public void update() {
        update(null, null);
    }

    public void update(Observable source, Object arg) {
        log.debug(Settings.DBG_UpdatingView);
    }

}
