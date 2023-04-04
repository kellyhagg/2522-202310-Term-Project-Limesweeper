package com.example._2522_game_project;

import javafx.application.Application;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
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
 * @version 230321
 */
public class LimesweeperApplication extends Application {
    public static final int EASY_COLUMNS_ROWS = 10;
    public static final int MEDIUM_COLUMNS_ROWS = 16;
    public static final int HARD_COLUMNS_ROWS = 24;
    public static final int PANE_SIZE = 27;
    static Board board;
    private static Pane pane;
    private static StackPane resetBtn;
    private static StackPane settingsBtn;
    private static StackPane flagChangeBtn;
    private static Timer timer;
    private static String flagID = "white";

    private Iterator flagIterator = new InfiniteStringIterator();
    private static Text flags;
    private static int[] timeCounter = {0};
    static int counter;
    private Difficulty difficulty = Difficulty.MEDIUM;
    static int numLimes;

    public static void checkWin(){
        youWin();
        if (counter == (board.getColumns() * board.getRows())) {
            System.out.println("All cells are opened.");
            youWin();
        }
    }

    public static void youWin() {
        timer.cancel();
        TextInputDialog userInput = new TextInputDialog();
        userInput.setTitle("LeaderBoard");
        userInput.setContentText("Enter your name to save the score: ");
        userInput.setHeaderText(null);
        userInput.setGraphic(null);
        Optional<String> result = userInput.showAndWait();
        if (result.isPresent()) {
            String user = userInput.getEditor().getText();
            writeToFile(user);
        }
        readFile();
    }

