package core;

import ui.Connect4Gui;

/**
 * Displays in console a simulated game board for the game Connect 4.
 *
 * @author jedidiahlafond
 * @version 0.04
 * @see Connect4Logic
 * @see Connect4Gui
 */
public class GameState {
    /**
     * The constant MAX_MOVES.
     */
    public static final int MAX_MOVES = 42;

    /**
     * The Connect 4 matrix.
     */
    public char[][] connect4matrix;
    /**
     * The Total moves.
     */
    public int totalMoves;
    /**
     * The Cur player turn.
     */
    public Player curPlayerTurn;
    /**
     * The Other player.
     */
    public Player otherPlayer;

    /**
     * The Valid move index.
     */
    public int[] validMoveIndex;
    /**
     * The constant isComputer.
     */
    public static boolean isComputer;

    /**
     * Set is computer.
     *
     * @param playComputer the play computer
     */
    public static void setIsComputer(boolean playComputer){
        isComputer = playComputer;
    }

    /**
     * Instantiates a new Game state.
     */
    public GameState(){
        this.connect4matrix = new char[6][7];
        this.validMoveIndex = new int[]{5, 5, 5, 5, 5, 5, 5};
        this.totalMoves = 0;
    }

}
