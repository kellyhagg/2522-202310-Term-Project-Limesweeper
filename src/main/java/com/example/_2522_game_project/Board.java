package com.example._2522_game_project;

import java.util.Random;

/**
 * @author Eunjeong (Alice) Hur, Kelly Hagg
 * @version 1
 */
public class Board {
    private final int row;
    private final int col;
    private Cell[][] board;
    private int numLimes;
    private static int totalCreatedLimes = 0;

    public Board(int row, int col) {
        this.row = row;
        this.col = col;
        this.board = new Cell[row][col];

        int totalCells = row * col;
        /*TODO: Check if totalCells is less than 225 (15 * 15) and greater than 64 (8*8). Otherwise, create an exception. ( Can be other number) */
        if (totalCells <= 100) {
            this.numLimes = 10;
        } else if (totalCells <= 150) {
            this.numLimes = 12;
        } else if (totalCells <= 200) {
            this.numLimes = 13;
        } else {
            this.numLimes = 15;
        }
    }

    public Board(int row, int col, int minLimes) {
        this.row = row;
        this.col = col;
        this.board = new Cell[row][col];

        int totalCells = row * col;
        /*TODO: if minLimes is greater than or equal to totalCells, create an exception. */
        this.numLimes = minLimes;
    }

    public void createBoard() {
        boolean isLime;
        for (int row = 0; row < this.row; row++) {
            for (int col = 0; col < this.col; col++) {
                isLime = this.populateLimes();
                this.board[row][col] = new Cell(StateType.UPOPENED, isLime);
            }
        }
    }

    private boolean populateLimes() {
        if (numLimes == 0) {
            return false;
        }

        Random rand = new Random();
        int random_num = rand.nextInt(5);
        if (random_num == 0 && totalCreatedLimes < numLimes) {
            totalCreatedLimes += 1;
            return true;
        }
        return false;
    }

    private void setNeighbourLimes() {

        for (int row = 0; row < this.row;  row ++) {
            for (int col = 0; col < this.col; col ++) {
                if (board[row][col].isLime()) {
                    continue;
                }
                int count = 0;
                if (row == 0) { // Check Top Line
                    count = checkTopLine(col);
                }
                /*TODO: Check Bottom Line and Check Middles */
                board[row][col].setNeighbourLimes(count);
            }
        }
    }

    private int checkTopLine(int col) {
        int count = 0;
        if (col == 0) { // When the cell is top-left corner
            if (board[row][col+1].isLime()) {
                count += 1;
            }
            if (board[row+1][col+1].isLime()) {
                count += 1;
            }
        } else if (col == this.col) { // When the cell is top-right corner
            if (board[row][col - 1].isLime()) {
                count += 1;
            }
            if (board[row+1][col-1].isLime()) {
                count += 1;
            }
        } else {
            if (board[row][col-1].isLime()) {
                count += 1;
            }
            if (board[row + 1][col - 1].isLime()) {
                count += 1;
            }
            if (board[row + 1][col + 1].isLime()) {
                count += 1;
            }
            if (board[row][col+1].isLime()) {
                count += 1;
            }
        }
        if (board[row + 1][col].isLime()) {
            count += 1;
        }

        return count;
    }
}