    private static void readFile() {
        String fileName = "src/main/java/com/example/_2522_game_project/LeaderBoard.txt";
        try (Scanner scanner = new Scanner(new File(fileName));) {
            int lineNum = 0;
            List<String> names = new ArrayList<String>();
            List<Integer> scores = new ArrayList<Integer>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (lineNum % 2 == 0) {
                    names.add(line);
                } else {
                    scores.add(Integer.parseInt(String.valueOf(line)));
                }
                System.out.println(line);
                lineNum += 1;
            }
            showLeaderBoard(names, scores);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void showLeaderBoard(List<String> names, List<Integer> scores) {
        for (int i = 0; i < names.size(); i ++) {
            System.out.printf("%s: %d", names.get(i), scores.get(i));
        }
    }

    private static void writeToFile(String userName) {
        String fileName = "src/main/java/com/example/_2522_game_project/LeaderBoard.txt";

        try(FileWriter fileWriter = new FileWriter(fileName, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);) {
            printWriter.println(userName);
            printWriter.println(timeCounter[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void youLose() throws IOException {
        final int resetBtnDimension = 50;
        final int flagDimension = 44;
        final int settingsDimension = 36;
        timer.cancel();
        revealAllLimes();
        pane.setStyle("-fx-background-color: rgb(255,97,55);");
        Cell[][] boardGrid = board.getBoardGrid();
        for (Cell[] cells : boardGrid) {
            for (Cell cell : cells) {
                cell.setOutline(Color.rgb(255,97,55));
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

    public static ImageView makeImageView(final String filename, final int xDimension, final int yDimension) throws IOException {
        Image image = new Image(Objects.requireNonNull(
                LimesweeperApplication.class.getResource(filename)).openStream());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(xDimension);
        imageView.setFitHeight(yDimension);
        return imageView;
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

    void changeFlag() throws IOException {
        final int outerDimension = 44;
        System.out.format("testing\n");
        flagID = (String) flagIterator.next();
        flagChangeBtn.getChildren().clear();
        flagChangeBtn.getChildren().add(makeImageView("flag_change_" + flagID + ".png", outerDimension, outerDimension));
    }

    public class InfiniteStringIterator implements Iterator<String> {
        private String[] strings;
        private int currentIndex;

        public InfiniteStringIterator() {
            this.strings = new String[]{"white", "red", "blue", "cross", "exclamation"};
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
    private static Iterator<String> createFlagIterator() {
        List<String> stringList = new ArrayList<>();
        stringList.add("white");
        stringList.add("red");
        stringList.add("blue");
        stringList.add("cross");
        stringList.add("exclamation");
        return stringList.iterator();
    }


    private void startTimer() {
        timer = new Timer();
        Text time = new Text();
        StackPane timeField = new StackPane();
        timeField.setPrefSize(65, 40);
        timeField.setBackground(Background.fill(Color.rgb(87,126,27)));
        timeField.getChildren().add(time);
        timeField.setTranslateX(MEDIUM_COLUMNS_ROWS * PANE_SIZE  - 80);
        timeField.setTranslateY(MEDIUM_COLUMNS_ROWS * PANE_SIZE + 8);
        time.setText(String.valueOf(0) + ' ' + 's');
        time.setFont(Font.font("Impact", 22));
        time.setFill(Color.rgb(241,252,184));
        pane.getChildren().add(timeField);

        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
//                System.out.printf("%ds\n", timeCounter);
                time.setText(String.valueOf(timeCounter[0]) + ' ' + 's');
                timeCounter[0]++;
            }
        },0, 1000);
        checkNumOfFlags();
    }
    private void createContentPane(final Stage stage, final Difficulty difficulty) throws IOException {
        pane = new Pane();
        final int barHeight = 60;
        final int easyNumLimes = 10;
        final int mediumNumLimes = 40;
        final int hardNumLimes = 99;
        switch (difficulty) {
            case EASY -> {
                board = new Board(EASY_COLUMNS_ROWS, EASY_COLUMNS_ROWS, easyNumLimes);
                pane.setPrefSize(EASY_COLUMNS_ROWS * PANE_SIZE, EASY_COLUMNS_ROWS * PANE_SIZE + barHeight);
            }
            case HARD -> {
                board = new Board(HARD_COLUMNS_ROWS, HARD_COLUMNS_ROWS, hardNumLimes);
                pane.setPrefSize(HARD_COLUMNS_ROWS * PANE_SIZE, HARD_COLUMNS_ROWS * PANE_SIZE + barHeight);
            }
            default -> {
                board = new Board(MEDIUM_COLUMNS_ROWS, MEDIUM_COLUMNS_ROWS, mediumNumLimes);
                pane.setPrefSize(MEDIUM_COLUMNS_ROWS * PANE_SIZE, MEDIUM_COLUMNS_ROWS * PANE_SIZE + barHeight);
            }
        }
        pane.setStyle("-fx-background-color: rgb(134,183,62);");
        addContent(stage, difficulty);
    }

    private void checkNumOfFlags() {
        numLimes = board.getNumLimes();
        flags = new Text();
        StackPane flagField = new StackPane();
        flagField.setPrefSize(80, 40);
        flagField.setBackground(Background.fill(Color.rgb(87,126,27)));
        flagField.getChildren().add(flags);
        flagField.setTranslateX(MEDIUM_COLUMNS_ROWS * PANE_SIZE / 5.5  - 65);
        flagField.setTranslateY(MEDIUM_COLUMNS_ROWS * PANE_SIZE + 8);
        flags.setText(String.valueOf(numLimes) + ' ' + "Limes");
        flags.setFont(Font.font("Impact", 18));
        flags.setFill(Color.rgb(241,252,184));
        pane.getChildren().add(flagField);
    }

    public static void decreaseFlags() {
        numLimes -=1 ;
        flags.setText(String.valueOf(numLimes) + ' ' + "Limes");
    }

    public static void increaseFlags() {
        numLimes += 1;
        flags.setText(String.valueOf(numLimes) + ' ' + "Limes");
    }

    private void reset(Stage stage) throws Exception {
        timer.cancel();
        timeCounter = new int[]{0};
        counter = 0;
        startGame(stage);
    }

    private void generateResetBtn() throws IOException {
        final int outerDimension = 50;
        final int ySpacing = 4;
        resetBtn = new StackPane();
        resetBtn.setPrefSize(outerDimension, outerDimension);
        Image image = new Image(Objects.requireNonNull(
                LimesweeperApplication.class.getResource("reset.png")).openStream());
        ImageView resetView = new ImageView(image);
        resetView.setFitHeight(outerDimension);
        resetView.setFitWidth(outerDimension);
        resetBtn.getChildren().add(resetView);
        resetBtn.setTranslateX((MEDIUM_COLUMNS_ROWS * PANE_SIZE - outerDimension) / 2.0);
        resetBtn.setTranslateY(MEDIUM_COLUMNS_ROWS * PANE_SIZE + ySpacing);
    }

    private void generateSettingsBtn(final int xSpacing) throws IOException {
        final int outerDimension = 36;
        final int ySpacing = 11;
        settingsBtn = new StackPane();
        settingsBtn.setPrefSize(outerDimension, outerDimension);
        Image image = new Image(Objects.requireNonNull(
                LimesweeperApplication.class.getResource("settings.png")).openStream());
        ImageView settingsView = new ImageView(image);
        settingsView.setFitHeight(outerDimension);
        settingsView.setFitWidth(outerDimension);
        settingsBtn.getChildren().add(settingsView);
        settingsBtn.setTranslateX(MEDIUM_COLUMNS_ROWS * PANE_SIZE / 2.0  + xSpacing);
        settingsBtn.setTranslateY(MEDIUM_COLUMNS_ROWS * PANE_SIZE + ySpacing);
    }

    private void generateFlagChangeBtn(final int xSpacing, boolean dead) throws IOException {
        final int outerDimension = 44;
        final int ySpacing = 7;
        flagChangeBtn = new StackPane();
        flagChangeBtn.setPrefSize(outerDimension, outerDimension);
        Image image;
        if (!dead) {
            image = new Image(Objects.requireNonNull(
                    LimesweeperApplication.class.getResource("flag_change_" + flagID + ".png")).openStream());
        } else {
            image = new Image(Objects.requireNonNull(
                    LimesweeperApplication.class.getResource("dead_flag_change_" + flagID + ".png")).openStream());
        }
        ImageView flagView = new ImageView(image);
        flagView.setFitHeight(outerDimension);
        flagView.setFitWidth(outerDimension);
        flagChangeBtn.getChildren().add(flagView);
        flagChangeBtn.setTranslateX(113);
        flagChangeBtn.setTranslateY(MEDIUM_COLUMNS_ROWS * PANE_SIZE + ySpacing);
    }

    private void addContent(final Stage stage, final Difficulty difficulty) throws IOException {
        Cell[][] boardGrid = board.getBoardGrid();
        for (int columns = 0; columns < board.getColumns(); columns++) {
            for (int rows = 0; rows < board.getRows(); rows++) {
                pane.getChildren().add(boardGrid[columns][rows]);
            }
        }
        generateResetBtn();
        generateSettingsBtn(63);
        generateFlagChangeBtn(63, false);
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
        pane.getChildren().add(settingsBtn);
        pane.getChildren().add(flagChangeBtn);
        flagChangeBtn.setOnMouseClicked(t -> {
            try {
                changeFlag();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void startGame(final Stage stage) throws Exception {
        createContentPane(stage, difficulty);
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
    public void start(final Stage primarystage) throws Exception {
        startGame(primarystage);
    }

    public static void main(final String[] args) {
        launch();
    }
}