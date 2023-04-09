package com.example._2522_game_project;

import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.*;
import java.util.*;

/**
 * The Limesweeper Application class.
 * @author kellyhagg, EunjeongHur
 * @version 230408
 */
public class LimesweeperApplication extends Application {
    /**
     * The number of rows and columns for the EASY Difficulty.
     */
    public static final int EASY_COLUMNS_ROWS = 12;
    /**
     * The number of rows and columns for the MEDIUM Difficulty.
     */
    public static final int MEDIUM_COLUMNS_ROWS = 16;
    /**
     * The number of rows and columns for the HARD Difficulty.
     */
    public static final int HARD_COLUMNS_ROWS = 22;
    /**
     * The pane size for each cell.
     */
    public static final int PANE_SIZE = 27;
    /**
     * The Color of the timer and counter text.
     */
    public static final Color TEXT_GREEN = Color.rgb(241, 252, 184);
    /**
     * The Color of the cells.
     */
    public static final Color LIGHT_GREEN = Color.rgb(221, 232, 164);
    /**
     * The Color of the content pane background.
     */
    public static final Color BACKGROUND_GREEN = Color.rgb(134, 183, 62);
    /**
     * The Color of the opened cells.
     */
    public static final Color OPEN_GREEN = Color.rgb(107, 146, 47);
    /**
     * The Color of the timer and counter backgrounds.
     */
    public static final Color DARKEST_GREEN = Color.rgb(87, 126, 27);
    /**
     * The Color of the background upon losing the game.
     */
    public static final Color DEAD_RED = Color.rgb(255, 97, 55);
    /**
     * The iterator that will infinitely loop through the flag label Strings.
     */
    public static final Iterator<String> FLAG_ITERATOR = new InfiniteStringIterator();
    // The game Board consisting of Cells.
    private static Board board;
    // The content Pane where all graphics appear.
    private static Pane pane;
    // The reset button StackPane.
    private static StackPane resetBtn;
    // The settings button StackPane.
    private static StackPane settingsBtn;
    // The flag toggle button StackPane.
    private static StackPane flagToggleBtn;
    // The flagID String set to the starting flag colour.
    private static String flagID = "white";
    // The timer in seconds for the player to view their progress.
    private static Timer timer;
    // The timer counter integer array.
    private static int[] timeCounter = {0};
    // The number of flags to be displayed to the player to view their progress.
    private static int flagCounter;
    // The Text of the number of flags to be displayed to the player to view their progress.
    private static Text flagCounterText;
    // The number of opened limes.
    private static int limeCounter;
    // The Difficulty enum set to the starting Difficulty.
    private Difficulty difficulty = Difficulty.MEDIUM;
    // The custom number of limes set by the player should they choose.
    private int customNumLimes;
    // The total number of limes of the current Board.
    private int numLimes;
    // The boolean to select if the board will be populated with the default or custom lime number.
    private boolean defaultLimes;
    // The offset of the bottom bar buttons set per the current Difficulty.
    private int btnOffset;

    // GAME FUNCTION METHODS

    /**
     * Change the artwork, lock the cells, and display the leaderboard once the game is won.
     */
    private static void youWin() {
        final int outerDimension = 50;
        pane.setStyle("-fx-background-color: rgb(221,232,164);");
        flagToggleBtn.getChildren().clear();
        settingsBtn.getChildren().clear();
        resetBtn.getChildren().clear();
        resetBtn.getChildren().add(makeImageView("reset_sunglasses.png", outerDimension, outerDimension));
        Cell[][] boardGrid = board.getBoardGrid();
        for (Cell[] cells : boardGrid) {
            for (Cell cell : cells) {
                cell.setOutline(LIGHT_GREEN);
                cell.setState(StateType.LOCKED);
            }
        }
        timer.cancel();
        populateLeaderBoard();
    }

    /**
     * Check win.
     */
    protected static void checkWin() {
        if (limeCounter == (board.getColumns() * board.getRows())) {
            youWin();
        }
    }

    private static void revealAllLimes() {
        for (Cell[] cells : board.getBoardGrid()) {
            for (Cell cell : cells) {
                if (cell.isLime() && cell.getState() != StateType.FLAGGED) {
                    cell.getChildren().add(makeImageView("lime.png",
                            Cell.CELL_SIZE - 1, Cell.CELL_SIZE - 1));
                }
                cell.setState(StateType.LOCKED);
            }
        }
    }

