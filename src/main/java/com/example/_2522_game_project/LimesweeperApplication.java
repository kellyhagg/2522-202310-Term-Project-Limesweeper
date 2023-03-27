package com.example._2522_game_project;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

    public static void youLose() {
        revealAllLimes();
    }

    private static void revealAllLimes() {
        for (Cell[] cells : board.getBoardGrid()) {
            for (Cell cell : cells) {
                if (cell.isLime()) {
                    cell.setValue("X");
                }
            }
        }
    }

    private Pane createContentPane() {
        Pane pane = new Pane();
        pane.setPrefSize(810, 432); // hardcoded for now, Kelly to make dynamic with each difficulty
        pane.setStyle("-fx-background-color: green;");
        board = new Board(30, 16, 99); // hardcoded here for now (likely to move)
        Cell[][] boardGrid = board.getBoardGrid();
        for (int columns = 0; columns < board.getColumns(); columns++) {
            for (int rows = 0; rows < board.getRows(); rows++) {
                pane.getChildren().add(boardGrid[columns][rows]);
            }
        }
        return pane;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContentPane());
        stage.setTitle("Limesweeper");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}