package com.example._2522_game_project;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
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
    private Pane createContentPane(final Difficulty difficulty) {
        Pane pane = new Pane();
        final int factor = 27;
        final int barHeight = 48;
        final int easyColumnsRows = 10, mediumColumnsRows = 16, hardColumnsRows = 24;
        final int easyNumLimes = 10, mediumNumLimes = 40, hardNumLimes = 99;
        switch (difficulty) {
            case EASY -> {
                board = new Board(easyColumnsRows, easyColumnsRows, easyNumLimes);
                pane.setPrefSize(easyColumnsRows * factor, easyColumnsRows * factor + barHeight);
            }
            case HARD -> {
                board = new Board(hardColumnsRows, hardColumnsRows, hardNumLimes);
                pane.setPrefSize(hardColumnsRows * factor, hardColumnsRows * factor + barHeight);
            }
            default -> {
                board = new Board(mediumColumnsRows, mediumColumnsRows, mediumNumLimes);
                pane.setPrefSize(mediumColumnsRows * factor, mediumColumnsRows * factor + barHeight);
            }
        }
        pane.setStyle("-fx-background-color: rgb(134,183,62);");
        Cell[][] boardGrid = board.getBoardGrid();
        for (int columns = 0; columns < board.getColumns(); columns++) {
            for (int rows = 0; rows < board.getRows(); rows++) {
                pane.getChildren().add(boardGrid[columns][rows]);
            }
        }
        return pane;
    }

    @Override
    public void start(final Stage stage) throws Exception {
        Scene scene = new Scene(createContentPane(Difficulty.MEDIUM));
        stage.setTitle("Limesweeper");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(final String[] args) {
        launch();
    }
}