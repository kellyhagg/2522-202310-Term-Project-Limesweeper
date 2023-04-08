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
 * Utility class to run the Limesweeper application.
 *
 * @author Eunjeong (Alice) Hur, Kelly Hagg
 * @version 230407
 */
public class LimesweeperApplication extends Application {
    public static final int EASY_COLUMNS_ROWS = 12;
    public static final int MEDIUM_COLUMNS_ROWS = 16;
    public static final int HARD_COLUMNS_ROWS = 22;
    public static final int PANE_SIZE = 27;
    public static final Color TEXT_GREEN = Color.rgb(241, 252, 184);
    public static final Color LIGHT_GREEN = Color.rgb(221, 232, 164);
    public static final Color BACKGROUND_GREEN = Color.rgb(134, 183, 62);
    public static final Color OPEN_GREEN = Color.rgb(107, 146, 47);
    public static final Color DARKEST_GREEN = Color.rgb(87, 126, 27);
    public static final Color DEAD_RED = Color.rgb(255, 97, 55);
    private static Board board;
    private static Pane pane;
    private static StackPane resetBtn;
    private static StackPane settingsBtn;
    private static StackPane flagChangeBtn;
    private static Timer timer;
    private static String flagID = "white";
    private static Text flags;
    private static int[] timeCounter = {0};
    private static final Iterator<String> FLAG_ITERATOR = new InfiniteStringIterator();
    private static int counter;
    private Difficulty difficulty = Difficulty.MEDIUM;
    static int limeCounter;
    private int customNumLimes;
    private int numLimes;
    private boolean defaultLimes;
    private int btnOffset;


    public static int getCounter() {
        return counter;
    }

    public static void setCounter(final int counter) {
        LimesweeperApplication.counter = counter;
    }

    public static void checkWin() {
        if (counter == (board.getColumns() * board.getRows())) {
            youWin();
        }
    }

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

    public static void youWin() {
        final int outerDimension = 50;
        pane.setStyle("-fx-background-color: rgb(221,232,164);");
        flagChangeBtn.getChildren().clear();
        settingsBtn.getChildren().clear();
        resetBtn.getChildren().clear();
        resetBtn.getChildren().add(makeImageView("reset_sunglasses.png", outerDimension, outerDimension));
        Cell[][] boardGrid = board.getBoardGrid();
        for (Cell[] cells : boardGrid) {
            for (Cell cell : cells) {
                cell.setOutline(LIGHT_GREEN);
            }
        }
        timer.cancel();
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

    public static void youLose() {
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
        flagChangeBtn.getChildren().clear();
        flagChangeBtn.getChildren()
                .add(makeImageView("dead_flag_change_" + flagID + ".png", flagDimension, flagDimension));
        settingsBtn.getChildren().clear();
        settingsBtn.getChildren().add(makeImageView("dead_settings.png", settingsDimension, settingsDimension));
    }

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

    private void changeFlag() {
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
        flagChangeBtn.getChildren().clear();
        flagChangeBtn.getChildren()
                .add(makeImageView("flag_change_" + flagID + ".png", changeBtnDimension, changeBtnDimension));
    }

    private static class InfiniteStringIterator implements Iterator<String> {
        private final String[] strings;
        private int currentIndex;

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

    private void populateCounter() {
        final int counterWidth = 64;
        final int counterHeight = 40;
        final int xOffset = 12;
        final int yOffset = 8;
        final int fontSize = 22;
        limeCounter = board.getNumLimes();
        flags = new Text();
        StackPane flagField = new StackPane();
        ImageView limeImage = makeImageView("lime_counter.png", counterWidth, counterHeight);
        flagField.getChildren().add(limeImage);
        flagField.setPrefSize(counterWidth, counterHeight);
        flagField.setBackground(Background.fill(DARKEST_GREEN));
        flagField.getChildren().add(flags);
        flagField.setTranslateX(xOffset);
        flagField.setTranslateY(btnOffset * PANE_SIZE + yOffset);
        flags.setText(limeCounter + "      ");
        flags.setFont(Font.font("Impact", fontSize));
        flags.setFill(TEXT_GREEN);
        pane.getChildren().add(flagField);
    }

    protected static void decreaseFlags() {
        limeCounter--;
        flags.setText(limeCounter + "      ");
    }

    protected static void increaseFlags() {
        limeCounter++;
        flags.setText(limeCounter + "      ");
    }

    private void reset(final Stage stage) {
        timer.cancel();
        timeCounter = new int[]{0};
        counter = 0;
        startGame(stage);
    }

    private void generateResetBtn() {
        final int outerDimension = 50;
        final int ySpacing = 4;

        resetBtn = new StackPane();
        resetBtn.setPrefSize(outerDimension, outerDimension);
        resetBtn.getChildren().add(makeImageView("reset.png", outerDimension, outerDimension));
        resetBtn.setTranslateX((btnOffset * PANE_SIZE - outerDimension) / 2.0);
        resetBtn.setTranslateY(btnOffset * PANE_SIZE + ySpacing);
    }

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

    private void generateFlagChangeBtn() {
        final int outerDimension = 44;
        final int ySpacing = 7;
        final int offsetFactor = 3;
        flagChangeBtn = new StackPane();
        flagChangeBtn.setPrefSize(outerDimension, outerDimension);
        flagChangeBtn.getChildren()
                .add(makeImageView("flag_change_" + flagID + ".png", outerDimension, outerDimension));
        flagChangeBtn.setTranslateX(btnOffset * PANE_SIZE / 2.0 - btnOffset * offsetFactor - outerDimension);
        flagChangeBtn.setTranslateY(btnOffset * PANE_SIZE + ySpacing);
    }

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
        pane.getChildren().add(flagChangeBtn);
        flagChangeBtn.setOnMouseClicked(t -> changeFlag());
        settingsBtn.setOnMouseClicked(t -> generateSettings(stage));
    }

    private void generateSettings(final Stage stage) {
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

    public void startGame(final Stage stage) {
        createContentPane(stage);
        Scene scene = new Scene(pane);
        stage.setTitle("Limesweeper");
        stage.setScene(scene);
        stage.show();
        startTimer();
        stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::stopTimer);
    }

    private void stopTimer(final WindowEvent event) {
        timer.cancel();
    }

    @Override
    public void start(final Stage primarystage) {
        defaultLimes = true;
        startGame(primarystage);
    }

    public static void main(final String[] args) {
        launch();
    }
}
