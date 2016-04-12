package com.backgammon.model;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Checker {

    private Color checkerColor;
    Ellipse2D checker;

    public Checker(Color cellColor, Ellipse2D checker) {
        this.checkerColor = cellColor;
        this.checker = checker;
    }

    public Color getCheckerColor() {
        return checkerColor;
    }

    public Ellipse2D getChecker() {
        return checker;
    }
}
