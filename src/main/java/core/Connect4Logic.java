package core;

import ui.Connect4Gui;
import ui.Connect4TextConsole;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Connect 4 game logic that runs the Connect4TextConsole.java to create a game in the
 * user console. This is a two player game where alternating turns are made dropping
 * tokens into an upright matrix where the last piece occupies the first available vertical
 * slot. The matrix is 6 rows by 7 columns. The game ends when one player has 4 or more in a row
 * either vertically, horizontally or diagonally. The game ends in a tie if 42 moves have been
 * made and no 4 of one marking are connected.
 *
 * @author jedidiahlafond
 * @version 0.04
 * @see Connect4TextConsole fordisplay logic
 * @see Connect4Gui
 * @see Connect4ComputerPlayer
 * @see GameState
 * @see Player
 */
public class Connect4Logic{

    /**
     * The Game state.
     */
    public GameState gameState;
    private final Connect4TextConsole c4TextConsole;

    /**
     * Constructor for Connect4Logic
     *
     * @param c4TextConsole the c 4 text console
     */
    public Connect4Logic(Connect4TextConsole c4TextConsole){
        this.c4TextConsole = c4TextConsole;
        this.gameState = new GameState();
        this.gameState.curPlayerTurn = new Player("Player X", 'X');
        this.gameState.otherPlayer = new Player("Player O", 'O');
    }

    /**
     * Contains the logic to run the game from start to finish
     *
     * @param gameStateInit the game state init
     */
    public void playGame(GameState gameStateInit){
        try {
            //Ask to play in GUI or Console
            char gameInterfaceType = c4TextConsole.gameInterfaceType();
            if (gameInterfaceType == 'G') {
                Connect4Gui.launchGui();
            } else if (gameInterfaceType == 'C'){
                //select game type for console
                char gameType = c4TextConsole.gameType();
                c4TextConsole.displayGameboard(gameStateInit.connect4matrix);
                //Branch to selected game mode
                if (gameType == 'P') {
                    pvpGameType(gameStateInit);
                } else if (gameType == 'C') {
                    gameStateInit.otherPlayer.setName("Computer");
                    GameState.setIsComputer(true);
                    Connect4ComputerPlayer connect4ComputerPlayer = new Connect4ComputerPlayer();
                    pvcGameType(connect4ComputerPlayer, gameStateInit);
                }
            } else{
                throw new InputMismatchException("Invalid input. Please enter a valid input.");
            }
        }catch(InputMismatchException e){
            System.out.println(e.getMessage());
            throw e;
        }

    }

    /**
     * Player versus Computer game mode
     *
     * @param connect4ComputerPlayer the connect 4 computer player
     * @param gameState1             the game state 1
     */
    public void pvcGameType(Connect4ComputerPlayer connect4ComputerPlayer, GameState gameState1){
        Player temp;
        while(this.movesLeft()) {
            if (gameState1.totalMoves % 2 == 0) {
                temp = gameState1.curPlayerTurn;
                gameState1.curPlayerTurn = gameState1.otherPlayer;
                gameState1.otherPlayer = temp;
                //System.out.println(c4TextConsole.displayTurnMessage(gameState1.curPlayerTurn.getName()));
                takeTurn(gameState1);
            } else {
                temp = gameState1.otherPlayer;
                gameState1.otherPlayer = gameState1.curPlayerTurn;
                gameState1.curPlayerTurn = temp;
                //System.out.println(c4TextConsole.displayTurnMessage(gameState1.curPlayerTurn.getName()));
                connect4ComputerPlayer.takeTurn(gameState1);
            }
            //Print state
            if (checkEndOfGame(gameState1)) return;
        }
        gameState1.curPlayerTurn.setRecord(2);
        gameState1.otherPlayer.setRecord(2);
        System.out.print("\nTie Game.\n");
    }

    /**
     * Checks for end of game and updates records/state.
     *
     * @param gameStateEnd the game state end
     * @return true if game won, false otherwise
     */
    public boolean checkEndOfGame(GameState gameStateEnd) {
        //System.out.println(c4TextConsole.displayGameboard(this.gameState.connect4matrix));
        if(checkForWinner(this.gameState)){
            if(gameStateEnd.curPlayerTurn.getName().equals("Player X")) {
                gameStateEnd.curPlayerTurn.setRecord(0);
                gameStateEnd.otherPlayer.setRecord(1);
            }else{
                gameStateEnd.otherPlayer.setRecord(0);
                gameStateEnd.curPlayerTurn.setRecord(1);
            }
            //System.out.println(c4TextConsole.gameEndMessage(gameStateEnd.curPlayerTurn.getName()));
            return true;
        }
        return false;
    }

