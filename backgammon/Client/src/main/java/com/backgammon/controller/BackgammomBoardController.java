package com.backgammon.controller;

import com.backgammon.model.BackgammonBoardModel;
import com.backgammon.model.Cell;
import com.backgammon.model.Checker;
import com.backgammon.view.BackgammonBoardView;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.JLabel;

public class BackgammomBoardController implements MouseListener, MouseMotionListener {

    private BackgammonBoardView boardView;
    private BackgammonBoardModel boardModel;

    private Cell previousCell;
    private Checker previousFigure;
    private Cell currentCell;

    public BackgammomBoardController(BackgammonBoardModel backgammonBoardModel, BackgammonBoardView backgammonBoardView) {
        this.boardModel = backgammonBoardModel;
        this.boardView = backgammonBoardView;

        init();
    }

    private void init(){
        boardModel.initBoard();
        boardModel.initFigurs();
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
