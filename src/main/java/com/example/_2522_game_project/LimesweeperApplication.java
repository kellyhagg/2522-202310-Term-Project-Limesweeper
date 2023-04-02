package com.example._2522_game_project;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Timer;

/**
 * Utility class to run the Limesweeper application.
 *
 * @author Eunjeong (Alice) Hur, Kelly Hagg
 * @version 230321
 */
public class LimesweeperApplication extends Application {
    static Board board;
    Timer timer;
    int counter;
    public static final int EASY_COLUMNS_ROWS = 10;
    public static final int MEDIUM_COLUMNS_ROWS = 16;
    public static final int HARD_COLUMNS_ROWS = 24;
    public static final int PANE_SIZE = 27;

    public static void youLose() throws IOException {
        revealAllLimes();
    }

    private static void revealAllLimes() throws IOException {
        for (Cell[] cells : board.getBoardGrid()) {
            for (Cell cell : cells) {
                if (cell.isLime() && cell.getState() != StateType.FLAGGED) {
                    Image image = new Image(Objects.requireNonNull(
                            LimesweeperApplication.class.getResource("lime.png")).openStream());
                    ImageView limeView = new ImageView(image);
                    limeView.setFitWidth(Cell.CELL_SIZE - 1);
                    limeView.setFitHeight(Cell.CELL_SIZE - 1);
                    cell.getChildren().add(limeView);
                }
                cell.setState(StateType.LOCKED);
            }
        }
    }
    private Pane createContentPane(Stage stage, final Difficulty difficulty) throws IOException {
        Pane pane = new Pane();
        final int barHeight = 60;
        final int easyNumLimes = 10, mediumNumLimes = 40, hardNumLimes = 99;
        switch (difficulty) {
            case EASY -> {
                board = new Board(EASY_COLUMNS_ROWS, EASY_COLUMNS_ROWS, easyNumLimes);
                pane.setPrefSize(EASY_COLUMNS_ROWS * PANE_SIZE, EASY_COLUMNS_ROWS * PANE_SIZE + barHeight);
            }
            case HARD -> {
                board = new Board(EASY_COLUMNS_ROWS, EASY_COLUMNS_ROWS, hardNumLimes);
                pane.setPrefSize(EASY_COLUMNS_ROWS * PANE_SIZE, EASY_COLUMNS_ROWS * PANE_SIZE + barHeight);
            }
            default -> {
                board = new Board(MEDIUM_COLUMNS_ROWS, MEDIUM_COLUMNS_ROWS, mediumNumLimes);
                pane.setPrefSize(MEDIUM_COLUMNS_ROWS * PANE_SIZE, MEDIUM_COLUMNS_ROWS * PANE_SIZE + barHeight);
            }
        }
        pane.setStyle("-fx-background-color: rgb(134,183,62);");
        return addContent(stage, pane, difficulty);
    }

    private void checkNumOfFlags() {
        Cell[][] boardGrid = board.getBoardGrid();
        for (int column = 0; column < board.getColumns(); column++) {
            for (int row = 0; row < board.getRows(); row++) {
                if (boardGrid[column][row].getState() == StateType.FLAGGED) {
                    this.counter += 1;
                }
            }
        }
    }

    private void reset(Stage stage) throws Exception {
        startGame(stage);
    }

    private Pane addContent(Stage stage, final Pane pane, final Difficulty difficulty) throws IOException {
        Cell[][] boardGrid = board.getBoardGrid();
        for (int columns = 0; columns < board.getColumns(); columns++) {
            for (int rows = 0; rows < board.getRows(); rows++) {
                pane.getChildren().add(boardGrid[columns][rows]);
            }
        }
        Pane resetBtn = new StackPane();
        resetBtn.setPrefSize(50, 50);
        Image image = new Image(Objects.requireNonNull(
                LimesweeperApplication.class.getResource("reset.png")).openStream());
        ImageView resetView = new ImageView(image);
        resetView.setFitHeight(50);
        resetView.setFitWidth(50);
        resetBtn.getChildren().add(resetView);
        resetBtn.setTranslateX(MEDIUM_COLUMNS_ROWS * PANE_SIZE / 2.0  - 25);
        resetBtn.setTranslateY(MEDIUM_COLUMNS_ROWS * PANE_SIZE + 4);
        resetBtn.setOnMouseClicked(t -> {
            MouseButton btn = t.getButton();
            if (btn == MouseButton.PRIMARY) {
                try {
                    reset(stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        pane.getChildren().add(resetBtn);
        return pane;
    }

    public void startGame(Stage stage) throws Exception{
        Scene scene = new Scene(createContentPane(stage, Difficulty.MEDIUM));
        stage.setTitle("Limesweeper");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void start(final Stage primarystage) throws Exception {
        startGame(primarystage);
    }

    public static void main(final String[] args) {
        launch();
    }
}