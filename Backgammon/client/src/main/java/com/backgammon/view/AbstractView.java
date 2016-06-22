package com.backgammon.view;

import com.backgammon.model.Settings;
import org.apache.log4j.Logger;

import javax.swing.*;

public abstract class AbstractView {

    static Logger log = Logger.getLogger(AbstractView.class.getName());

    //public static final String ACTION_LEAVE_GAME = "leave game";

    private boolean closed = false;

    public abstract JFrame getFrame();

    protected abstract void initializeUI();

    public abstract void update();

    public void showView() {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getFrame().setVisible(true);
                log.debug(Settings.DBG_ShowView);
            }
        });

    }

    public void closeView() {

        if (!getFrame().isVisible())
            return;

        getFrame().setVisible(false);
        getFrame().dispose();

        log.debug(Settings.DBG_ClosingView);

        setClosed(true);

    }


    public void showError(String message) {
        log.error("error message: " + message);
        JOptionPane.showMessageDialog(getFrame(), message, "Error", JOptionPane.ERROR_MESSAGE);

    }

    public void showInfoMessage(String message) {
        log.debug("info message: " + message);
        JOptionPane.showMessageDialog(getFrame(), message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean isClosed() {
        return closed;
    }

}
