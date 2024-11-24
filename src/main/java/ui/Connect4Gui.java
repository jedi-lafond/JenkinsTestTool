package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import core.Connect4ComputerPlayer;
import core.Connect4Logic;
import core.GameState;
import core.Player;

/**
 * Displays in GUI a simulated game board for the game Connect 4.
 *
 * @author jedidiahlafond
 * @version 0.04
 * @see Connect4Logic
 * @see GameState
 * @see Player
 * @see Connect4ComputerPlayer
 */
public class Connect4Gui extends Application {
    private final Player playerX;
    private final Player playerO;
    private final Circle[] circleButtons;
    private final Circle[][] circleBoardMatrix;
    /**
     * The Game state.
     */
    protected GameState gameState;
    private final Connect4Logic logicMethods;
    /**
     * The Header text.
     */
    protected Text headerText;
    /**
     * The Top box.
     */
    protected VBox topBox;
    /**
     * The Game over.
     */
    protected boolean gameOver;

    /**
     * Instantiates a new Connect 4 gui.
     */
    public Connect4Gui(){
        this.logicMethods = new Connect4Logic(new Connect4TextConsole());
        this.gameState = new GameState();
        this.topBox = new VBox();
        this.playerX = new Player("Player 1", 'X');
        this.playerO = new Player("Player 2", 'O');
        this.circleButtons = new Circle[7];
        this.circleBoardMatrix = new Circle[6][7];
        this.gameState.curPlayerTurn = playerX;
        this.headerText = new Text("2-Player mode or Computer vs. Player");
        this.gameOver = true;
    }

