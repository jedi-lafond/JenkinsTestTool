package ui;
import core.Connect4ComputerPlayer;
import core.Connect4Logic;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Produces text output for program
 *
 * @author jedidiahlafond
 * @version 0.04
 * @see Connect4Logic
 * @see Connect4ComputerPlayer
 */
public class Connect4TextConsole{
    /**
     * default constructor
     */
    public Connect4TextConsole(){

    }

    /**
     * Returns the current state of the game after a players valid move
     * Uses double for loop to load the matrix output
     *
     * @param gameBoardMatrix the game board matrix
     * @return String of game-board
     */
    public String displayGameboard(char[][] gameBoardMatrix){
        StringBuilder gameBoard = new StringBuilder();
        for(int row = 0; row < 6; row++){
            for(int col = 0; col < 7; col++){
                gameBoard.append("| ");
                if(!(gameBoardMatrix[row][col] == '\0')) gameBoard.append(gameBoardMatrix[row][col]);
                gameBoard.append("\t");
            }
            gameBoard.append("|\n");
        }
        return gameBoard.toString();
    }

    /**
     * Game interface type char.
     * Throws Exception InputMismatchException if wrong input detected.
     *
     * @return the char
     */
    public char gameInterfaceType(){
        char gameInterfaceType = 0;
        System.out.println("Would you like to play in the console or use a GUI? Enter 'C' for Console or 'G' for GUI.");
        Scanner scanner = new Scanner(System.in);
        try{
            String input = scanner.next().toUpperCase();
            gameInterfaceType = input.charAt(0);
            if (gameInterfaceType == 'C' || gameInterfaceType == 'G'){
                scanner.nextLine();
                return gameInterfaceType;
            }
            else{
                throw new InputMismatchException("Input must be 'C' or 'G'.");
            }
        }catch (InputMismatchException e){
            System.out.println("Invalid Input. Try again.");
            scanner.next();
        }
        return gameInterfaceType;
    }

    /**
     * Returns the initial state and start message
     * Throws exception if input mismatch
     *
     * @return String gameboard and start message
     */
    public char gameType(){

        System.out.println("Begin Game. Enter ‘P’ if you want to play against another player; enter ‘C’ to play against computer.");
        Scanner scanner = new Scanner(System.in);
        char selection = 0;
        try{
            String input = scanner.next().toUpperCase();
            selection = input.charAt(0);
            if(selection == 'C' || selection == 'P'){
                scanner.nextLine();
                return selection;
            }
            else{
                throw new InputMismatchException("Input must be 'C' or 'P'.");
            }

        }
        catch (InputMismatchException e){
            System.out.println("Invalid Input. Try again.");
            scanner.next();
            gameType();
        }
        gameType();
        return selection;
    }

    /**
     * Prints and informs players of whose turn it is and what range int to input to the console.
     *
     * @param playersTurn the players turn
     * @return String message to console
     */
    public String displayTurnMessage(String playersTurn){
        return playersTurn + " your turn. Please enter a column number (1-7).";
    }

    /**
     * Game end message string.
     *
     * @param player the player
     * @return the string
     */
    public String gameEndMessage(String player){
        return player + " won the game.";
    }
}
