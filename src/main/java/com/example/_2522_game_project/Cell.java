package com.example._2522_game_project;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell extends StackPane {
    public static final int CELL_SIZE = 24;
    public static final int PANE_SIZE = 27;
    public Rectangle outline = new Rectangle(CELL_SIZE, CELL_SIZE);
    private final int row;
    private final int column;
    private final boolean isLime;

    private StateType state;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        this.isLime = false;
        this.state = StateType.UNOPENED;

        outline.setStroke(Color.LIGHTGREEN);

        getChildren().addAll(outline);

        setTranslateX(row * PANE_SIZE);
        setTranslateY(column * PANE_SIZE);
    }


}
