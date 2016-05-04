package com.backgammon.view;


import com.backgammon.controller.BackgammomBoardController;
import com.backgammon.model.BackgammonBoardModel;
import com.backgammon.model.Cell;
import com.backgammon.model.Checker;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class BackgammonBoardView  extends JFrame {

    BackgammonBoardModel boardModel;

    public BackgammonBoardView() {

        boardModel = new BackgammonBoardModel();
        BackgammomBoardController motionHandler = new BackgammomBoardController(boardModel, this);
        addMouseListener(motionHandler);
        addMouseMotionListener(motionHandler);

    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        //g2.drawImage(BackgammonBoardModel.dice1, 300, 300, null);

        for(int i = 0; i < boardModel.getSidePanel().length; i++) {
            g2.setColor(BackgammonBoardModel.whiteColor);
            g2.fill(boardModel.getSidePanel()[i]);
        }

        g2.setColor(BackgammonBoardModel.brownBronzeColor);
        g2.setStroke(new BasicStroke(5));
        g2.draw(new Rectangle2D.Double(boardModel.getxS(), boardModel.getyS(), 13 * boardModel.getWidthCell() + boardModel.getWidthCell() + boardModel.getWidthMidCell(), 2 * boardModel.getHightCell() + boardModel.getWidthCell()));

        g2.setColor(BackgammonBoardModel.brownColor);
        g2.fill(boardModel.getMidboardCells()[0]);
        g2.fill(boardModel.getMidboardCells()[1]);

        g2.setColor(BackgammonBoardModel.whiteColor);
        g2.fill(boardModel.getMidboardCells()[2]);
        g2.fill(boardModel.getMidboardCells()[3]);

        for (Cell cell : boardModel.getBoardCells()) {

            if (cell.getCellNumber() % 2 == 0)
                g2.setColor(BackgammonBoardModel.brownBuffColor);
            else
                g2.setColor(BackgammonBoardModel.brownBronzeColor);

            g2.fill(cell.getBoardCell());

            g2.setColor(BackgammonBoardModel.whiteColor);
            g2.fill(cell.getTriangleIndicator());

            g2.setColor(BackgammonBoardModel.redColor);
            g2.fill(cell.getEllipseIndicator());

            g2.setColor(BackgammonBoardModel.blackColor);
            g2.setStroke(new BasicStroke(3));
            g2.draw(cell.getEllipseIndicator());

            for (Checker checker : cell.getListCheckers()) {

                g2.setColor(checker.getCheckerColor());
                g2.fill(checker.getChecker());

                if (checker.getCheckerColor() == BackgammonBoardModel.whiteColor)
                    g2.setColor(BackgammonBoardModel.blackColor);
                else if (checker.getCheckerColor() == BackgammonBoardModel.blackColor)
                    g2.setColor(BackgammonBoardModel.whiteColor);

                g2.setStroke(new BasicStroke(2));
                g2.draw(checker.getChecker());

            }

        }

    }

}
