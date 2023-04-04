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
    private String flagID = "white";
    private int neighbourLimes; // temporarily set to 5 for testing purposes


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
                    increaseCounter();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (btn == MouseButton.SECONDARY && state != StateType.LOCKED && state != StateType.OPENED) {
                boolean flagged = state == StateType.FLAGGED;
                try {
                    flag(flagged);
                    LimesweeperApplication.checkWin();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void setOutline(Color color) {
        this.outline.setStroke(color);
    }

    public void setText() {
        if (neighbourLimes != 0) {
            Text text = new Text(String.valueOf(neighbourLimes));
            final int r = 159 + (16 * neighbourLimes);
            final int g = 207 + (8 * neighbourLimes);
            final int b = 87 + (28 * neighbourLimes);
            text.setFont(Font.font("Impact", 19));
            text.setFill(Color.rgb(r, g, b));
            text.setVisible(true);
            getChildren().add(text);
        }
    }

    private void increaseCounter() {
        LimesweeperApplication.counter += 1;
        LimesweeperApplication.checkWin();
    }

    public void openNeighbour() throws IOException {
        if (neighbourLimes != 0) {
            setText();
        }
        this.state = StateType.OPENED;
        increaseCounter();;
        outline.setFill(Color.rgb(107,146,47));
        if (isLime()) {
            LimesweeperApplication.youLose();
        }
    }

    public void open() throws IOException {
        if (state != StateType.LOCKED) {
            setText();
            this.state = StateType.OPENED;
            outline.setFill(Color.rgb(107,146,47));
            if (isLime()) {
                LimesweeperApplication.youLose();
            }
            Board.openNeighborCells(this);
        }
    }

    public void flag(final boolean flagged) throws IOException {
        this.state = StateType.FLAGGED;
        if (!flagged) {
            getChildren().add(LimesweeperApplication
                    .makeImageView(flagID + "_flag.png", CELL_SIZE - 1, CELL_SIZE - 1));
            LimesweeperApplication.decreaseFlags();
            LimesweeperApplication.counter += 1;
        } else {
            LimesweeperApplication.increaseFlags();
            this.state = StateType.UNOPENED;
            LimesweeperApplication.counter -= 1;
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
    public int getNeighbourLimes() {
        return this.neighbourLimes;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }
}
