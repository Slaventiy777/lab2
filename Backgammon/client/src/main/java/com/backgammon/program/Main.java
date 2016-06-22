package com.backgammon.program;

import com.backgammon.controller.BackgammonController;
import com.backgammon.controller.EventLoop;
import com.backgammon.model.Settings;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.IOException;

public class Main {

    static Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        log.debug("Starting program...");

        try {
            Settings.readSettingsFromXML();
        } catch (Exception e) {
            log.error(Settings.ERR_CannotReadSettings);
        }

        try {
            BackgammonController backgammomController = new BackgammonController();
            EventLoop eventLoop = new EventLoop(backgammomController);
            eventLoop.setDaemon(true);
            eventLoop.start();

            //wait 3 sec before receiving
            Thread.sleep(Settings.MILLIS_WAIT);
        } catch (IOException e) {
            log.error(e);
            JOptionPane.showMessageDialog(null, Settings.ERR_ConnectionRefused, "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException e) {
            log.debug(e);
        }


    }

}
