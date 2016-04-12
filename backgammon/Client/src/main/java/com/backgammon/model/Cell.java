package com.backgammon.model;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

public class Cell {

    private int cellNumber;
    private Rectangle2D boardCell;

    //private int amountCheckers;
    private Color colorCheckers;

    private Shape triangleIndicator;
    private Ellipse2D ellipseIndicator;

    private LinkedList<Checker> checkers;

    public Cell(int cellNumber, Rectangle2D boardCell) {
        this.cellNumber = cellNumber;
        this.boardCell = boardCell;

        checkers = new LinkedList<Checker>();
    }

    public int getCellNumber() {
        return cellNumber;
    }

    public Rectangle2D getBoardCell() {
        return boardCell;
    }

    public Color getColorCheckers() {
        return colorCheckers;
    }

    public void setColorCheckers(Color colorCheckers) {
        this.colorCheckers = colorCheckers;
    }

    public void addChecker(Checker checker) {
        checkers.add(checker);
    }

    public void removeChecker() {
        checkers.remove(checkers.size() - 1);
    }

    public LinkedList<Checker> getListCheckers() {
        return checkers;
    }

    public Shape getTriangleIndicator() {
        return triangleIndicator;
    }

    public void setTriangleIndicator(Shape triangleIndicator) {
        this.triangleIndicator = triangleIndicator;
    }

    public Ellipse2D getEllipseIndicator() {
        return ellipseIndicator;
    }

    public void setEllipseIndicator(Ellipse2D ellipseIndicator) {
        this.ellipseIndicator = ellipseIndicator;
    }

}
