package lab2.protocol.model;

import lab2.protocol.User;
import org.apache.log4j.Logger;

import java.io.Serializable;

public class BackgammonModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(BackgammonModel.class.getName());

    private User firstUser;
    private User secondUser;

    private ColorUser firstUserColor;
    private ColorUser secondUserColor;

    private Cell[] boardCells;
    private int numCheckerInGameFirstUser;
    private int numCheckerInGameSecondUser;

    private boolean endGame = false;
    private boolean enableMoveFirstUser;
    private boolean enableMoveSecondUser;

    public BackgammonModel(User user1, User user2) {
        this(user1, user2, ColorUser.COLOR_WHITE, ColorUser.COLOR_BLACK);
    }

    public BackgammonModel(User user1, User user2, ColorUser colorUser1, ColorUser colorUser2) {

        int numChecker = 16;
        int numCell = 24;

        firstUser = user1;
        secondUser = user2;

        firstUserColor = colorUser1;
        secondUserColor = colorUser2;

        boardCells = new Cell[numCell];
        setCellOnBoard();

        setNumCheckerInGameFirstUser(numChecker);
        setNumCheckerInGameSecondUser(numChecker);

        setEnableMoveFirstUser(true);
        setEnableMoveSecondUser(false);

    }

    private void setCellOnBoard() {

        Cell tempCell = null;
        for (int i = 0; i < 24; i++) {

            tempCell = new Cell(i + 1);

            if (tempCell.getCellNumber() == 11)
                setCheckersOnCell(tempCell, ColorUser.COLOR_BLACK);

            if (tempCell.getCellNumber() == 12)
                setCheckersOnCell(tempCell, ColorUser.COLOR_WHITE);

            boardCells[i] = tempCell;

        }

    }

    private void setCheckersOnCell(Cell cell, ColorUser colorCheker) {

        cell.setColorCheckers(colorCheker);

        Checker tempChecker = null;
        for (int i = 0; i < 16; i++) {
            tempChecker = new Checker(colorCheker);

            cell.getCheckers().add(tempChecker);
        }

    }

    public void afterDidMove(User currentUser, int firstCheckerMoveFrom, int firstCheckerMoveInto,
                             int secondCheckerMoveFrom,  int secondCheckerMoveInto) {

        log.debug("" + currentUser.getNickname() + " move [" + firstCheckerMoveFrom + "->" + firstCheckerMoveInto
                + " " + secondCheckerMoveFrom + "->" + secondCheckerMoveInto + "]");

        if (currentUser.equals(firstUser) && enableMoveFirstUser) {
            enableMoveFirstUser = false;
            enableMoveSecondUser = true;
        }

        if (currentUser.equals(secondUser) && enableMoveSecondUser) {
            enableMoveFirstUser = true;
            enableMoveSecondUser = false;
        }

        if (numCheckerInGameFirstUser == 0 || numCheckerInGameSecondUser == 0) {
            endGame = true;
        }

    }


    public User getFirstUser() {
        return firstUser;
    }

    public User getSecondUser() {
        return secondUser;
    }

    public ColorUser getFirstUserColor() {
        return firstUserColor;
    }

    public ColorUser getSecondUserColor() {
        return secondUserColor;
    }

    public Cell[] getBoardCells() {
        return boardCells;
    }

    public int getNumCheckerInGameFirstUser() {
        return numCheckerInGameFirstUser;
    }

    public void setNumCheckerInGameFirstUser(int numCheckerInGameFirstUser) {
        this.numCheckerInGameFirstUser = numCheckerInGameFirstUser;
    }

    public int getNumCheckerInGameSecondUser() {
        return numCheckerInGameSecondUser;
    }

    public void setNumCheckerInGameSecondUser(int numCheckerInGameSecondUser) {
        this.numCheckerInGameSecondUser = numCheckerInGameSecondUser;
    }

    public boolean isEnableMoveFirstUser() {
        return enableMoveFirstUser;
    }

    public boolean isEnableMoveSecondUser() {
        return enableMoveSecondUser;
    }

    public void setEnableMoveFirstUser(boolean enableMoveFirstUser) {
        this.enableMoveFirstUser = enableMoveFirstUser;
    }

    public void setEnableMoveSecondUser(boolean enableMoveSecondUser) {
        this.enableMoveSecondUser = enableMoveSecondUser;
    }

    public boolean isEndGame() {
        return endGame;
    }

}
