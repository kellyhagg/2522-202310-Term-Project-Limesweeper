package com.example._2522_game_project;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;

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

        // Set the rectangle outline to green and add to the content pane
        outline.setFill(Color.LIGHTGREEN);
        outline.setStroke(Color.GREEN);
        getChildren().addAll(outline);
        setTranslateY(row * PANE_SIZE + 1);
        setTranslateX(column * PANE_SIZE + 1);

        setOnMouseClicked(t -> {
            MouseButton btn = t.getButton();
            if (btn == MouseButton.PRIMARY) {
                open();
            } else if (btn == MouseButton.SECONDARY) {
                try {
                    flag();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void open() {
        this.state = StateType.OPENED;
        outline.setFill(Color.DARKGREEN);
        if(isLime()) {
            LimesweeperApplication.youLose();
        }
    }

    public void flag() throws IOException {
        this.state = StateType.FLAGGED;
        Image image = new Image(Objects.requireNonNull(
                LimesweeperApplication.class.getResource("flag.png")).openStream());
        ImageView flagView = new ImageView(image);
        flagView.setFitWidth(CELL_SIZE - 1);
        flagView.setFitHeight(CELL_SIZE - 1);
        getChildren().add(flagView);
        value.setText("Y");
    }

    public boolean isLime() {
        return isLime;
    }

    public void setLime(boolean lime) {
        isLime = lime;
    }

    public Text getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value.setText(value);
    }

    public StateType getState() {
        return state;
    }

    public void setState(StateType state) {
        this.state = state;
    }

    public void setNeighbourLimes(int neighbourLimes) { this.neighbourLimes = neighbourLimes; }
}
