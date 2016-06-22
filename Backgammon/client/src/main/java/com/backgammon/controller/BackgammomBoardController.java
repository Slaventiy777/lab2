package com.backgammon.controller;

import com.backgammon.model.BackgammonBoardModel;
import com.backgammon.model.Cell;
import com.backgammon.model.Checker;
import com.backgammon.model.Settings;
import com.backgammon.view.AuthorizationWindow;
import com.backgammon.view.BackgammonBoardView;

import lab2.protocol.Transport;
import lab2.protocol.TransportXML;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class BackgammomBoardController implements MouseListener, MouseMotionListener {

    //private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final Logger log = Logger.getLogger(BackgammomBoardController.class.getName());

    // static block, executes when class is loaded first time
    static {
        try {
            // Set system gui skin
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            log.error(e);
            System.exit(2);
        }
    }

    private BackgammonBoardView boardView;
    private BackgammonBoardModel boardModel;

    private Cell previousCell;
    private Checker previousFigure;
    private Cell currentCell;

    private Transport transport;

    private AuthorizationWindow authorizationWindow;

//    public BackgammomBoardController(BackgammonBoardModel backgammonBoardModel, BackgammonBoardView backgammonBoardView) {
//        this.boardModel = backgammonBoardModel;
//        this.boardView = backgammonBoardView;
//
//        init();
//    }


    public BackgammomBoardController() throws IOException {
        Socket clientSocket = new Socket();
        clientSocket.connect(new InetSocketAddress(Settings.HOST, Settings.PORT), Settings.SO_TIMEOUT);
        transport = new TransportXML(clientSocket);
    }

    public Transport getTransport() {
        return transport;
    }

    public static void showError(String message) {
        log.error(message);
        JOptionPane.showMessageDialog(null, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void showAuthorizationWindow() {

        if (authorizationWindow == null) {
            //create new if it first time
            authorizationWindow = new AuthorizationWindow(this);
        }
        authDialog.setDefaultUser();

        if (authDialog.showDialog(null, Settings.INF_DialogTitle)) {
            // if accepted, retrieve user input
            log.debug("myUsername " + authDialog.getUsername() + ", pass: " + authDialog.getPassword());

            myUsername = authDialog.getUsername();
            String pass = authDialog.getPassword();

            if (authDialog.getReg()) {
                log.debug("Registration: username - " + myUsername + " pass - " + pass);
                try {
                    getTransport().send(new RegAuth(myUsername, pass, RegAuth.Type.Register));
                } catch (IOException e) {
                    showError(Settings.ERR_ConnectionErr);
                    System.exit(0);
                }
            } else {
                log.debug("Authorization: username - " + myUsername + " pass - " + pass);
                try {
                    getTransport().send(new RegAuth(myUsername, pass, RegAuth.Type.Authorization));
                } catch (IOException e) {
                    showError(Settings.ERR_ConnectionErr);
                    System.exit(0);
                }
            }
        }

    }



    private void init(){
        boardModel.initBoard();
        boardModel.initFigurs();
    }


    protected void processOperation(String operationName, String message) {
        switch (operationName){
            case "error":
                try {
                    processError(message);
                } catch (ParserConfigurationException e) {
                    System.err.println("Internal error");
                } catch (IOException e) {
                    System.err.println("Internal error");
                } catch (SAXException e) {
                    System.err.println("Internal error");
                }
                break;
            case "status":
                try {
                    processStatus(message);
                } catch (ParserConfigurationException e) {
                    System.err.println("Internal error");
                } catch (IOException e) {
                    System.err.println("Internal error");
                } catch (SAXException e) {
                    System.err.println("Internal error");
                }
                break;
        }
    }

    protected void processError(String message) throws IOException, SAXException, ParserConfigurationException {
        LinkedList<Transport.ErrorType> errorTypes = MessageParser.getErrorTypes(message);

        for (Transport.ErrorType errorType : errorTypes) {
            switch (errorType) {
                case REGISTRATION:
                    try {
                        openMainQuestion();
                    } catch (ParserConfigurationException e) {
                        System.err.println("Registration error!");
                    } catch (TransformerException e) {
                        System.err.println("Registration error!");
                    } catch (IOException e) {
                        System.err.println("Registration error!");
                    }
                    break;
                case CONNECTION:
                    try {
                        openMainQuestion();
                    } catch (TransformerException e) {
                        System.err.println("Connection error!");
                    }
                    break;
            }
        }
    }

    protected void processStatus(String message) throws ParserConfigurationException, IOException, SAXException {
        LinkedList<Transport.StatusType> statusTypes = MessageParser.getStatusTypes(message);

        for (Transport.StatusType statusType : statusTypes) {
            switch (statusType) {
                case REGISTRATION:
                    try {
                        openMainQuestion();
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    }
                    break;
                case CONNECTION:
                    try {
                        openConnectionQuestion();
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public void openMainQuestion() throws ParserConfigurationException, TransformerException, IOException {
        System.out.println("\nChoose operation:");
        System.out.println("1. Registration");
        System.out.println("2. Connection");
        System.out.println("0. Exit");

        int answer = 0;
        try {
            answer = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            System.out.println("Invalid operation!");
            openMainQuestion();
            return;
        }
        catch (NumberFormatException e){
            System.out.println("Invalid operation!");
            openMainQuestion();
            return;
        }

        switch (answer) {
            case 1: registerOnServer();
                break;
            case 2: connectToServer();
                break;
            case 0:  exit();
                break;
            default:
                System.out.println("Invalid operation!");
                openMainQuestion();
                break;
        }
    }

    public void openConnectionQuestion() throws IOException, TransformerException, ParserConfigurationException {
        System.out.println("\nChoose operation:");
        System.out.println("1. Get gamer list");
        System.out.println("0. Exit");

        int answer = 0;
        try {
            answer = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            System.out.println("Invalid operation!");
            openConnectionQuestion();
            return;
        }
        catch (NumberFormatException e){
            System.out.println("Invalid operation!");
            openConnectionQuestion();
            return;
        }

        switch (answer) {
            case 1: getFreeUsers();
                break;
            case 0:  exit();
                break;
            default:
                System.out.println("Invalid operation!");
                openConnectionQuestion();
                break;
        }
    }

    public void registerOnServer() throws IOException, TransformerException, ParserConfigurationException {
        System.out.println("\n-----------------Registration-----------------");
        System.out.println("\nEnter username: ");
        String username = br.readLine();
        System.out.println("\nEnter password: ");
        String password = br.readLine();

        String message = createRegistrationMessage(username, password);
        getTransport().send(message);
    }

    public String createRegistrationMessage(String nickname, String password) throws ParserConfigurationException, TransformerException {
        HashMap param = new LinkedHashMap();
        param.put("nickname", nickname);
        param.put("password", password);
        return MessageParser.createMessage("registration", param);
    };

    public void connectToServer() throws IOException, TransformerException, ParserConfigurationException {
        System.out.println("\n-----------------Connection-----------------");
        System.out.println("\nEnter username: ");
        String username = br.readLine();
        System.out.println("\nEnter password: ");
        String password = br.readLine();

        String message = createConnectionMessage(username, password);
        getTransport().send(message);
    }

    public void getFreeUsers() throws IOException, TransformerException, ParserConfigurationException {
        String message = createGetFreeUsersMessage();
        getTransport().send(message);
    }

    public String createConnectionMessage(String nickname, String password) throws TransformerException, ParserConfigurationException {
        HashMap param = new LinkedHashMap();
        param.put("nickname", nickname);
        param.put("password", password);
        return MessageParser.createMessage("connection", param);
    }

    public String createGetFreeUsersMessage() throws TransformerException, ParserConfigurationException {
        return MessageParser.createMessage("getFreeUsers", new LinkedHashMap());
    }

    public void exit(){
        System.exit(1);
    }



    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        Point2D p = e.getPoint();
        for (Cell cell : boardModel.getBoardCells()) {
            if (!cell.getBoardCell().contains(p))
                continue;

            if (cell.getListCheckers().size() > 0) {

                Checker checker = cell.getListCheckers().getLast();
                if (checker != null) {
                    if (checker.getChecker().contains(p)) {
                        previousCell = cell;
                        previousFigure = checker;
                    }
                }

            }

        }
    }

    @Override
    public void mouseReleased (MouseEvent e){

        if (previousCell == null)
            return;

        Point2D p = e.getPoint();
        for(Cell cell : boardModel.getBoardCells()) {
            if(cell.getBoardCell().contains(p)) {
                currentCell = cell;
                break;
            }
        }

        if (currentCell == null) {
            currentCell = previousCell;

            boardModel.drawFigursInCell(currentCell);
            currentCell = null;
            previousCell = null;
            previousFigure = null;
        } else {
            if (currentCell.getColorCheckers() != null && currentCell.getColorCheckers() != previousCell.getColorCheckers()) {
                currentCell = previousCell;

                boardModel.drawFigursInCell(currentCell);
                currentCell = null;
                previousCell = null;
                previousFigure = null;
            } else {
                currentCell.getListCheckers().add(previousCell.getListCheckers().getLast());
                currentCell.setColorCheckers(currentCell.getListCheckers().getLast().getCheckerColor());

                previousCell.getListCheckers().removeLast();

                boardModel.drawFigursInCell(previousCell);
                boardModel.drawFigursInCell(currentCell);
                currentCell = null;
                previousCell = null;
                previousFigure = null;
            }

        }

        boardView.repaint();

    }

    @Override
    public void mouseEntered (MouseEvent e){

    }

    @Override
    public void mouseExited (MouseEvent e){

    }

    @Override
    public void mouseDragged (MouseEvent e){

        int x = e.getX();
        int y = e.getY();

        if(previousFigure != null)
            previousFigure.getChecker().setFrame(x, y, 0.8 * boardModel.getWidthCell(), 0.8 * boardModel.getWidthCell());

        boardView.repaint();

    }

    @Override
    public void mouseMoved (MouseEvent e){

    }

}
