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
        setNeighbourLimes();
        System.out.println("testing");
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
        for (int col = 0; col < this.columns; col++) {
            for (int row = 0; row < this.rows; row++) {

                if (boardGrid[col][row].isLime()) {
                    continue;
                }
                int count;
                if (col == 0) { // Check Left Line
                    count = checkLeftLine(row);
                } else if (col == this.columns-1) { //Check Right Line
                    count = checkRightLine(row);
                }
//                else {
//                    count = checkMiddle(col, row); // Check Middle
//                }
//                boardGrid[row][col].setNeighbourLimes(count);
            }
        }
    }

    private int checkLeftLine(int col) {
        int count = 0;
        int [] point = new int[] {-1, 0, -1, 1, 0, 1, 1, 1, 1, 0};
        int[] leftTop = new int[] {0, 1, 1, 1, 1, 0};
        int [] leftBottom = new int[] {-1, 0, -1, 1, 0, 1};
        if (col == 0) {
            for (int i = 0; i < leftTop.length - 1; i++) {
                if (boardGrid[col + leftTop[i]][leftTop[i+1]].isLime()) {
                    count += 1;
                }
                i += 1;
            }
        } else if (col == this.rows -1) {
            for (int i = 0; i < leftBottom.length - 1; i++) {
                if (boardGrid[col + leftBottom[i]][leftBottom[i+1]].isLime()) {
                    count += 1;
                }
                i += 1;
            }
        } else {
            for (int i = 0; i < point.length - 1; i++) {
                if (boardGrid[col  + point[i]][point[i+1]].isLime()) {
                    count += 1;
                }
                i += 1;
            }
        }
        return count;
    }

    private int checkRightLine(int row) {

        int count = 0;
        int [] point = new int[] {0, -1, -1, -1, -1, 0, -1, 1, 0, 1};
        int[] rightTop = new int[] {-1, 0, -1, 1, 0, 1};
        int [] rightBottom = new int[] {0, -1, -1, -1, -1, 0};

        if (row == 0) {
            for (int i = 0; i < rightTop.length - 1; i++) {
                if (boardGrid[this.columns - 1 + rightTop[i]][rightTop[i+1]].isLime()) {
                    count += 1;
                }
                i += 1;
            }
        } else if (row == this.rows-1) {
            for (int i = 0; i < rightBottom.length - 1; i++) {
                if (boardGrid[this.columns - 1 +  rightBottom[i]][row + rightBottom[i+1]].isLime()) {
                    count += 1;
                }
                i += 1;
            }
        } else {
            for (int i = 0; i < point.length - 1; i++) {
                if (boardGrid[this.columns - 1 + point[i]][row + point[i+1]].isLime()) {
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