    /**
     * Set stage and game-board state
     * @param primaryStage sets stage for GUI
     */
    @Override
    public void start(Stage primaryStage) {
        // Root
        AnchorPane root = new AnchorPane();
        root.setPrefSize(840, 945);
        root.setStyle("-fx-border-color: #000000; -fx-border-width: 3;");

        // Top Vbox with message
        Button cpu = new Button("Computer");
        Button twoPlayer = new Button("2-Player");
        headerText.setFont(new Font(30));
        headerText.setTextAlignment(TextAlignment.CENTER);
        topBox.setPrefSize(845, 100);
        topBox.setStyle("-fx-background-color: #6BBAEC; -fx-border-color: #000000; -fx-border-width: 3;");
        topBox.setSpacing(5);
        topBox.setPadding(new Insets(0,200,5,200));
        topBox.getChildren().addAll(headerText, cpu, twoPlayer);
        cpu.setOnAction(e -> {
            cpuMode();
            topBox.getChildren().removeAll(cpu, twoPlayer);
        });
        twoPlayer.setOnAction(e -> {
            twoPlayerMode();
            topBox.getChildren().removeAll(cpu, twoPlayer);
        });
        root.getChildren().add(topBox);

        // button gridPain because it is Pain
        GridPane firstRowGrid = new GridPane();
        firstRowGrid.setLayoutY(100);
        firstRowGrid.setPrefSize(840, 125);
        firstRowGrid.setStyle("-fx-border-width: 3;");

        for (int col = 0; col < 7; col++) {
            ColumnConstraints colConstraints = new ColumnConstraints(120);
            firstRowGrid.getColumnConstraints().add(colConstraints);
            RowConstraints rowConstraints = new RowConstraints(120);
            firstRowGrid.getRowConstraints().add(rowConstraints);

            Circle circle = new Circle(50, Color.WHITE);
            circle.setStroke(Color.BLACK);

            Rectangle rectangle = new Rectangle(120, 120, Color.web("#ffffff"));

            StackPane stack = new StackPane();
            stack.getChildren().addAll(rectangle,circle);
            circleButtons[col] = circle;

            firstRowGrid.add(stack, col, 0);
            circle.setOnMouseEntered(e -> initialize());
            circle.setOnMouseExited(e -> initialize());
            int columnIndex = col;
            circle.setOnMouseClicked(e -> takeTurnOnMouseClick(columnIndex));
        }

        root.getChildren().add(firstRowGrid);

        // gridPain
        GridPane mainGrid = new GridPane();
        mainGrid.setLayoutY(220);
        mainGrid.setPrefSize(840, 720);
        mainGrid.setStyle("-fx-border-color: #000000; -fx-grid-lines-visible: true; -fx-background-color: #6987C9; -fx-border-width: 3;");
        //Add columns to gridPain
        for (int col = 0; col < 7; col++) {
            ColumnConstraints colConstraints = new ColumnConstraints(120);
            mainGrid.getColumnConstraints().add(colConstraints);
        }
        //Add rows to gridPain
        for (int row = 0; row < 6; row++) {
            RowConstraints rowConstraints = new RowConstraints(120);
            mainGrid.getRowConstraints().add(rowConstraints);
        }
        // Add circles to the main gridpane and matrix to manage state
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                //create circle object and add the obj to the matrix to be able to access and change properties later
                Circle circle = new Circle(50, Color.web("#bcc4db"));
                circle.setStroke(Color.BLACK);
                circleBoardMatrix[row][col] = circle;
                //create square
                Rectangle rectangle = new Rectangle(120, 120, Color.web("#6987C9"));
                rectangle.setStroke(Color.BLACK);
                //create stackPane and add objs to the stackpane
                StackPane stack = new StackPane();
                stack.getChildren().addAll(rectangle,circle);
                //add obj to the grid
                mainGrid.add(stack, col, row);
            }
        }

        root.getChildren().add(mainGrid);

        //scene and stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMaxHeight(976);
        primaryStage.setMinHeight(976);
        primaryStage.setMaxWidth(845);
        primaryStage.setMinWidth(845);
        primaryStage.setTitle("Connect 4");
        primaryStage.show();
    }

    /**
     * Sets the proper state for the selected game mode. Uses the static variable isComputer from GameState.
     */
    private void cpuMode(){
        headerText.setText("Begin Game.\nPlayer X it is your turn.");
        this.gameOver = false;
        topBox.setPadding(new Insets(0,300,5,300));
        GameState.setIsComputer(true);
        playerO.setName("Computer");
    }

    /**
     * Ensures state for two-player game mode is set.
     */
    private void twoPlayerMode(){
        headerText.setText("Begin Game.\nPlayer X it is your turn.");
        this.gameOver = false;
        topBox.setPadding(new Insets(0,300,5,300));
        GameState.setIsComputer(false);
        playerO.setName("Player 2");
    }

    /**
     * initializes the circles in the gridPane above the gameboard
     */
    private void initialize() {
        for(int i = 0; i < 7; i++){
            setupHoverEffect(circleButtons[i]);
        }
    }

    /**
     * Take the column of the event and checks to ensure it is a valid move.
     * Changes the circle node's fill to match that players color.
     * If playing against a computer, computer will take its turn.
     * Check for winner and repeat until game finishes.
     * @param col Takes the column the player token needs to be dropped down
     */
    private void takeTurnOnMouseClick(int col){
        //No more moves in that column
        if(this.gameState.validMoveIndex[col] < 0 || this.gameOver){
            return;
        }
        //update state
        if(this.gameState.totalMoves % 2 == 0){
            circleBoardMatrix[gameState.validMoveIndex[col]][col].setFill(Color.web("#ff2e21"));
            gameState.connect4matrix[gameState.validMoveIndex[col]][col] = 'X';
            gameState.validMoveIndex[col]--;
            this.gameState.curPlayerTurn = playerO;
            this.gameState.otherPlayer = playerX;
            this.headerText.setText(gameState.curPlayerTurn.getName() + " it is your turn");
            this.gameState.totalMoves++;
        }
        boolean continueGame = this.logicMethods.checkForWinner(this.gameState);
        checkForWinner();
        if(GameState.isComputer && this.gameState.totalMoves % 2 == 1 && !continueGame){
            Connect4ComputerPlayer cpu = new Connect4ComputerPlayer();
            int column = cpu.computerMove(this.gameState);
            gameState.connect4matrix[gameState.validMoveIndex[column]][column] = 'O';
            circleBoardMatrix[gameState.validMoveIndex[column]][column].setFill(Color.web("#fff41f"));
            gameState.validMoveIndex[column]--;
            this.gameState.curPlayerTurn = playerX;
            this.gameState.otherPlayer = playerO;
            this.headerText.setText(gameState.curPlayerTurn.getName() + " it is your turn");
            this.gameState.totalMoves++;
        }
        else if (!GameState.isComputer && this.gameState.totalMoves % 2 == 1 && !continueGame){
            circleBoardMatrix[gameState.validMoveIndex[col]][col].setFill(Color.web("#fff41f"));
            gameState.connect4matrix[gameState.validMoveIndex[col]][col] = 'X';
            gameState.validMoveIndex[col]--;
            //Change turns
            this.gameState.curPlayerTurn = playerX;
            this.gameState.otherPlayer = playerO;
            this.headerText.setText(gameState.curPlayerTurn.getName() + " it is your turn");
        }
        checkForWinner();
    }

    /**
     * Checks for winner and updates states.
     */
    private void checkForWinner(){
        if(this.logicMethods.checkForWinner(this.gameState)){
            this.gameOver = true;
            //End Game use otherPlayer because you already changed the
            headerText.setText(gameState.otherPlayer.getName() + " you win!");
            endGameMenu();
            this.gameState.curPlayerTurn.setRecord(0);
            this.gameState.otherPlayer.setRecord(1);
        }
        if(this.gameState.totalMoves == GameState.MAX_MOVES){
            headerText.setText("Tie game");
            this.gameState.curPlayerTurn.setRecord(2);
            this.gameState.otherPlayer.setRecord(2);
            endGameMenu();
        }
    }

    /**
     * Display end game menu.
     */
    private void endGameMenu(){
        //create button to ask if they what to play again or quit.
        Button quitBtn = new Button("Quit.");
        quitBtn.prefHeight(20);
        quitBtn.prefWidth(30);
        Button playAgain = new Button("Play Again");
        playAgain.prefHeight(20);
        playAgain.prefWidth(30);
        quitBtn.setOnAction(e -> Platform.exit());
        playAgain.setOnAction(e -> {
            playAgain();
            topBox.getChildren().removeAll(quitBtn, playAgain);
        });
        topBox.getChildren().addAll(playAgain,quitBtn);
    }

    /**
     * Change circle fill to show players color token
     * @param circle takes circle of the event to be triggered under
     */
    private void setupHoverEffect(Circle circle) {
        circle.setOnMouseEntered(mouseEvent -> hoverColor(circle));
        circle.setOnMouseExited(mouseEvent -> hoverColorReset(circle));
    }

    /**
     * Helper method for setupHoverEffect
     * @param hoverCircle takes the circle that will have new .setFill applied to it
     */
    private void hoverColor(Circle hoverCircle){
        if(gameState.totalMoves % 2 == 0){
            //Player x color
            hoverCircle.setFill(Color.web("#ff2e21"));
        }else{
            //Player o color
            hoverCircle.setFill(Color.web("#fff41f"));
        }

    }
    /**
     * Helper method for setupHoverEffect
     * @param hoverCircle takes circle that will have color value reset to default
     */
    private void hoverColorReset(Circle hoverCircle){
        hoverCircle.setFill(Color.web("#ffffff"));
    }

    /**
     * Reset game state for new game.
     */
    private void playAgain() {
        this.gameState.totalMoves = 0;
        this.gameState.connect4matrix = new char[6][7];  // Clear the board matrix
        this.gameState.validMoveIndex = new int[]{5, 5, 5, 5, 5, 5, 5};  // Reset column indices
        this.gameState.curPlayerTurn = playerX;
        this.gameState.otherPlayer = playerO;
        this.gameOver = false;
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                circleBoardMatrix[row][col].setFill(Color.web("#bcc4db"));
            }
            headerText.setText("Player X it is your turn.");
        }
    }

    /**
     * Used to launch GUI from Connect4Logic
     */
    public static void launchGui() {
        launch();
    }
}