package org.example.gridpane;

public class Cell {

    private boolean isAlive;

    public Cell() {
        this.isAlive = false;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void toggleState() {
        isAlive = !isAlive;
    }



}