    /**
     * Change the artwork and lock the cells once the game is lost.
     */
    protected static void youLose() {
        final int resetBtnDimension = 50;
        final int flagDimension = 44;
        final int settingsDimension = 36;
        timer.cancel();
        revealAllLimes();
        pane.setStyle("-fx-background-color: rgb(255,97,55);");
        Cell[][] boardGrid = board.getBoardGrid();
        for (Cell[] cells : boardGrid) {
            for (Cell cell : cells) {
                cell.setOutline(DEAD_RED);
            }
        }
        resetBtn.getChildren().clear();
        resetBtn.getChildren().add(makeImageView("dead_lime.png", resetBtnDimension, resetBtnDimension));
        flagToggleBtn.getChildren().clear();
        flagToggleBtn.getChildren()
                .add(makeImageView("dead_flag_change_" + flagID + ".png", flagDimension, flagDimension));
        settingsBtn.getChildren().clear();
        settingsBtn.getChildren().add(makeImageView("dead_settings.png", settingsDimension, settingsDimension));
    }

    /*
     * A class that creates an infinite string iterator of the flag label Strings.
     */
    private static class InfiniteStringIterator implements Iterator<String> {
        private final String[] strings;
        private int currentIndex;

        /**
         * Instantiates a new Infinite string iterator for all flag label Strings.
         */
        InfiniteStringIterator() {
            this.strings = new String[]{"red", "blue", "cross", "exclamation", "white"};
            this.currentIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public String next() {
            final int size = 5;
            String nextString = this.strings[this.currentIndex];
            this.currentIndex = (this.currentIndex + 1) % size;
            return nextString;
        }
    }

    /*
     * Toggles the flag icon from the infinite flag iterator.
     */
    private void toggleFlag() {
        final int flagDimension = 23;
        final int changeBtnDimension = 44;
        String flagString = (String) FLAG_ITERATOR.next();
        flagID = flagString;
        for (Cell[] cells : board.getBoardGrid()) {
            for (Cell cell : cells) {
                cell.setFlagID(flagString);
                if (cell.getState() == StateType.FLAGGED) {
                    cell.getChildren().clear();
                    ImageView flag = makeImageView(flagID + "_flag.png", flagDimension, flagDimension);
                    cell.setOutline(BACKGROUND_GREEN);
                    cell.getChildren().add(cell.outline);
                    cell.getChildren().add(flag);
                }
            }
        }
        flagToggleBtn.getChildren().clear();
        flagToggleBtn.getChildren()
                .add(makeImageView("flag_change_" + flagID + ".png", changeBtnDimension, changeBtnDimension));
    }

    /**
     * Decrease flags in flagCounter.
     */
    protected static void decreaseFlags() {
        flagCounter--;
        flagCounterText.setText(flagCounter + "      ");
    }

    /**
     * Increase flags in flagCounter.
     */
    protected static void increaseFlags() {
        flagCounter++;
        flagCounterText.setText(flagCounter + "      ");
    }

    /**
     * Gets the number of limes.
     *
     * @return the number of limes.
     */
    public static int getCounter() {
        return limeCounter;
    }

    /**
     * Sets the number of limes.
     *
     * @param counter the counter
     */
    public static void setCounter(final int counter) {
        limeCounter = counter;
    }

    /*
     * Resets the game inside the current window when the reset button is pressed.
     */
    private void reset(final Stage stage) {
        timer.cancel();
        timeCounter = new int[]{0};
        limeCounter = 0;
        startGame(stage);
    }

    // LEADERBOARD GENERATOR METHODS

