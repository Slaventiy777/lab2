package lab2.protocol.model;

import java.io.Serializable;
import java.util.LinkedList;

public class Cell implements Serializable {

    private static final long serialVersionUID = 1L;

    private int cellNumber;
    private ColorUser colorCheckers;

    private LinkedList<Checker> checkers;

    public Cell(int cellNumber) {
        this.cellNumber = cellNumber;

        checkers = new LinkedList<Checker>();
    }

    public int getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(int cellNumber) {
        this.cellNumber = cellNumber;
    }

    public ColorUser getColorCheckers() {
        return colorCheckers;
    }

    public void setColorCheckers(ColorUser colorCheckers) {
        this.colorCheckers = colorCheckers;
    }

    public LinkedList<Checker> getCheckers() {
        return checkers;
    }

    public void setCheckers(LinkedList<Checker> checkers) {
        this.checkers = checkers;
    }

}
