package com.backgammon.model;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class BackgammonBoardModel {

    public static final Color whiteColor = Color.WHITE;
    public static final Color blackColor = Color.BLACK;

    public static final Color greenColor        = new Color(0, 250, 0);
    public static final Color redColor          = new Color(220, 90, 80);
    public static final Color brownBronzeColor  = new Color(205, 127, 50);
    public static final Color brownColor        = new Color(105, 75, 0);
    public static final Color brownBuffColor    = new Color(240, 220, 130);

    public static final Icon dice1 = new ImageIcon("dice1.png");
    public static final Icon dice2 = new ImageIcon("dice2.png");
    public static final Icon dice3 = new ImageIcon("dice3.png");
    public static final Icon dice4 = new ImageIcon("dice4.png");
    public static final Icon dice5 = new ImageIcon("dice5.png");
    public static final Icon dice6 = new ImageIcon("dice6.png");

    private Cell[] boardCells = new Cell[24];
    private Rectangle2D[] midboardCells = new Rectangle2D[4];
    private Rectangle2D[] sidePanel = new Rectangle2D[4];

    private double widthCell = 75;
    private double hightCell = 300;

    private double xS = 10;
    private double yS = 32;
    private double widthMidCell = 30;

    public void initBoard() {

        sidePanel[0] = new Rectangle2D.Double(xS, yS + widthCell / 2, widthCell, 2 * hightCell); // left panel
        sidePanel[1] = new Rectangle2D.Double(xS + 13 * widthCell + widthMidCell, yS + widthCell / 2, widthCell, 2 * hightCell); // right panel

        sidePanel[2] = new Rectangle2D.Double(xS, yS, 13 * widthCell + widthCell + widthMidCell, widthCell / 2); // top panel
        sidePanel[3] = new Rectangle2D.Double(xS, yS + 2 * hightCell + widthCell / 2, 13 * widthCell + widthCell + widthMidCell, widthCell / 2); // bottom panel

        int[] x = new int[4];
        int[] y = new int[4];

        Rectangle2D rectangle2D;
        Cell cell;

        for(int i = 0; i < 13; i++) {
            for(int j = 0; j < 2; j++) {

                rectangle2D = null;
                cell = null;

                if (i < 6) {

                    rectangle2D = new Rectangle2D.Double(xS + i * widthCell + widthCell, yS + j * hightCell + widthCell / 2, widthCell, hightCell);
                    cell = new Cell(i + 12 * j + 1, rectangle2D);

                    x[0] = (int) (xS + (i * widthCell + widthCell) + widthCell * 0.1);
                    x[1] = (int) (xS + (i * widthCell + widthCell) + widthCell * 0.5);
                    x[2] = (int) (xS + (i * widthCell + widthCell) + widthCell * 0.9);
                    x[3] = (int) (xS + (i * widthCell + widthCell) + widthCell * 0.5);

                    if (j == 0) {
                        y[0] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell * 0.1);
                        if (i == 0 || i == 5) {
                            y[1] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell * 0.9);
                        } else if (i == 1 || i == 4) {
                            y[1] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell * 0.8);
                        } else if (i == 2 || i == 3) {
                            y[1] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell * 0.7);
                        }
                        y[2] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell * 0.1);
                        y[3] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell * 0.15);
                    } else {
                        y[0] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell - hightCell * 0.1);
                        if (i == 0 || i == 5) {
                            y[1] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell - hightCell * 0.9);
                        } else if (i == 1 || i == 4) {
                            y[1] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell - hightCell * 0.8);
                        } else if (i == 2 || i == 3) {
                            y[1] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell - hightCell * 0.7);
                        }
                        y[2] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell - hightCell * 0.1);
                        y[3] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell - hightCell * 0.15);
                    }

                    cell.setTriangleIndicator(new Polygon(x, y, 4));

                    if (j == 0) {
                        double tempX = xS + i * widthCell + widthCell + (0.5 * widthCell - 0.25 * 0.5 * widthCell);
                        double tempY = yS + 0.5 * widthCell / 2 - 0.5 * 0.5 * widthCell / 2;

                        cell.setEllipseIndicator(new Ellipse2D.Double(tempX, tempY, widthCell / 4, widthCell / 4));
                    }
                    else if (j == 1) {
                        double tempX = xS + i * widthCell + widthCell + (0.5 * widthCell - 0.25 * 0.5 * widthCell);
                        double tempY = (yS + 2 * hightCell + widthCell / 2) + (0.5 * widthCell / 2 - 0.5 * 0.5 * widthCell / 2);

                        cell.setEllipseIndicator(new Ellipse2D.Double(tempX, tempY, widthCell / 4, widthCell / 4));
                    }

                    boardCells[cell.getCellNumber() - 1] = cell;

                } else if (i == 6) {

                    midboardCells[j] = new Rectangle2D.Double(xS + i * widthCell + widthCell, yS + j * hightCell + widthCell / 2, widthMidCell, hightCell);

                    if (j == 0)
                        midboardCells[j+2] = new Rectangle2D.Double(xS + i * widthCell + widthCell + widthCell * 0.1, yS + j * hightCell + widthCell / 2 + hightCell * 0.25, widthMidCell * 0.5, hightCell * 0.5);
                    else
                        midboardCells[j+2] = new Rectangle2D.Double(xS + i * widthCell + widthCell + widthCell * 0.1, yS + j * hightCell + widthCell / 2 + hightCell * 0.25, widthMidCell * 0.5, hightCell * 0.5);

                } else if (i > 6) {

                    rectangle2D = new Rectangle2D.Double(xS + i * widthCell - (widthCell - widthMidCell) + widthCell, yS + j * hightCell + widthCell / 2, widthCell, hightCell);
                    cell = new Cell(i + 12 * j, rectangle2D);

                    x[0] = (int) (xS + (i * widthCell - (widthCell - widthMidCell)) + widthCell + widthCell * 0.1);
                    x[1] = (int) (xS + (i * widthCell - (widthCell - widthMidCell)) + widthCell + widthCell * 0.5);
                    x[2] = (int) (xS + (i * widthCell - (widthCell - widthMidCell)) + widthCell + widthCell * 0.9);
                    x[3] = (int) (xS + (i * widthCell - (widthCell - widthMidCell)) + widthCell + widthCell * 0.5);

                    if (j == 0) {
                        y[0] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell * 0.1);
                        if (i == 7 || i == 12) {
                            y[1] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell * 0.9);
                        } else if (i == 8 || i == 11) {
                            y[1] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell * 0.8);
                        } else if (i == 9 || i == 10) {
                            y[1] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell * 0.7);
                        }
                        y[2] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell * 0.1);
                        y[3] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell * 0.15);
                    } else {
                        y[0] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell - hightCell * 0.1);
                        if (i == 7 || i == 12) {
                            y[1] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell - hightCell * 0.9);
                        } else if (i == 8 || i == 11) {
                            y[1] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell - hightCell * 0.8);
                        } else if (i == 9 || i == 10) {
                            y[1] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell - hightCell * 0.7);
                        }
                        y[2] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell - hightCell * 0.1);
                        y[3] = (int) (yS + (j * hightCell + widthCell / 2) + hightCell - hightCell * 0.15);
                    }

                    cell.setTriangleIndicator(new Polygon(x, y, 4));

                    if (j == 0) {
                        double tempX = xS + i * widthCell - (widthCell - widthMidCell) + widthCell + (0.5 * widthCell - 0.25 * 0.5 * widthCell);
                        double tempY = yS + 0.5 * widthCell / 2 - 0.5 * 0.5 * widthCell / 2;

                        cell.setEllipseIndicator(new Ellipse2D.Double(tempX, tempY, widthCell / 4, widthCell / 4));
                    } else if (j == 1) {
                        double tempX = xS + i * widthCell - (widthCell - widthMidCell) + widthCell + (0.5 * widthCell - 0.25 * 0.5 * widthCell);
                        double tempY = (yS + 2 * hightCell + widthCell / 2) + (0.5 * widthCell / 2 - 0.5 * 0.5 * widthCell / 2);

                        cell.setEllipseIndicator(new Ellipse2D.Double(tempX, tempY, widthCell / 4, widthCell / 4));
                    }

                    boardCells[cell.getCellNumber() - 1] = cell;

                }

            }
        }

    }

    public void initFigurs() {

        boardCells[12].setColorCheckers(whiteColor);
        for (int i = 0; i < 16; i++) {
            double tempX = xS + widthCell + 0.1 * widthCell;
            double tempY = yS + widthCell / 2 + 2 * hightCell - 0.05 * hightCell - 0.8 * widthCell - i* (hightCell - 0.05 * hightCell - 0.8 * widthCell - 0.1 * hightCell) / 15;

            boardCells[12].addChecker(new Checker(whiteColor, new Ellipse2D.Double(tempX, tempY, 0.8 * widthCell, 0.8 * widthCell)));
        }

        boardCells[11].setColorCheckers(blackColor);
        for (int i = 0; i < 16; i++) {
            double tempX = xS + widthCell + 12 * widthCell - (widthCell - widthMidCell) + 0.1 * widthCell;
            double tempY = yS + widthCell / 2 + 0.05 * hightCell + i* (hightCell - 0.05 * hightCell - 0.8 * widthCell - 0.1 * hightCell) / 15;

            boardCells[11].addChecker(new Checker(blackColor, new Ellipse2D.Double(tempX, tempY, 0.8 * widthCell, 0.8 * widthCell)));
        }

    }

    public void drawFigursInCell(Cell cell) {

        int i = -1;
        for (Checker checker : cell.getListCheckers()) {

            i++;

            double tempX = 0;
            double tempY = 0;

            if (cell.getCellNumber() >= 1 && cell.getCellNumber() <= 6) {

                tempX = xS + widthCell + (cell.getCellNumber() - 1) * widthCell + 0.1 * widthCell;
                tempY = yS + widthCell / 2 + 0.05 * hightCell + i* (hightCell - 0.05 * hightCell - 0.8 * widthCell - 0.1 * hightCell) / cell.getListCheckers().size();

            } else if (cell.getCellNumber() >= 7 && cell.getCellNumber() <= 12) {

                tempX = xS + widthCell + cell.getCellNumber() * widthCell - (widthCell - widthMidCell) + 0.1 * widthCell;
                tempY = yS + widthCell / 2 + 0.05 * hightCell + i* (hightCell - 0.05 * hightCell - 0.8 * widthCell - 0.1 * hightCell) / cell.getListCheckers().size();

            } else if (cell.getCellNumber() >= 13 && cell.getCellNumber() <= 18) {

                tempX = xS + widthCell + (cell.getCellNumber() - 12 - 1) * widthCell + 0.1 * widthCell;
                tempY = yS + widthCell / 2 + 2 * hightCell - 0.05 * hightCell - 0.8 * widthCell - i * (hightCell - 0.05 * hightCell - 0.8 * widthCell - 0.1 * hightCell) / cell.getListCheckers().size();

            } else if (cell.getCellNumber() >= 19 && cell.getCellNumber() <= 24) {

                tempX = xS + widthCell + (cell.getCellNumber() - 12) * widthCell - (widthCell - widthMidCell) + 0.1 * widthCell;
                tempY = yS + widthCell / 2 + 2 * hightCell - 0.05 * hightCell - 0.8 * widthCell - i * (hightCell - 0.05 * hightCell - 0.8 * widthCell - 0.1 * hightCell) / cell.getListCheckers().size();

            }

            checker.getChecker().setFrame(tempX, tempY, 0.8 * widthCell, 0.8 * widthCell);

        }

    }

    public Rectangle2D[] getMidboardCells() {
        return midboardCells;
    }

    public Rectangle2D[] getSidePanel() {
        return sidePanel;
    }

    public Cell[] getBoardCells() {
        return boardCells;
    }

    public double getWidthCell() {
        return widthCell;
    }

    public double getHightCell() {
        return hightCell;
    }

    public double getxS() {
        return xS;
    }

    public double getWidthMidCell() {
        return widthMidCell;
    }

    public double getyS() {
        return yS;
    }
}
