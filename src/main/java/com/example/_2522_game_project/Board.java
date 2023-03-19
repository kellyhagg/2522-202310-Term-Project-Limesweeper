package com.example._2522_game_project;

<<<<<<< HEAD
/**
 * @author Eunjeong (Alice) Hur, Kelly Hagg
 * @version 1
 */
public class Board {
    private final int row;
    private final int col;
    private Cell[][] board;
    private int numLimes;

    public Board(int row, int col) {
        this.row = row;
        this.col = col;
        this.board = new Cell[row][col];

        int totalCells = row * col;
        /*TODO: Check if totalCells is less than 225 (15 * 15) and greater than 64 (8*8). Otherwise, create an exception. ( Can be other number) */
        if (totalCells <= 100) {
            this.numLimes = 10;
        } else if ( totalCells <= 150) {
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
        for (int row = 0; row < this.row; row ++) {
            for (int col = 0; col < this.col; col ++) {
                isLime = this.populateLimes();
                this.board[row][col] = new Cell(StateType.UPOPENED, isLime);
            }
        }
    }

    private boolean populateLimes() {
        if (numLimes == 0 ) {
            return false;
        }
        /* TODO: return true with 20%? chance. */
        /* TODO: need to think about the case that when it doesn't generate the specific number of limes */
        return true;
    }
=======
public class Board {
>>>>>>> origin/master
}
