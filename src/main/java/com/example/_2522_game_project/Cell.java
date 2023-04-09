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
    /**
     * The size of each Cell.
     */
    public static final int CELL_SIZE = 24;
    /**
     * The size of each Cell Pane.
     */
    public static final int PANE_SIZE = 27;
    /**
     * The outline graphic of the cell.
     */
    public final Rectangle outline = new Rectangle(CELL_SIZE, CELL_SIZE);
    // The row of the Cell
    private final int row;
    // The column of the Cell
    private final int column;
    // The boolean representing if the Cell is a lime
    private boolean isLime;
    // The state of the cell either OPENED, UNOPENED, FLAGGED, or LOCKED
    private StateType state;
    // The String ID tag of the current flag graphic
    private String flagID;
    // The number of lime Cells surrounding the Cell
    private int neighbourLimes;


    /**
     * Instantiates a new Cell.
     *
     * @param column the column.
     * @param row    the row.
     * @param flagID the flag id.
     */
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
                open();
                increaseCounter();
            } else if (btn == MouseButton.SECONDARY && state != StateType.LOCKED && state != StateType.OPENED) {
                boolean flagged = state == StateType.FLAGGED;
                flag(flagged);
                LimesweeperApplication.checkWin();
            }
        });
    }

    /**
     * Sets the flagID String.
     *
     * @param flagID the flagID as a String.
     */
    protected void setFlagID(final String flagID) {
        this.flagID = flagID;
    }

    /**
     * Sets the outline.
     *
     * @param color the Color of the outline.
     */
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

    /*
    * Increase the number of limes found by 1.
    */
    private void increaseCounter() {
        int counter = LimesweeperApplication.getCounter();
        LimesweeperApplication.setCounter(counter + 1);
        LimesweeperApplication.checkWin();
    }

    /**
     * Opens the neighbour Cell.
     */
    protected void openNeighbour() {
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

    /**
     * Changes the Cell state to OPEN and updates the graphics to match.
     */
    protected void open() {
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

    /**
     * Changes the Cell state to FLAGGED or UNOPENED and updates the graphics to match depending on the boolean passed.
     *
     * @param flagged the flagged
     */
    protected void flag(final boolean flagged) {
        this.state = StateType.FLAGGED;
        if (!flagged) {
            Image image = null;
            try {
                image = new Image(Objects.requireNonNull(
                        LimesweeperApplication.class.getResource(flagID + "_flag.png")).openStream());
            } catch (IOException e) {
                System.out.println("Image attempting to be loaded cannot be found");
            }
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

    /**
     * Returns true if the Cell is a lime, else false.
     *
     * @return the boolean
     */
    protected boolean isLime() {
        return isLime;
    }

    /**
     * Sets the isLime boolean.
     *
     * @param lime is a boolean.
     */
    protected void setLime(final boolean lime) {
        isLime = lime;
    }

    /**
     * Gets the state of the Cell as a StateType.
     *
     * @return the state of the Cell as a StateType.
     */
    protected StateType getState() {
        return state;
    }

    /**
     * Sets the state of the Cell as a StateType.
     *
     * @param state the state of the Cell as a StateType.
     */
    protected void setState(final StateType state) {
        this.state = state;
    }

    /**
     * Sets the number of neighbour limes.
     *
     * @param neighbourLimes is an integer of the number of neighbour limes.
     */
    protected void setNeighbourLimes(final int neighbourLimes) { this.neighbourLimes = neighbourLimes; }

    /**
     * Gets the number of neighbour limes.
     *
     * @return the number of neighbour limes.
     */
    protected int getNeighbourLimes() {
        return this.neighbourLimes;
    }

    /**
     * Gets the row of the Cell.
     *
     * @return the row of the Cell.
     */
    protected int getRow() {
        return this.row;
    }

    /**
     * Gets the column of the Cell.
     *
     * @return the column of the Cell.
     */
    protected int getColumn() {
        return this.column;
    }
}
