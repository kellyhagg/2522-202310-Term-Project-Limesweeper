package com.example._2522_game_project;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The cell in a board.
 *
 * @author Eunjeong (Alice) Hur, Kelly Hagg
 * @version 230321
 */
public class Cell extends StackPane {
    public static final int CELL_SIZE = 24;
    public static final int PANE_SIZE = 27;
    public static final Rectangle outline = new Rectangle(CELL_SIZE, CELL_SIZE);
    private final int row;
    private final int column;
    private boolean isLime;
    private StateType state;

    public Cell(int column, int row) {
        this.row = row;
        this.column = column;
        this.isLime = false;
        this.state = StateType.UNOPENED;

        // Set the rectangle outline to green and add to each cell
        outline.setStroke(Color.LIGHTGREEN);
        getChildren().addAll(outline);
        setTranslateY(row * PANE_SIZE + 1);
        setTranslateX(column * PANE_SIZE + 1);
    }

    public boolean isLime() {
        return isLime;
    }

    public void setLime(boolean lime) {
        isLime = lime;
    }

    public StateType getState() {
        return state;
    }

    public void setState(StateType state) {
        this.state = state;
    }
}
