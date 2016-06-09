package lab2.protocol.model;

import java.io.Serializable;

public class Checker implements Serializable {

    private static final long serialVersionUID = 1L;

    private ColorUser checkerColor;

    public Checker(ColorUser cellColor) {
        this.checkerColor = cellColor;
    }

    public ColorUser getCheckerColor() {
        return checkerColor;
    }

    public void setCheckerColor(ColorUser checkerColor) {
        this.checkerColor = checkerColor;
    }

}
