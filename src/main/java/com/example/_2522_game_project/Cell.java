package com.example._2522_game_project;

/**
 * @author Eunjeong (Alice) Hur, Kelly Hagg
 * @version 1
 */
public class Cell {
    private StateType state;
    private boolean isLime;
    private int  neighbourLimes;

    public Cell(StateType state, boolean isLime) {
        this.state = state;
        this.isLime = isLime;
    }
}