    /*
     * Reads the current leaders in the LeaderBoard text file.
     */
    private static List<Person> readFile() {
        String fileName = "src/main/java/com/example/_2522_game_project/LeaderBoard.txt";
        try (Scanner scanner = new Scanner(new File(fileName))) {
            List<Person> people = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String name = scanner.nextLine();
                String score = scanner.nextLine();
                people.add(new Person(name, Integer.parseInt(String.valueOf(score))));
            }
            return people;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * Writes new leaders to the LeaderBoard text file.
     */
    private static void writeToFile(final String userName) {
        String fileName = "src/main/java/com/example/_2522_game_project/LeaderBoard.txt";
        try (FileWriter fileWriter = new FileWriter(fileName, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(userName);
            printWriter.println(timeCounter[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Generates a StringBuilder of the current ranks in the leaderboard.
     */
    private static StringBuilder printRanks(final List<Person> people) {
        StringBuilder builder = new StringBuilder();
        int printCounter = 0;
        final int breakVal = 5;
        for (int index = 0; index < people.size(); index++) {
            if (printCounter >= breakVal) {
                break;
            } else {
                builder.append("#").append(index + 1).append(": ").append(people.get(index).name())
                        .append(" - ").append(people.get(index).score()).append("s\n");
                printCounter++;
            }
        }
        return builder;
    }

    /*
     * Populates the leaderboard with the current leaders in the LeaderBoard text file.
     */
    private static void populateLeaderBoard() {
        TextInputDialog userInput = new TextInputDialog();
        List<Person> people = readFile();

        userInput.setTitle("LeaderBoard");
        userInput.setContentText("Enter your name to save the score: ");
        timeCounter[0] = timeCounter[0] - 1;

        if (people != null) {
            people.sort(Comparator.comparing(Person::score));
            userInput.setHeaderText("Congratulations!\nYour score: " + timeCounter[0] + "s\n\n" + printRanks(people));
        } else {
            userInput.setHeaderText("Congratulations!\nYour score: " + timeCounter[0] + "s");
        }
        userInput.setGraphic(null);
        Optional<String> result = userInput.showAndWait();
        if (result.isPresent()) {
            String user = userInput.getEditor().getText();
            writeToFile(user);
        }
    }

    // COUNTER AND TIMER METHODS

    /*
     * Populates the remaining lime counter in the game window.
     */
    private void populateCounter() {
        final int counterWidth = 64;
        final int counterHeight = 40;
        final int xOffset = 12;
        final int yOffset = 8;
        final int fontSize = 22;
        flagCounter = board.getNumLimes();
        flagCounterText = new Text();
        StackPane flagField = new StackPane();
        ImageView limeImage = makeImageView("lime_counter.png", counterWidth, counterHeight);
        flagField.getChildren().add(limeImage);
        flagField.setPrefSize(counterWidth, counterHeight);
        flagField.setBackground(Background.fill(DARKEST_GREEN));
        flagField.getChildren().add(flagCounterText);
        flagField.setTranslateX(xOffset);
        flagField.setTranslateY(btnOffset * PANE_SIZE + yOffset);
        flagCounterText.setText(flagCounter + "      ");
        flagCounterText.setFont(Font.font("Impact", fontSize));
        flagCounterText.setFill(TEXT_GREEN);
        pane.getChildren().add(flagField);
    }

    /*
     * Stops the timer.
     */
    private void stopTimer(final WindowEvent event) {
        timer.cancel();
    }

    /*
     * Generates a new timer and timer graphics.
     */
    private void startTimer() {
        final int timerWidth = 64;
        final int timerHeight = 40;
        final int xOffset = 76;
        final int yOffset = 8;
        final int fontSize = 22;
        final int period = 1000;
        timer = new Timer();
        Text time = new Text();
        StackPane timeField = new StackPane();
        timeField.setPrefSize(timerWidth, timerHeight);
        timeField.setBackground(Background.fill(DARKEST_GREEN));
        timeField.getChildren().add(time);
        timeField.setTranslateX(btnOffset * PANE_SIZE  - xOffset);
        timeField.setTranslateY(btnOffset * PANE_SIZE + yOffset);
        time.setText(String.valueOf(0) + ' ' + 's');
        time.setFont(Font.font("Impact", fontSize));
        time.setFill(TEXT_GREEN);
        pane.getChildren().add(timeField);

        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                time.setText(String.valueOf(timeCounter[0]) + ' ' + 's');
                timeCounter[0]++;
            }
        }, 0, period);
        populateCounter();
    }

    // SETTINGS MENU GENERATOR METHODS

    /*
     * Populates the settings menu grid and opens the menu to await user input.
     */
    private void openSettings(final Stage stage,
                              final RadioButton easyRadioButton,
                              final RadioButton mediumRadioButton,
                              final RadioButton hardRadioButton,
                              final Spinner<Integer> spinner,
                              final ToggleGroup difficultyRadioBtn) {
        final int vInt = 5;
        final int gridGap = 10;
        GridPane grid = new GridPane();
        grid.setHgap(gridGap);
        grid.setVgap(gridGap);
        grid.add(new Label("Difficulty:"), 0, 0);
        grid.add(new VBox(vInt, easyRadioButton, mediumRadioButton, hardRadioButton), 1, 0);
        grid.add(new Label("Number:"), 0, 1);
        grid.add(spinner, 1, 1);

        Dialog<String> settingsWindow = new Dialog<>();
        settingsWindow.setTitle("Settings");
        settingsWindow.getDialogPane().setContent(grid);
        settingsWindow.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Button okButton = (Button) settingsWindow.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setOnAction(event -> {
            String difficultyText = ((RadioButton) difficultyRadioBtn.getSelectedToggle()).getText();
            int enteredNumLimes = spinner.getValue();
            if (enteredNumLimes != numLimes) {
                customNumLimes = enteredNumLimes;
                defaultLimes = false;
            }
            if (Difficulty.valueOf(difficultyText.toUpperCase()) != difficulty) {
                difficulty = Difficulty.valueOf(difficultyText.toUpperCase());
            }
            reset(stage);
        });
        settingsWindow.showAndWait();
    }

    /*
     * Generates the radio buttons and spinner for the settings menu.
     */
    private void generateSettingsMenu(final Stage stage) {
        final int maxLimes = 99;
        final int easyLimes = 10;
        final int mediumLimes = 35;
        final int hardLimes = 99;

        Spinner<Integer> spinner = new Spinner<>(1,  maxLimes, numLimes);
        RadioButton easyRadioButton = new RadioButton("Easy");
        RadioButton mediumRadioButton = new RadioButton("Medium");
        RadioButton hardRadioButton = new RadioButton("Hard");

        ToggleGroup difficultyRadioBtn = new ToggleGroup();
        easyRadioButton.setToggleGroup(difficultyRadioBtn);
        mediumRadioButton.setToggleGroup(difficultyRadioBtn);
        hardRadioButton.setToggleGroup(difficultyRadioBtn);

        switch (difficulty) {
            case EASY -> easyRadioButton.setSelected(true);
            case HARD -> hardRadioButton.setSelected(true);
            default -> mediumRadioButton.setSelected(true);
        }

        easyRadioButton.setOnAction(event -> spinner.getValueFactory().setValue(easyLimes));
        mediumRadioButton.setOnAction(event -> spinner.getValueFactory().setValue(mediumLimes));
        hardRadioButton.setOnAction(event -> spinner.getValueFactory().setValue(hardLimes));
        openSettings(stage, easyRadioButton, mediumRadioButton, hardRadioButton, spinner, difficultyRadioBtn);
    }

    // BUTTON GENERATOR METHODS

    /*
     * Helper method to the button generators to add an image to the Pane.
     */
    private static ImageView makeImageView(final String filename, final int xDimension, final int yDimension) {
        Image image = null;
        try {
            image = new Image(Objects.requireNonNull(
                    LimesweeperApplication.class.getResource(filename)).openStream());
        } catch (IOException e) {
            System.out.println("Image attempting to be loaded cannot be found");
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(xDimension);
        imageView.setFitHeight(yDimension);
        return imageView;
    }

    /*
     * Generates the settings button in the correct location for the board size.
     */
    private void generateSettingsBtn() {
        final int outerDimension = 36;
        final int ySpacing = 11;
        final int offsetFactor = 3;
        settingsBtn = new StackPane();
        settingsBtn.setPrefSize(outerDimension, outerDimension);
        settingsBtn.getChildren().add(makeImageView("settings.png", outerDimension, outerDimension));
        settingsBtn.setTranslateX(btnOffset * PANE_SIZE / 2.0 + btnOffset * offsetFactor);
        settingsBtn.setTranslateY(btnOffset * PANE_SIZE + ySpacing);
    }

    /*
     * Generates the flag toggle button in the correct location for the board size.
     */
    private void generateFlagChangeBtn() {
        final int outerDimension = 44;
        final int ySpacing = 7;
        final int offsetFactor = 3;
        flagToggleBtn = new StackPane();
        flagToggleBtn.setPrefSize(outerDimension, outerDimension);
        flagToggleBtn.getChildren()
                .add(makeImageView("flag_change_" + flagID + ".png", outerDimension, outerDimension));
        flagToggleBtn.setTranslateX(btnOffset * PANE_SIZE / 2.0 - btnOffset * offsetFactor - outerDimension);
        flagToggleBtn.setTranslateY(btnOffset * PANE_SIZE + ySpacing);
    }

    /*
     * Generates the reset button in the correct location for the board size.
     */
    private void generateResetBtn() {
        final int outerDimension = 50;
        final int ySpacing = 4;

        resetBtn = new StackPane();
        resetBtn.setPrefSize(outerDimension, outerDimension);
        resetBtn.getChildren().add(makeImageView("reset.png", outerDimension, outerDimension));
        resetBtn.setTranslateX((btnOffset * PANE_SIZE - outerDimension) / 2.0);
        resetBtn.setTranslateY(btnOffset * PANE_SIZE + ySpacing);
    }

    // CONTENT PANE GENERATOR METHODS

    /*
     * Adds the cells and buttons to the content Pane.
     */
    private void addContent(final Stage stage) {
        Cell[][] boardGrid = board.getBoardGrid();
        for (int columns = 0; columns < board.getColumns(); columns++) {
            for (int rows = 0; rows < board.getRows(); rows++) {
                pane.getChildren().add(boardGrid[columns][rows]);
            }
        }
        generateResetBtn();
        generateSettingsBtn();
        generateFlagChangeBtn();
        resetBtn.setOnMouseClicked(t -> {
            MouseButton btn = t.getButton();
            if (btn == MouseButton.PRIMARY) {
                reset(stage);
            }
        });
        pane.getChildren().add(resetBtn);
        pane.getChildren().add(settingsBtn);
        pane.getChildren().add(flagToggleBtn);
        flagToggleBtn.setOnMouseClicked(t -> toggleFlag());
        settingsBtn.setOnMouseClicked(t -> generateSettingsMenu(stage));
    }

    /*
     * Generates the content Pane and assigns correct sizing and offsets for the selected Difficulty.
     */
    private void createContentPane(final Stage stage) {
        pane = new Pane();
        final int barHeight = 60;
        final int easyNumLimes = 10;
        final int mediumNumLimes = 30;
        final int hardNumLimes = 99;
        int desiredNumLimes;
        switch (difficulty) {
            case EASY -> {
                desiredNumLimes = easyNumLimes;
                btnOffset = EASY_COLUMNS_ROWS; }
            case HARD -> {
                desiredNumLimes = hardNumLimes;
                btnOffset = HARD_COLUMNS_ROWS; }
            default -> {
                desiredNumLimes = mediumNumLimes;
                btnOffset = MEDIUM_COLUMNS_ROWS; }
        }
        if (defaultLimes) {
            numLimes = desiredNumLimes;
        } else {
            numLimes = customNumLimes;
        }
        board = new Board(btnOffset, btnOffset, numLimes, flagID);
        pane.setPrefSize(btnOffset * PANE_SIZE, btnOffset * PANE_SIZE + barHeight);
        pane.setStyle("-fx-background-color: rgb(134,183,62);");
        addContent(stage);
    }

    // GAME DRIVER METHODS

    /*
     * Secondary method to launch the game after reset.
     */
    private void startGame(final Stage stage) {
        createContentPane(stage);
        Scene scene = new Scene(pane);
        stage.setTitle("Limesweeper");
        stage.setScene(scene);
        stage.show();
        startTimer();
        stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::stopTimer);
    }

    /**
     * Primary method to launch the game.
     *
     * @param primaryStage is the primary game Stage.
     */
    @Override
    public void start(final Stage primaryStage) {
        defaultLimes = true;
        startGame(primaryStage);
    }

    /**
     * Main method to launch the game.
     *
     * @param args the args.
     */
    public static void main(final String[] args) {
        launch();
    }
}
