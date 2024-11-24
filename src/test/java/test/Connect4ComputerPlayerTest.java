package test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import core.Connect4ComputerPlayer;
import core.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import core.GameState;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.InputMismatchException;
public class Connect4ComputerPlayerTest {
    @Mock
    private Connect4ComputerPlayer mockC4ComputerPlayer;
    @Mock
    private GameState mockGameState;
    @Mock
    private Player mockPlayerX;
    @Mock
    private Player mockPlayerO;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // This should come first

        when(mockPlayerX.getMarking()).thenReturn('X');
        when(mockPlayerO.getMarking()).thenReturn('O');

        mockGameState.connect4matrix = new char[6][7];
        mockGameState.validMoveIndex = new int[]{5,5,5,5,5,5,5};
        mockGameState.curPlayerTurn = mockPlayerO;
        mockGameState.otherPlayer = mockPlayerX;

        mockC4ComputerPlayer = spy(new Connect4ComputerPlayer());
    }
    @AfterEach
    public void tearDown() {
        mockGameState = null;
        mockPlayerX = null;
        mockPlayerO = null;
    }
    @Test
    public void testTakeTurnMakeValidMove() {
        doReturn(0).when(mockC4ComputerPlayer).computerMove(this.mockGameState);
        mockC4ComputerPlayer.takeTurn(this.mockGameState);

        verify(mockC4ComputerPlayer, times(1)).computerMove(this.mockGameState);
        assertEquals(4, this.mockGameState.validMoveIndex[0]);
        assertEquals('O', this.mockGameState.connect4matrix[5][0]);
        assertEquals(1, this.mockGameState.totalMoves);
    }
    @Test
    public void testTakeTurnMakeInvalidMoveThrowException() {
        doReturn(0).when(mockC4ComputerPlayer).computerMove(this.mockGameState);
        this.mockGameState.validMoveIndex[0] = -1;
        assertThrows(InputMismatchException.class, () -> {
            mockC4ComputerPlayer.takeTurn(this.mockGameState);
        });
        verify(mockC4ComputerPlayer, times(1)).computerMove(this.mockGameState);
        assertNotEquals(4, this.mockGameState.validMoveIndex[0]);
        assertNotEquals('O', this.mockGameState.connect4matrix[5][0]);
        assertEquals(0, this.mockGameState.totalMoves);
    }
    //Tests for computerMove
    @Test
    public void testComputerMoveWinningMove() {
        this.mockGameState.validMoveIndex[0] = 4;
        this.mockGameState.validMoveIndex[1] = 4;
        this.mockGameState.validMoveIndex[2] = 4;
        this.mockGameState.connect4matrix = new char[][]{
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'O', 'O', 'O', '\0', '\0', '\0', '\0'}
        };
        int retValue = mockC4ComputerPlayer.computerMove(mockGameState);
        assertEquals(3, retValue);
    }
    @Test
    public void testComputerMoveBlockOtherPlayerWin() {
        this.mockGameState.validMoveIndex[0] = 4;
        this.mockGameState.validMoveIndex[1] = 4;
        this.mockGameState.validMoveIndex[2] = 4;
        this.mockGameState.connect4matrix = new char[][]{
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'X', 'X', 'X', '\0', '\0', '\0', '\0'}
        };
        int retValue = mockC4ComputerPlayer.computerMove(mockGameState);
        assertEquals(3, retValue);
    }
    @Test
    public void testComputerMoveThrowsException() {
        //No valid move left
        this.mockGameState.validMoveIndex = new int[]{-1,-1,-1,-1,-1,-1,-1};
        assertThrows(IndexOutOfBoundsException.class, () -> {
            mockC4ComputerPlayer.computerMove(mockGameState);
        });
    }
    @Test
    public void testCountConnectionsTestHorizontal() {
        this.mockGameState.connect4matrix = new char[][]{
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', 'O', '\0', '\0', '\0', '\0'},
                {'\0', 'O', '\0', '\0', '\0', '\0', '\0'},
                {'O', 'O', 'O', '\0', '\0', '\0', '\0'}
        };
        int retValue = mockC4ComputerPlayer.countConnections(5, 1, 'O', mockGameState);
        assertEquals(3, retValue);
    }
    @Test
    public void testCountConnectionsTestDiagonal() {
        this.mockGameState.connect4matrix = new char[][]{
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', 'O', '\0', '\0', '\0'},
                {'\0', '\0', 'O', '\0', '\0', '\0', '\0'},
                {'\0', 'O', '\0', '\0', '\0', '\0', '\0'},
                {'O', 'O', 'O', '\0', '\0', '\0', '\0'}
        };
        int retValue = mockC4ComputerPlayer.countConnections(2, 3, 'O', mockGameState);
        assertEquals(4, retValue);
    }
    @Test
    public void testCountDirection() {
        this.mockGameState.connect4matrix = new char[][]{
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', 'O', '\0', '\0', '\0'},
                {'\0', '\0', 'O', '\0', '\0', '\0', '\0'},
                {'\0', 'O', '\0', '\0', '\0', '\0', '\0'},
                {'O', 'O', 'O', '\0', '\0', '\0', '\0'}
        };
        int retValue = mockC4ComputerPlayer.countConnections(2, 3, 'O', mockGameState);
        assertEquals(4, retValue);
    }
    @Test
    public void testCountDirectionInvalidMarking() {
        this.mockGameState.connect4matrix = new char[][]{
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', 'O', '\0', '\0', '\0'},
                {'\0', '\0', 'O', '\0', '\0', '\0', '\0'},
                {'\0', 'O', '\0', '\0', '\0', '\0', '\0'},
                {'O', 'O', 'O', '\0', '\0', '\0', '\0'}
        };
        int retValue = mockC4ComputerPlayer.countConnections(2, 3, 'X', mockGameState);
        assertNotEquals(4, retValue);
    }
}