    /**
     * Player vs Player game mode.
     *
     * @param gameStatePvP the game state pv p
     */
    public void pvpGameType(GameState gameStatePvP){
        Player temp;
        while(this.movesLeft()){
            if(gameStatePvP.totalMoves % 2 == 0){
                temp = gameStatePvP.curPlayerTurn;
                gameStatePvP.curPlayerTurn = gameStatePvP.otherPlayer;
                gameStatePvP.otherPlayer = temp;
            }
            else{
                temp = gameStatePvP.otherPlayer;
                gameStatePvP.otherPlayer = gameStatePvP.curPlayerTurn;
                gameStatePvP.curPlayerTurn = temp;
            }
            //System.out.println(c4TextConsole.displayTurnMessage(this.gameState.curPlayerTurn.getName()));
            takeTurn(gameStatePvP);
            //Print state
            if (checkEndOfGame(gameStatePvP)) return;
        }
        gameStatePvP.curPlayerTurn.setRecord(2);
        gameStatePvP.otherPlayer.setRecord(2);
        System.out.print("\nTie Game.\n");
    }

    /**
     * Method to check whether moves and spaces are left in the game-board
     *
     * @return boolean if no moves left
     */
    public boolean movesLeft(){
        try {
            if (this.gameState.totalMoves < 0 || this.gameState.totalMoves > 42) {
                throw new IndexOutOfBoundsException("Invalid move count.");
            }
            return this.gameState.totalMoves < GameState.MAX_MOVES;
        }catch (IndexOutOfBoundsException e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Takes player input and infinite loops until valid input is made
     * Throws Exception if invalid input is entered. Loops back to start after
     * logging exception and notifying user of proper input.
     *
     * @param gameStateLocal GameState
     */
    public void takeTurn(GameState gameStateLocal){
        Scanner scanner = new Scanner(System.in);
        int column;
        boolean validInput = false;
        while (!validInput) {
            try {
                if (!scanner.hasNextInt()) {
                    throw new InputMismatchException("Input not an integer.");
                }
                column = scanner.nextInt() - 1;
                if (column < 0 || column > 6) {
                    throw new InputMismatchException("Input must be an integer between 1 and 7.");
                } else if (gameState.validMoveIndex[column] < 0) {
                    throw new InputMismatchException("Column full.");
                } else {
                    this.gameState.connect4matrix[gameState.validMoveIndex[column]][column] = gameStateLocal.curPlayerTurn.getMarking();
                    this.gameState.validMoveIndex[column]--;
                    this.gameState.totalMoves++;
                    validInput = true;
                }
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                throw e;
            }
        }

    }

    /**
     * Checks all the ways in which a player can win and returns a boolean if a condition is met
     *
     * @param stateOfGame the state of game
     * @return boolean of status of game
     */
    public boolean checkForWinner(GameState stateOfGame) {
        for (int row = 5; row >= 0; row--) {
            for (int col = 0; col < 7; col++) {
                char curr = stateOfGame.connect4matrix[row][col];
                if (curr == '\0') continue; // Skip empty cells
                // Check Vertical
                if (row >= 3 &&
                        curr == stateOfGame.connect4matrix[row - 1][col] &&
                        curr == stateOfGame.connect4matrix[row - 2][col] &&
                        curr == stateOfGame.connect4matrix[row - 3][col]) {
                    return true;
                }

                // Check Horizontal
                if (col <= 3 &&
                        curr == stateOfGame.connect4matrix[row][col + 1] &&
                        curr == stateOfGame.connect4matrix[row][col + 2] &&
                        curr == stateOfGame.connect4matrix[row][col + 3]) {
                    return true;
                }

                // Check Left to Right Diagonal
                if (row >= 3 && col <= 3 &&
                        curr == stateOfGame.connect4matrix[row - 1][col + 1] &&
                        curr == stateOfGame.connect4matrix[row - 2][col + 2] &&
                        curr == stateOfGame.connect4matrix[row - 3][col + 3]) {
                    return true;
                }

                // Check Right to Left Diagonal
                if (row >= 3 && col >= 3 &&
                        curr == stateOfGame.connect4matrix[row - 1][col - 1] &&
                        curr == stateOfGame.connect4matrix[row - 2][col - 2] &&
                        curr == stateOfGame.connect4matrix[row - 3][col - 3]) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Connect4Logic newGame = new Connect4Logic(new Connect4TextConsole());
        newGame.playGame(newGame.gameState);
    }



}
