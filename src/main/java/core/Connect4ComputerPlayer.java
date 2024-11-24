package core;

import ui.Connect4TextConsole;

import java.util.InputMismatchException;

/**
 * Displays in console a simulated game board for the game Connect 4.
 *
 * @author jedidiahlafond
 * @version 0.04
 * @see Connect4Logic
 * @see Connect4TextConsole
 */
public class Connect4ComputerPlayer {

    /**
     * Creates a new Connect4ComputerPlayer game object
     */
    public Connect4ComputerPlayer(){

    }

    /**
     * Takes a Player object and takes a turn. The computer will find an advantageous valid position
     * to drop a token into and make the move. It will update the validMoveIndex and totalMoves.
     *
     * @param gameState the game state
     */
    public void takeTurn(GameState gameState){
        try {
            int column = computerMove(gameState);
            if(gameState.validMoveIndex[column] < 0){
                throw new InputMismatchException("Column is full");
            }
            gameState.connect4matrix[gameState.validMoveIndex[column]][column] = gameState.curPlayerTurn.getMarking();
            gameState.validMoveIndex[column]--;
            gameState.totalMoves++;
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Chooses a valid input from the validMoveIndex[] and tests to see which move is most
     * advantageous by maximizing the connections between its tokens. It then checks to see if it needs to block
     * the other player from winning. In future implementations the offense/defense on will be optimized to block
     * the other player from getting three in a row. It will simulate a move it has made and one move the other
     * person has made.
     *
     * @param gameState the game state
     * @return the column to drop the token down.
     */
    public int computerMove(GameState gameState) {
        int bestChoice4CPU = -1;
        int maxConnections = 0;
        for (int col = 0; col < gameState.validMoveIndex.length; col++) {
            int row = gameState.validMoveIndex[col];

            // Check if this column has a valid move
            if (row >= 0) {
                // Simulate placing CPU token
                gameState.connect4matrix[row][col] = 'O';

                // Evaluate the number of connected tokens
                int connections = countConnections(row, col, gameState.curPlayerTurn.getMarking(), gameState);

                // Reset the simulated move
                gameState.connect4matrix[row][col] = '\0';

                // Update the best move if this column has more connections
                if (connections > maxConnections) {
                    maxConnections = connections;
                    bestChoice4CPU = col;
                }
            }
        }
        for (int col = 0; col < gameState.validMoveIndex.length; col++) {
            int row = gameState.validMoveIndex[col];

            // Check if this column has a valid move
            if (row >= 0) {
                // Simulate placing player token
                gameState.connect4matrix[row][col] = 'X';

                // Evaluate the number of connected tokens
                int connections = countConnections(row, col, gameState.otherPlayer.getMarking(), gameState);

                // Reset the simulated move
                gameState.connect4matrix[row][col] = '\0';

                //block player move if they'll win next turn
                if (connections >= 4) {
                    return col;
                }
            }
        }
        try {
            if (gameState.validMoveIndex[bestChoice4CPU] < 0) {
                throw new InputMismatchException("InvalidMoveSelected");
            }
            return bestChoice4CPU;
        }catch(IndexOutOfBoundsException e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Takes a row, column, and a marking. It will scan vertically, horizontally, and diagonally.
     * With the local variable maxConnection it uses a Math.max to weigh it against the return
     * value of countDirection. This adds up how many connections are possible in all directions.
     * It subtracts one since the target being tested is double counted.
     *
     * @param row       int of the row of the gameboardmatrix
     * @param col       int of the col of the gameboardmatrix
     * @param marking   of the player
     * @param gameState the game state
     * @return the total connections to determine which move is most advantageous
     */
    public int countConnections(int row, int col, char marking, GameState gameState) {
        int maxConnections = 1; // Starts with the current token being placed
        // Horizontal
        maxConnections = Math.max(maxConnections, countDirection(row, col, 1, 0, marking, gameState) + countDirection(row, col, -1, 0, marking, gameState) - 1);
        // Vertical
        maxConnections = Math.max(maxConnections, countDirection(row, col, 0, 1, marking, gameState) + countDirection(row, col, 0, -1, marking, gameState) - 1);
        // Diagonal right to left
        maxConnections = Math.max(maxConnections, countDirection(row, col, 1, 1, marking, gameState) + countDirection(row, col, -1, -1, marking, gameState) - 1);
        // Diagonal left to right
        maxConnections = Math.max(maxConnections, countDirection(row, col, 1, -1, marking, gameState) + countDirection(row, col, -1, 1, marking, gameState) - 1);

        return maxConnections;
    }

    /**
     * This takes a row, column. This is the targeted, valid potential move. It then adds up the total it would connect
     * in all directions using the deltas to search up, down, side to side, and diagonally. It checks to make sure all
     * parameters and changes are within the bounds of the matrix. It then returns the total.
     *
     * @param row       integer of the row
     * @param col       integer of the Column
     * @param rowDelta  integer of the delta of the row/the direction the row extends
     * @param colDelta  integer of the delta of the col/the direction the col extends
     * @param marking   player char for marking
     * @param gameState the game state
     * @return integer of the amount connected from a specific direction
     */
    public int countDirection(int row, int col, int rowDelta, int colDelta, char marking, GameState gameState) {
        int count = 0;
        int r = row;
        int c = col;

        // Traverse in one direction to count consecutive tokens
        while (r >= 0 && r < gameState.connect4matrix.length && c >= 0 && c < gameState.connect4matrix[0].length && gameState.connect4matrix[r][c] == marking) {
            count++;
            r += rowDelta;
            c += colDelta;
        }

        return count;
    }
}
