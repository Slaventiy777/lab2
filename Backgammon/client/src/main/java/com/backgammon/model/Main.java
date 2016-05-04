package com.backgammon.model;

import com.backgammon.view.BackgammonBoardView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        //ChessBoardController chessBoardController = new ChessBoardController();
        JFrame backgammonBoardView = new BackgammonBoardView();
        backgammonBoardView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        backgammonBoardView.pack();
        backgammonBoardView.setVisible(true);
        //chessBoardController.setChessBoardView(chessBoardView);

    }

}
