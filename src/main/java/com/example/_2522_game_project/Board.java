package com.example._2522_game_project;

import java.util.Random;

/**
 * The game board.
 *
 * @author Eunjeong (Alice) Hur, Kelly Hagg
 * @version 230321
 */
public class Board {
    public int EASY_ROWS = 9; // Kelly will move these/clean these up later
    public int EASY_COLUMNS = 9;
    public int MEDIUM_ROWS = 9;
    public int MEDIUM_COLUMNS = 9;
    public static final int HARD_COLUMNS = 30;
    public static final int HARD_ROWS = 16;
    private final int rows;
    private final int columns;
    private final Cell[][] boardGrid = new Cell[HARD_COLUMNS][HARD_ROWS];
    private final int numLimes;

    public Board(int columns, int rows, int numLimes) {
        this.columns = columns;
        this.rows = rows;
        this.numLimes = numLimes;
        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                Cell cell = new Cell(column, row);
                boardGrid[column][row] = cell;
            }
        }
        populateLimes(columns, rows, numLimes);
    }

    private void populateLimes(int columns, int rows, int numLimes) {
        Random rand = new Random();
        int index = 0;
        while (index < numLimes) {
            int column = rand.nextInt(columns);
            int row = rand.nextInt(rows);
            if (!boardGrid[column][row].isLime()) {
                boardGrid[column][row].setLime(true);
                index++;
            }
        }
    }

    private void setNeighbourLimes() {
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.columns; col++) {
                if (boardGrid[row][col].isLime()) {
                    continue;
                }
                int count;
                if (row == 0) { // Check Top Line
                    count = checkTopLine(col);
                } else if (row == this.rows-1) { //Check Bottom Line
                    count = checkBottomLine(col);
                } else {
                    count = checkMiddle(row, col); // Check Middle
                }
                boardGrid[row][col].setNeighbourLimes(count);
            }
        }
    }

    private int checkTopLine(int col) {
        int count = 0;
        int [] point = new int[] {0, -1, 1, -1, 1, 1, 0, 1, 1, 0};
        int[] firstCol = new int[] {0, 1, 1, 1, 1, 0};
        int [] lastCol = new int[] {0, -1, 1, -1, 1, 0};
        if (col == 0) { // When the cell is top-left corner
            for (int i = 0; i < firstCol.length - 1; i++) {
                if (boardGrid[firstCol[i]][col + firstCol[i+1]].isLime()) {
                    count += 1;
                }
                i += 1;
            }
        } else if (col == this.columns -1) { // When the cell is top-right corner
            for (int i = 0; i < lastCol.length - 1; i++) {
                if (boardGrid[lastCol[i]][col + lastCol[i+1]].isLime()) {
                    count += 1;
                }
                i += 1;
            }
        } else {
            for (int i = 0; i < point.length - 1; i++) {
                if (boardGrid[point[i]][col + point[i+1]].isLime()) {
                    count += 1;
                }
                i += 1;
            }
        }
        return count;
    }

    private int checkBottomLine(int col) {
        int count = 0;
        int [] point = new int[] {0, -1, -1, -1, -1, 0, -1, 1, 0, 1};
        int[] firstCol = new int[] {-1, 0, -1, 1, 0, 1};
        int [] lastCol = new int[] {-1, 0, -1, -1, 0, -1};

        if (col == 0) {
            for (int i = 0; i < firstCol.length - 1; i++) {
                if (boardGrid[this.rows - 1 + firstCol[i]][col + firstCol[i+1]].isLime()) {
                    count += 1;
                }
                i += 1;
            }
        } else if (col == this.columns-1) {
            for (int i = 0; i < lastCol.length - 1; i++) {
                if (boardGrid[this.rows - 1 + lastCol[i]][col + lastCol[i+1]].isLime()) {
                    count += 1;
                }
                i += 1;
            }
        } else {
            for (int i = 0; i < point.length - 1; i++) {
                if (boardGrid[this.rows - 1 + point[i]][col + point[i+1]].isLime()) {
                    count += 1;
                }
                i += 1;
            }
        }

        return count;
    }

    private int checkMiddle(int row, int col){
        int count = 0;
        int[] points = new int[] {
                -1, -1,
                -1, 0,
                -1, 1,
                0, -1,
                0, 1,
                1, -1,
                1, 0,
                1, 1
        };
        int[] firstCol = new int[] {
                -1, 0,
                -1, 1,
                0, 1,
                1, 0,
                1, 1
        };

        int [] lastCol = new int[] {
                -1, 0,
                -1, -1,
                0, -1,
                1, -1,
                1, 0
        };
        if (col == 0) {
            for (int i = 0; i < firstCol.length - 1; i++) {
                if (boardGrid[row + firstCol[i]][col + firstCol[i+1]].isLime()) {
                    count += 1;
                }
                i += 1;
            }
        } else if (col == this.columns - 1) {
            for (int i = 0; i < lastCol.length - 1; i++) {
                if (boardGrid[row + lastCol[i]][col + lastCol[i+1]].isLime()) {
                    count += 1;
                }
                i += 1;
            }
        } else {
            for (int i = 0; i < points.length - 1; i++) {
                if (boardGrid[row + points[i]][col + points[i+1]].isLime()) {
                    count += 1;
                }
                i += 1;
            }
        }

        return count;
    }


    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Cell[][] getBoardGrid() {
        return boardGrid;
    }
}
