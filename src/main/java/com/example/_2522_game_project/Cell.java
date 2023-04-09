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
import java.util.Objects;

/**
 * The Cell of a Board.
 *
 * @author kellyhagg, EunjeongHur
 * @version 230408
 */
public class Cell extends StackPane {
    public static final int CELL_SIZE = 24;
    public static final int PANE_SIZE = 27;
    public final Rectangle outline = new Rectangle(CELL_SIZE, CELL_SIZE);
    private final int row;
    private final int column;
    private boolean isLime;
    private StateType state;
    private String flagID;
    private int neighbourLimes;


    public Cell(final int column, final int row, final String flagID) {
        this.row = row;
        this.column = column;
        this.isLime = false;
        this.state = StateType.UNOPENED;
        this.flagID = flagID;

        // Set the rectangle outline to green and add to the content pane
        outline.setFill(LimesweeperApplication.LIGHT_GREEN);
        outline.setStroke(LimesweeperApplication.BACKGROUND_GREEN);
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

    protected void setFlagID(final String flagID) {
        this.flagID = flagID;
    }

    protected void setOutline(final Color color) {
        this.outline.setStroke(color);
    }

    private void setText() {
        if (neighbourLimes != 0) {
            final int fontSize = 19;
            Text text = new Text(String.valueOf(neighbourLimes));
            final int r = 159 + (16 * neighbourLimes);
            final int g = 207 + (8 * neighbourLimes);
            final int b = 87 + (28 * neighbourLimes);
            text.setFont(Font.font("Impact", fontSize));
            text.setFill(Color.rgb(r, g, b));
            text.setVisible(true);
            getChildren().add(text);
        }
    }

    private void increaseCounter() throws IOException {
        int counter = LimesweeperApplication.getCounter();
        LimesweeperApplication.setCounter(counter + 1);
        LimesweeperApplication.checkWin();
    }

    protected void openNeighbour() throws IOException {
        if (neighbourLimes != 0) {
            setText();
        }
        this.state = StateType.OPENED;
        increaseCounter();
        outline.setFill(LimesweeperApplication.OPEN_GREEN);
        if (isLime()) {
            LimesweeperApplication.youLose();
        }
    }

    protected void open() throws IOException {
        if (state != StateType.LOCKED) {
            setText();
            this.state = StateType.OPENED;
            outline.setFill(LimesweeperApplication.OPEN_GREEN);
            if (isLime()) {
                LimesweeperApplication.youLose();
            }
            Board.openNeighborCells(this);
        }
    }

    protected void flag(final boolean flagged) throws IOException {
        this.state = StateType.FLAGGED;
        if (!flagged) {
            Image image = new Image(Objects.requireNonNull(
                    LimesweeperApplication.class.getResource(flagID + "_flag.png")).openStream());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(CELL_SIZE - 1);
            imageView.setFitHeight(CELL_SIZE - 1);
            getChildren().add(imageView);
            LimesweeperApplication.decreaseFlags();
            int counter = LimesweeperApplication.getCounter();
            LimesweeperApplication.setCounter(counter + 1);
        } else {
            LimesweeperApplication.increaseFlags();
            this.state = StateType.UNOPENED;
            int counter = LimesweeperApplication.getCounter();
            LimesweeperApplication.setCounter(counter - 1);
            getChildren().clear();
            outline.setFill(LimesweeperApplication.LIGHT_GREEN);
            outline.setStroke(LimesweeperApplication.BACKGROUND_GREEN);
            getChildren().addAll(outline);
        }
    }

    protected boolean isLime() {
        return isLime;
    }

    protected void setLime(final boolean lime) {
        isLime = lime;
    }

    protected StateType getState() {
        return state;
    }

    protected void setState(final StateType state) {
        this.state = state;
    }

    protected void setNeighbourLimes(final int neighbourLimes) { this.neighbourLimes = neighbourLimes; }
    protected int getNeighbourLimes() {
        return this.neighbourLimes;
    }

    protected int getRow() {
        return this.row;
    }

    protected int getColumn() {
        return this.column;
    }
}
