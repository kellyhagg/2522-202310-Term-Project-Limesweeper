package com.example._2522_game_project;

import java.io.IOException;
import java.util.Random;

/**
 * The game board.
 *
 * @author kellyhagg, EunjeongHur
 * @version 230408
 */
public class Board {
    private static Cell[][] copiedGrid;
    private static int currentColumn;
    private static int currentRow;
    private final Cell[][] boardGrid;
    private final int columns;
    private final int rows;
    private final int numLimes;

    public Board(final int columns, final int rows, final int numLimes, final String flagID) {
        this.boardGrid = new Cell[columns][rows];
        copiedGrid = this.boardGrid;
        this.columns = columns;
        this.rows = rows;
        currentRow = rows;
        currentColumn = columns;
        this.numLimes = numLimes;
        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                Cell cell = new Cell(column, row, flagID);
                boardGrid[column][row] = cell;
            }
        }
        populateLimes(columns, rows, numLimes);
        setNeighbourLimes();
    }

    private void populateLimes(final int totalColumns, final int totalRows, final int totalNumLimes) {
        Random rand = new Random();
        int index = 0;
        while (index < totalNumLimes) {
            int column = rand.nextInt(totalColumns);
            int row = rand.nextInt(totalRows);
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
                } else {
                    count = checkMiddle(row, col); // Check Middle
                }
                boardGrid[col][row].setNeighbourLimes(count);
            }
        }
    }

    private int checkLeftLine(final int row) {
        int count = 0;
        int[] point = new int[] {-1, 0, -1, 1, 0, 1, 1, 1, 1, 0};
        int[] leftTop = new int[] {0, 1, 1, 1, 1, 0};
        int[] leftBottom = new int[] {-1, 0, -1, 1, 0, 1};
        if (row == 0) {
            for (int index = 0; index < leftTop.length - 1; index++) {
                if (boardGrid[leftTop[index + 1]][row + leftTop[index]].isLime()) {
                    count += 1;
                }
                index++;
            }
        } else if (row == this.rows - 1) {
            for (int index = 0; index < leftBottom.length - 1; index++) {
                if (boardGrid[leftBottom[index + 1]][row + leftBottom[index]].isLime()) {
                    count += 1;
                }
                index++;
            }
        } else {
            for (int index = 0; index < point.length - 1; index++) {
                if (boardGrid[point[index + 1]][row  + point[index]].isLime()) {
                    count += 1;
                }
                index++;
            }
        }
        return count;
    }

    private int checkRightLine(final int row) {
        int count = 0;
        int[] point = new int[] {0, -1, -1, -1, -1, 0, -1, 1, 0, 1};
        int[] rightTop = new int[] {-1, 0, -1, 1, 0, 1};
        int[] rightBottom = new int[] {0, -1, -1, -1, -1, 0};

        if (row == 0) {
            for (int index = 0; index < rightTop.length - 1; index++) {
                if (boardGrid[this.columns - 1 + rightTop[index]][rightTop[index + 1]].isLime()) {
                    count += 1;
                }
                index++;
            }
        } else if (row == this.rows - 1) {
            for (int index = 0; index < rightBottom.length - 1; index++) {
                if (boardGrid[this.columns - 1 +  rightBottom[index]][row + rightBottom[index + 1]].isLime()) {
                    count += 1;
                }
                index++;
            }
        } else {
            for (int index = 0; index < point.length - 1; index++) {
                if (boardGrid[this.columns - 1 + point[index]][row + point[index + 1]].isLime()) {
                    count += 1;
                }
                index++;
            }
        }
        return count;
    }

    private int checkMiddle(final int row, final int col) {
        int count = 0;
        int[] points = new int[] {-1, -1, -1, 0, -1, 1, 0, -1, 0, 1, 1, -1, 1, 0, 1, 1};
        int[] firstRow = new int[] {-1, 0, -1, 1, 0, 1, 1, 0, 1, 1};
        int[] lastRow = new int[] {-1, 0, -1, -1, 0, -1, 1, -1, 1, 0};

        if (row == 0) {
            for (int index = 0; index < firstRow.length - 1; index++) {
                if (boardGrid[col + firstRow[index]][row + firstRow[index + 1]].isLime()) {
                    count += 1;
                }
                index++;
            }
        } else if (row == this.rows - 1) {
            for (int index = 0; index < lastRow.length - 1; index++) {
                if (boardGrid[col + lastRow[index]][row + lastRow[index + 1]].isLime()) {
                    count += 1;
                }
                index++;
            }
        } else {
            for (int index = 0; index < points.length - 1; index++) {
                if (boardGrid[col + points[index]][row + points[index + 1]].isLime()) {
                    count += 1;
                }
                index++;
            }
        }
        return count;
    }

    private static int[][] getNeighbours(final Cell cell) {
        int[] points = new int[] {-1, -1, -1, 0, -1, 1, 0, -1, 0, 1, 1, -1, 1, 0, 1, 1};

        int[][] neighbours = new int[points.length / 2][];
        int index = 0;
        for (int point = 0; point < points.length; point++) {
            int dx = points[point];
            int dy = points[++point];

            int newX = cell.getColumn() + dx;
            int newY = cell.getRow() + dy;

            if (newX >= 0 && newX < currentColumn && newY >= 0 && newY < currentRow) {
                neighbours[index] = new int[] {newX, newY};
                index += 1;
            }
        }
        return neighbours;
    }

    public static void openNeighborCells(final Cell cell) throws IOException {
        int xCoordinate;
        int yCoordinate;
        if (cell.getNeighbourLimes() == 0 && !cell.isLime()) {
            int[][] neighbours = getNeighbours(cell);
            for(int[] neighbour: neighbours) {
                if (neighbour != null) {
                    xCoordinate = neighbour[0];
                    yCoordinate = neighbour[1];
                    if (copiedGrid[xCoordinate][yCoordinate].getState() == StateType.UNOPENED) {
                        copiedGrid[xCoordinate][yCoordinate].openNeighbour();
                        openNeighborCells(copiedGrid[xCoordinate][yCoordinate]);
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
