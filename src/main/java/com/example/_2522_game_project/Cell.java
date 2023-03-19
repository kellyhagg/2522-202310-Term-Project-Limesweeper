package com.example._2522_game_project;

/**
 * @author Eunjeong (Alice) Hur, Kelly Hagg
 * @version 1
 */
public class Cell {
    private StateType state;
    private boolean isLime;
    private int neighbourLimes;

    public Cell(StateType state, boolean isLime) {
        this.state = state;
        this.isLime = isLime;
        this.neighbourLimes = 0;
    }

    public void openCell() {
        if (this.isLime) {
            /* TODO: End game */
        }
        this.setState(StateType.OPENED);

        /* TODO: open neighbourCell and display the number */
    }



    public boolean isLime() {
        return isLime;
    }

    public StateType getState() {
        return state;
    }

    public int getNeighbourLimes() {
        return neighbourLimes;
    }

    public void setNeighbourLimes(int neighbourLimes) {
        this.neighbourLimes = neighbourLimes;
    }

    public void setState(StateType state) {
        this.state = state;
    }
}