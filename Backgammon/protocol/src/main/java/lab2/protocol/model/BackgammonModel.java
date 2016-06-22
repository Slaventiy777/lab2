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

    public void afterDidMove(String currentUser, int firstCheckerMoveFrom, int firstCheckerMoveInto,
                             int secondCheckerMoveFrom,  int secondCheckerMoveInto) {

        log.debug("" + currentUser + " move [" + firstCheckerMoveFrom + "->" + firstCheckerMoveInto
                + " " + secondCheckerMoveFrom + "->" + secondCheckerMoveInto + "]");

        if (currentUser.equals(firstUser.getNickname()) && enableMoveFirstUser) {

            enableMoveFirstUser = false;
            enableMoveSecondUser = true;

            moveBetweenCell(firstCheckerMoveFrom, firstCheckerMoveInto, secondCheckerMoveFrom, secondCheckerMoveInto);

            if (firstCheckerMoveInto == -1)
                numCheckerInGameFirstUser--;

            if (secondCheckerMoveInto == -1)
                numCheckerInGameFirstUser--;

        }

        if (currentUser.equals(secondUser.getNickname()) && enableMoveSecondUser) {

            enableMoveFirstUser = true;
            enableMoveSecondUser = false;

            moveBetweenCell(firstCheckerMoveFrom, firstCheckerMoveInto, secondCheckerMoveFrom, secondCheckerMoveInto);

            if (firstCheckerMoveInto == -1)
                numCheckerInGameSecondUser--;

            if (firstCheckerMoveInto == -1)
                numCheckerInGameSecondUser--;

        }

        if (numCheckerInGameFirstUser == 0 || numCheckerInGameSecondUser == 0) {
            endGame = true;
        }

    }

    private void moveBetweenCell(int firstCheckerMoveFrom, int firstCheckerMoveInto,
                                 int secondCheckerMoveFrom,  int secondCheckerMoveInto) {

        Checker firstCheckerTemp = null;
        Checker secondCheckerTemp = null;

        int firstCellFrom = -1;
        int firstCellInto = -1;
        int secondCellFrom = -1;
        int secondCellInto = -1;

        int i = -1;
        for (Cell cell : boardCells) {

            i++;

            if (cell.getCheckers().size() > 0 && cell.getCellNumber() == firstCheckerMoveFrom) {
                firstCellFrom = i;
                firstCheckerTemp = cell.getCheckers().getLast();
            }

            if (firstCheckerMoveInto != -1 && cell.getCellNumber() == firstCheckerMoveInto)
                firstCellInto = i;

            if (cell.getCheckers().size() > 0 && cell.getCellNumber() == secondCheckerMoveFrom) {
                secondCellFrom = i;
                secondCheckerTemp = cell.getCheckers().getLast();
            }

            if (secondCheckerMoveInto != -1 && cell.getCellNumber() == secondCheckerMoveInto)
                secondCellInto = i;

        }

        if (firstCellFrom != -1)
            if (boardCells[firstCellFrom].getCheckers().size() > 0)
                boardCells[firstCellFrom].getCheckers().removeLast();

        if (firstCellInto != -1)
            if (firstCheckerTemp != null)
                boardCells[firstCellInto].getCheckers().add(firstCheckerTemp);

        if (secondCellFrom != -1)
            if (boardCells[secondCellFrom].getCheckers().size() > 0)
                boardCells[secondCellFrom].getCheckers().removeLast();

        if (secondCellInto != -1)
            if (secondCheckerTemp != null)
                boardCells[secondCellInto].getCheckers().add(secondCheckerTemp);

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
