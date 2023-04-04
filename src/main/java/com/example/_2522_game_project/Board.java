package com.example._2522_game_project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private final Cell[][] boardGrid;
    private static Cell[][] copiedGrid;
    private static int ROW;
    private static int COLUMN;
    private final int numLimes;

    public Board(final int columns, final int rows, final int numLimes) {
        this.boardGrid = new Cell[columns][rows];
        copiedGrid = this.boardGrid;
        this.columns = columns;
        this.rows = rows;
        ROW = rows;
        COLUMN = columns;
        this.numLimes = numLimes;
        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                Cell cell = new Cell(column, row);
                boardGrid[column][row] = cell;
            }
        }
        populateLimes(columns, rows, numLimes);
        setNeighbourLimes();
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
                else {
                    count = checkMiddle(row, col); // Check Middle
                }
                boardGrid[col][row].setNeighbourLimes(count);
            }
        }
    }

    private int checkLeftLine(int row) {
        int count = 0;
        int [] point = new int[] {-1, 0, -1, 1, 0, 1, 1, 1, 1, 0};
        int[] leftTop = new int[] {0, 1, 1, 1, 1, 0};
        int [] leftBottom = new int[] {-1, 0, -1, 1, 0, 1};
        if (row == 0) {
            for (int i = 0; i < leftTop.length - 1; i++) {
                if (boardGrid[leftTop[i+1]][row + leftTop[i]].isLime()) {
                    count += 1;
                }
                i += 1;
            }
        } else if (row == this.rows -1) {
            for (int i = 0; i < leftBottom.length - 1; i++) {
                if (boardGrid[leftBottom[i+1]][row + leftBottom[i]].isLime()) {
                    count += 1;
                }
                i += 1;
            }
        } else {
            for (int i = 0; i < point.length - 1; i++) {
                if (boardGrid[point[i+1]][row  + point[i]].isLime()) {
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
        int[] points = new int[] {-1, -1, -1, 0, -1, 1, 0, -1, 0, 1, 1, -1, 1, 0, 1, 1};
        int[] firstRow = new int[] {-1, 0, -1, 1, 0, 1, 1, 0, 1, 1};
        int [] lastRow = new int[] {-1, 0, -1, -1, 0, -1, 1, -1, 1, 0};

        if (row == 0) {
            for (int i = 0; i < firstRow.length - 1; i++) {
                if (boardGrid[col + firstRow[i]][row + firstRow[i+1]].isLime()) {
                    count += 1;
                }
                i += 1;
            }
        } else if (row == this.rows - 1) {
            for (int i = 0; i < lastRow.length - 1; i++) {
                if (boardGrid[col + lastRow[i]][row + lastRow[i+1]].isLime()) {
                    count += 1;
                }
                i += 1;
            }
        } else {
            for (int i = 0; i < points.length - 1; i++) {
                if (boardGrid[col + points[i]][row + points[i+1]].isLime()) {
                    count += 1;
                }
                i += 1;
            }
        }
        return count;
    }

    private static int[][] getNeighbours(Cell cell) {
        int[] points = new int[] {-1, -1, -1, 0, -1, 1, 0, -1, 0, 1, 1, -1, 1, 0, 1, 1};

        int[][] neighbours = new int[points.length / 2][];
        int index = 0;
        for (int i = 0; i < points.length; i ++) {
            int dx = points[i];
            int dy = points[++i];

            int newX = cell.getColumn() + dx;
            int newY = cell.getRow() + dy;

            if (newX >= 0 && newX < COLUMN && newY >= 0 && newY < ROW) {
                neighbours[index] = new int[] {newX, newY};
                index += 1;
            }
        }
        return neighbours;
    }

    public static void openNeighborCells(Cell cell) throws IOException {
        int x, y;
        if (cell.getNeighbourLimes() == 0 && !cell.isLime()) {
            int[][] neighbours = getNeighbours(cell);
            for(int[] neighbour: neighbours) {
                if (neighbour != null) {
                    x = neighbour[0];
                    y = neighbour[1];
                    if (copiedGrid[x][y].getState() == StateType.UNOPENED) {
                        copiedGrid[x][y].openNeighbour();
                        openNeighborCells(copiedGrid[x][y]);
                    }
                }
            }
        }
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

    public int getNumLimes() {
        return numLimes;
    }
}
