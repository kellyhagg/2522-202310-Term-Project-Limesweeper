package com.example._2522_game_project;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Random;

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
    private int neighbourLimes = 5; // temporarily set to 5 for testing purposes


    public Cell(int column, int row) {
        this.row = row;
        this.column = column;
        this.isLime = false;
        this.state = StateType.UNOPENED;

        // Set the rectangle outline to green and add to the content pane
        outline.setFill(Color.rgb(221,232,164));
        outline.setStroke(Color.rgb(134,183,62));
        getChildren().addAll(outline);
        setTranslateY(row * PANE_SIZE + 1);
        setTranslateX(column * PANE_SIZE + 1);

        setOnMouseClicked(t -> {
            MouseButton btn = t.getButton();
            if (btn == MouseButton.PRIMARY && state == StateType.UNOPENED) {
                try {
                    open();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (btn == MouseButton.SECONDARY && state != StateType.LOCKED && state != StateType.OPENED) {
                boolean flagged = state == StateType.FLAGGED;
                try {
                    flag(flagged);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void open() throws IOException {
        if (state != StateType.LOCKED) {
            Text text = new Text(String.valueOf(neighbourLimes));
            final int r = 159 + (16 * neighbourLimes);
            final int g = 207 + (8 * neighbourLimes);
            final int b = 87 + (28 * neighbourLimes);
            this.state = StateType.OPENED;
            outline.setFill(Color.rgb(107,146,47));
            text.setFont(Font.font("Impact", 19));
            text.setFill(Color.rgb(r, g, b));
            text.setVisible(true);
            getChildren().add(text);
            if (isLime()) {
                LimesweeperApplication.youLose();
            }
        }
    }

    public void flag(final boolean flagged) throws IOException {
        this.state = StateType.FLAGGED;
        Image image = new Image(LimesweeperApplication.class.getResource("flag.png").openStream());
        ImageView flagView = new ImageView(image);
        flagView.setFitWidth(CELL_SIZE - 1);
        flagView.setFitHeight(CELL_SIZE - 1);
        if (!flagged) {
            getChildren().add(flagView);
        } else {
            this.state = StateType.UNOPENED;
            getChildren().clear();
            outline.setFill(Color.rgb(221,232,164));
            outline.setStroke(Color.rgb(134,183,62));
            getChildren().addAll(outline);
        }
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
