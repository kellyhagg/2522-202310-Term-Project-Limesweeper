package com.example._2522_game_project;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * The cell in a board.
 *
 * @author Eunjeong (Alice) Hur, Kelly Hagg
 * @version 230321
 */
public class Cell extends StackPane {
    public static final int CELL_SIZE = 24;
    public static final int PANE_SIZE = 27;
    public final Rectangle outline = new Rectangle(CELL_SIZE, CELL_SIZE);
    private final int row;
    private final int column;
    private boolean isLime;
    private StateType state;
    private int neighbourLimes;
    private Text value = new Text();

    public Cell(int column, int row) {
        this.row = row;
        this.column = column;
        this.isLime = false;
        this.state = StateType.UNOPENED;

        // Set the rectangle outline to green and add to each cell
        outline.setFill(Color.LIMEGREEN);
        outline.setStroke(Color.GREEN);
        getChildren().addAll(outline);
        setTranslateY(row * PANE_SIZE + 1);
        setTranslateX(column * PANE_SIZE + 1);

        setOnMouseClicked(cell -> open());
    }

    public void open() {
        this.state = StateType.OPENED;
        outline.setFill(Color.DARKGREEN);
    }

    public void flag() {
        this.state = StateType.FLAGGED;
        value.setText("Y");
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

    public void setNeighbourLimes(int neighbourLimes) { this.neighbourLimes = neighbourLimes; }
}
