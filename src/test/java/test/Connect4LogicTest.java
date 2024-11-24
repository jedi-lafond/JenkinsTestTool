package test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import core.Connect4ComputerPlayer;
import core.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import core.Connect4Logic;
import core.GameState;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import ui.Connect4Gui;
import ui.Connect4TextConsole;
import java.io.ByteArrayInputStream;
import java.util.InputMismatchException;

public class Connect4LogicTest {
    private GameState gameState;
    private Connect4Logic connect4Logic;

    public Connect4LogicTest() {
        gameState = new GameState();
        connect4Logic = new Connect4Logic(mockConsole);
    }
    @Mock
    private Connect4TextConsole mockConsole;
    @Mock
    private Connect4ComputerPlayer mockComputerPlayer;
    @Mock
    private Player mockPlayerX;
    @Mock
    private Player mockPlayerO;
    @Mock
    private Player mockCpuPlayerO;
    @Mock
    private Player mockCurPlayerturn;
    @Mock
    private Player mockOtherPlayer;
    @Mock
    private Player mockCpuOtherPlayer;

    @BeforeEach
    public void setUp() throws Exception {
        gameState = new GameState();
        connect4Logic = new Connect4Logic(mockConsole);
        mockPlayerX = new Player("Player X", 'X');
        mockPlayerO = new Player("Player O", 'O');
        mockCurPlayerturn = mockPlayerX;
        mockOtherPlayer = mockPlayerO;
        mockCpuPlayerO = new Player("Computer", 'O');
        mockCpuOtherPlayer = mockCpuPlayerO;
        MockitoAnnotations.openMocks(this);
    }
    @AfterEach
    public void tearDown() throws Exception {
        gameState = null;
        connect4Logic = null;
        mockCurPlayerturn = null;
        mockOtherPlayer = null;
        mockCpuOtherPlayer = null;
        mockPlayerX = null;
        mockPlayerO = null;
        mockCpuPlayerO = null;
    }

    @Test
    public void testConnect4LogicConstructor() {
        Connect4Logic connect4Logic = new Connect4Logic(mockConsole);
        assertNotNull(connect4Logic);
    }

    @Test
    public void testPlayGamePvC() {
        when(mockConsole.gameInterfaceType()).thenReturn('C');
        when(mockConsole.gameType()).thenReturn('C');
        connect4Logic = spy(new Connect4Logic(mockConsole));
        doAnswer(invocation -> null).when(connect4Logic).pvcGameType(any(Connect4ComputerPlayer.class), any(GameState.class));
        connect4Logic.playGame(connect4Logic.gameState);
        assertTrue(GameState.isComputer);
        verify(connect4Logic, times(1)).pvcGameType(any(Connect4ComputerPlayer.class), any(GameState.class));
    }

    @Test
    public void testPlayGameLaunchGui() {
        when(mockConsole.gameInterfaceType()).thenReturn('G');
        try(MockedStatic<Connect4Gui> mockedConnect4Gui = mockStatic(Connect4Gui.class)){
            mockedConnect4Gui.when(Connect4Gui::launchGui).thenAnswer(invocation -> null);

            connect4Logic = spy(new Connect4Logic(mockConsole));
            connect4Logic.playGame(this.gameState);

            mockedConnect4Gui.verify(Connect4Gui::launchGui, times(1));
        }
    }

    @Test
    public void testPlayGameInvalidGameInterfaceType(){
        when(mockConsole.gameInterfaceType()).thenReturn('X');
        connect4Logic = spy(new Connect4Logic(mockConsole));

        assertThrows(InputMismatchException.class, () -> connect4Logic.playGame(this.gameState));
        // Verify that neither the GUI nor game type methods were called
        verify(connect4Logic, times(0)).pvpGameType(this.gameState);
        verify(connect4Logic, times(0)).pvcGameType(any(Connect4ComputerPlayer.class), any(GameState.class));
    }

    @Test
    public void testPlayGameLaunchConsole() {
        when(mockConsole.gameInterfaceType()).thenReturn('C');

        connect4Logic = spy(new Connect4Logic(mockConsole));
        connect4Logic.playGame(this.gameState);

        verify(mockConsole, times(1)).gameInterfaceType();
    }

    @Test
    public void testPlayGameLaunchConsolePvC() {
        when(mockConsole.gameInterfaceType()).thenReturn('C');
        when(mockConsole.gameType()).thenReturn('C');

        connect4Logic = spy(new Connect4Logic(mockConsole));
        doNothing().when(connect4Logic).pvcGameType(any(Connect4ComputerPlayer.class), any(GameState.class));
        connect4Logic.playGame(connect4Logic.gameState);

        assertTrue(GameState.isComputer);
        verify(connect4Logic, times(1)).pvcGameType(any(Connect4ComputerPlayer.class), any(GameState.class));
        verify(connect4Logic, times(0)).pvpGameType(this.gameState);
    }

    @Test
    public void testPlayGameLaunchConsolePvP() {
        when(mockConsole.gameInterfaceType()).thenReturn('C');
        when(mockConsole.gameType()).thenReturn('P');

        connect4Logic = spy(new Connect4Logic(mockConsole));
        doAnswer(invocationOnMock -> null).when(connect4Logic).pvpGameType(this.gameState);
        connect4Logic.playGame(this.gameState);

        verify(connect4Logic, times(1)).pvpGameType(this.gameState);
        verify(connect4Logic, times(0)).pvcGameType(mockComputerPlayer, this.gameState);
    }

    @Test
    public void testPvcGameTypeTerminatesOnMovesLeftFalse() {
        connect4Logic = spy(new Connect4Logic(mockConsole));
        this.gameState.curPlayerTurn = mockCurPlayerturn;
        this.gameState.otherPlayer = mockCpuOtherPlayer;

        doAnswer(invocationOnMock -> {
            this.gameState.connect4matrix[5][0] = 'X'; // Simulate Player X placing a token
            this.gameState.totalMoves++; // Increment total moves
            return null;
        }).when(connect4Logic).takeTurn(any(GameState.class));

        doAnswer(invocationOnMock -> {
            this.gameState.connect4matrix[4][0] = 'O'; // Simulate computer placing a token
            this.gameState.totalMoves++;
            return null;
        }).when(mockComputerPlayer).takeTurn(any(GameState.class));

        doReturn(true, true,false).when(connect4Logic).movesLeft();
        doReturn(false).when(connect4Logic).checkEndOfGame(this.gameState);

        connect4Logic.pvcGameType(mockComputerPlayer, this.gameState);
        //Verify it is called two times as the while conditional
        verify(connect4Logic, times(3)).movesLeft();
        //Verify it is called twice as the while conditional
        verify(connect4Logic, times(2)).checkEndOfGame(this.gameState);
        //Verify it is called once as the while conditional
        verify(connect4Logic, times(1)).takeTurn(any(GameState.class));
        //Verify it is called once as the while conditional
        verify(mockComputerPlayer, times(1)).takeTurn(any(GameState.class));
    }

    @Test
    public void testPvcGameTypeTerminatesOnCheckEndOfGameTrue() {
        connect4Logic = spy(new Connect4Logic(mockConsole));
        this.gameState.curPlayerTurn = mockCurPlayerturn;
        this.gameState.otherPlayer = mockCpuOtherPlayer;

        doAnswer(invocationOnMock -> {
            this.gameState.connect4matrix[5][0] = 'X'; // Simulate Player X placing a token
            this.gameState.totalMoves++; // Increment total moves
            return null;
        }).when(connect4Logic).takeTurn(any(GameState.class));

        doAnswer(invocationOnMock -> {
            this.gameState.connect4matrix[4][0] = 'O'; // Simulate computer placing a token
            this.gameState.totalMoves++;
            return null;
        }).when(mockComputerPlayer).takeTurn(any(GameState.class));

        doReturn(true, true).when(connect4Logic).movesLeft();
        doReturn(false, true).when(connect4Logic).checkEndOfGame(this.gameState);

        connect4Logic.pvcGameType(mockComputerPlayer, this.gameState);
        //Verify it is called two times as the while conditional
        verify(connect4Logic, times(2)).movesLeft();
        //Verify it is called twice as the while conditional
        verify(connect4Logic, times(2)).checkEndOfGame(this.gameState);
        //Verify it is called once as the while conditional
        verify(connect4Logic, times(1)).takeTurn(any(GameState.class));
        //Verify it is called once as the while conditional
        verify(mockComputerPlayer, times(1)).takeTurn(any(GameState.class));
        //Validate the token placement
        assertEquals('X', this.gameState.connect4matrix[5][0]);
        assertEquals('O', this.gameState.connect4matrix[4][0]);
        //Validate the total moves
        assertEquals(2, this.gameState.totalMoves);
    }

    @Test
    public void testPvpGameTypeTerminatesOnMovesLeftFalse() {
        connect4Logic = spy(new Connect4Logic(mockConsole));
        connect4Logic.gameState.curPlayerTurn = mockCurPlayerturn;
        connect4Logic.gameState.otherPlayer = mockOtherPlayer;

        connect4Logic.gameState.connect4matrix[5][0] = 'X';
        // Simulate Player X placing a token
        connect4Logic.gameState.validMoveIndex[0]--;
        doAnswer(invocationOnMock -> null).when(connect4Logic).takeTurn(connect4Logic.gameState);
        doReturn(true, true, false).when(connect4Logic).movesLeft();
        doReturn(false).when(connect4Logic).checkEndOfGame(connect4Logic.gameState);

        connect4Logic.pvpGameType(connect4Logic.gameState);
        assertFalse(connect4Logic.checkEndOfGame(connect4Logic.gameState));
        //Verify it is called two times as the while conditional
        verify(connect4Logic, times(3)).movesLeft();
        //Verify it is called twice as the while conditional
        verify(connect4Logic, times(3)).checkEndOfGame(connect4Logic.gameState);
        //Verify it is called once as the while conditional
        verify(connect4Logic, times(2)).takeTurn(connect4Logic.gameState);
    }

    @Test
    public void testPvpGameTypeTerminatesOnCheckForWinnerTrue() {
        connect4Logic = spy(new Connect4Logic(mockConsole));
        connect4Logic.gameState.curPlayerTurn = mockCurPlayerturn;
        connect4Logic.gameState.otherPlayer = mockOtherPlayer;

        connect4Logic.gameState.connect4matrix[5][0] = 'X';
        // Simulate Player X placing a token
        connect4Logic.gameState.validMoveIndex[0]--;
        connect4Logic.gameState.totalMoves++; // Increment total moves for second branch
        doAnswer(invocationOnMock -> null).when(connect4Logic).takeTurn(connect4Logic.gameState);
        doReturn(true, true).when(connect4Logic).movesLeft();
        doReturn(false,true).when(connect4Logic).checkEndOfGame(connect4Logic.gameState);

        connect4Logic.pvpGameType(connect4Logic.gameState);
        //Verify it is called two times as the while conditional
        verify(connect4Logic, times(2)).movesLeft();
        //Verify it is called twice as the while conditional
        verify(connect4Logic, times(2)).checkEndOfGame(connect4Logic.gameState);
        //Verify it is called once as the while conditional
        verify(connect4Logic, times(2)).takeTurn(connect4Logic.gameState);
    }

    @Test
    public void testCheckEndOfGameFalse(){
        connect4Logic = spy(new Connect4Logic(mockConsole));
        doReturn(false).when(connect4Logic).checkForWinner(any(GameState.class));
        assertFalse(connect4Logic.checkEndOfGame(this.gameState));
        verify(connect4Logic, times(1)).checkForWinner(any(GameState.class));
    }

    @Test
    public void testCheckEndOfGameTrue(){
        connect4Logic = spy(new Connect4Logic(mockConsole));
        doReturn(true).when(connect4Logic).checkForWinner(any(GameState.class));

        when(mockPlayerX.getName()).thenReturn("Player X");

        // Set up the gameState with the mocked players
        gameState.curPlayerTurn = mockPlayerX;
        gameState.otherPlayer = mockPlayerO;
        assertTrue(connect4Logic.checkEndOfGame(this.gameState));
        verify(connect4Logic, times(1)).checkForWinner(any(GameState.class));
        verify(gameState.curPlayerTurn).setRecord(0);
        verify(gameState.otherPlayer).setRecord(1);
    }

    @Test
    public void testCheckEndOfGameTrue2(){
        connect4Logic = spy(new Connect4Logic(mockConsole));
        doReturn(true).when(connect4Logic).checkForWinner(any(GameState.class));

        when(mockPlayerO.getName()).thenReturn("Player O");

        // Set up the gameState with the mocked players
        this.gameState.curPlayerTurn = mockPlayerO;
        this.gameState.otherPlayer = mockPlayerX;
        assertTrue(connect4Logic.checkEndOfGame(this.gameState));
        verify(connect4Logic, times(1)).checkForWinner(any(GameState.class));
        verify(gameState.curPlayerTurn).setRecord(1);
        verify(gameState.otherPlayer).setRecord(0);
    }

    @Test
    public void testMovesLeftIndexOutOfBoundsLowHigh(){
        connect4Logic = spy(new Connect4Logic(mockConsole));
        connect4Logic.gameState.totalMoves = 43;
        assertThrows(IndexOutOfBoundsException.class, () -> connect4Logic.movesLeft());
        connect4Logic.gameState.totalMoves = -1;
        assertThrows(IndexOutOfBoundsException.class, () -> connect4Logic.movesLeft());
    }

    @Test
    public void testMovesLeftTrue() {
        connect4Logic = spy(new Connect4Logic(mockConsole));
        connect4Logic.gameState.totalMoves = 0;
        assertTrue(connect4Logic.movesLeft());
        connect4Logic.gameState.totalMoves = 20;
        assertTrue(connect4Logic.movesLeft());
        connect4Logic.gameState.totalMoves = 42;
        assertFalse(connect4Logic.movesLeft());
    }

    @Test
    public void testTakeTurnValidInput() {
        //Mock input from scanner
        String input = "4\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        connect4Logic = new Connect4Logic(mockConsole);
        // Set up initial game state
        connect4Logic.gameState.validMoveIndex = new int[]{5, 5, 5, 5, 5, 5, 5};
        connect4Logic.gameState.connect4matrix = new char[6][7];

        // Mock the current player's marking
        when(mockPlayerX.getMarking()).thenReturn('X');
        gameState.curPlayerTurn = mockPlayerX;

        // Execute method
        connect4Logic.takeTurn(gameState);

        // Verify the move was made correctly
        assertEquals('X', connect4Logic.gameState.connect4matrix[5][3]); // Check piece placed
        assertEquals(4, connect4Logic.gameState.validMoveIndex[3]); // Check validMoveIndex updated
        assertEquals(1, connect4Logic.gameState.totalMoves); // Check total moves increased
    }

    @Test
    public void testTakeTurnInvalidInputNotInteger() {
        //Mock input from scanner
        String input = "a\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        connect4Logic = new Connect4Logic(mockConsole);
        // Set up initial game state
        connect4Logic.gameState.validMoveIndex = new int[]{5, 5, 5, 5, 5, 5, 5};
        connect4Logic.gameState.connect4matrix = new char[6][7];

        // Mock the current player's marking
        when(mockPlayerX.getMarking()).thenReturn('X');
        gameState.curPlayerTurn = mockPlayerX;

        // Verify the move was made correctly
        assertThrows(InputMismatchException.class, () -> connect4Logic.takeTurn(gameState));
    }

    @Test
    public void testTakeTurnInvalidInputIntegerOutOfBoundsLow() {
        //Mock input from scanner
        String input = "-1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        connect4Logic = new Connect4Logic(mockConsole);
        // Set up initial game state
        connect4Logic.gameState.validMoveIndex = new int[]{5, 5, 5, 5, 5, 5, 5};
        connect4Logic.gameState.connect4matrix = new char[6][7];

        // Mock the current player's marking
        when(mockPlayerX.getMarking()).thenReturn('X');
        gameState.curPlayerTurn = mockPlayerX;

        // Verify the move was made correctly
        assertThrows(InputMismatchException.class, () -> connect4Logic.takeTurn(gameState));
    }

    @Test
    public void testTakeTurnInvalidInputIntegerOutOfBoundsHigh() {
        //Mock input from scanner
        String input = "8\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        connect4Logic = new Connect4Logic(mockConsole);
        // Set up initial game state
        connect4Logic.gameState.validMoveIndex = new int[]{5, 5, 5, 5, 5, 5, 5};
        connect4Logic.gameState.connect4matrix = new char[6][7];

        // Mock the current player's marking
        when(mockPlayerX.getMarking()).thenReturn('X');
        gameState.curPlayerTurn = mockPlayerX;

        // Verify the move was made correctly
        assertThrows(InputMismatchException.class, () -> connect4Logic.takeTurn(gameState));
    }

    @Test
    public void testTakeTurnInvalidInputIntegerColumnFull() {
        //Mock input from scanner
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        connect4Logic = new Connect4Logic(mockConsole);
        // Set up initial game state
        connect4Logic.gameState.validMoveIndex = new int[]{-1, 5, 5, 5, 5, 5, 5};
        connect4Logic.gameState.connect4matrix = new char[6][7];

        // Mock the current player's marking
        when(mockPlayerX.getMarking()).thenReturn('X');
        gameState.curPlayerTurn = mockPlayerX;

        // Verify the move was made correctly
        assertThrows(InputMismatchException.class, () -> connect4Logic.takeTurn(gameState));
    }

    @Test
    public void testCheckForWinnerTrue() {
        connect4Logic = spy(new Connect4Logic(mockConsole));
        // Set up the game state with a winning condition
        connect4Logic.gameState.connect4matrix = new char[][]{
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'X', 'X', 'X', 'X', '\0', '\0', '\0'}
        };
        //Horizontal
        assertTrue(connect4Logic.checkForWinner(connect4Logic.gameState));
        connect4Logic.gameState.connect4matrix = new char[][]{
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'X', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'X', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'X', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'X', '\0', '\0', '\0', '\0', '\0', '\0'}
        };
        //Vertical
        assertTrue(connect4Logic.checkForWinner(connect4Logic.gameState));
        connect4Logic.gameState.connect4matrix = new char[][]{
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', 'X', '\0', '\0', '\0'},
                {'\0', '\0', 'X', '\0', '\0', '\0', '\0'},
                {'\0', 'X', '\0', '\0', '\0', '\0', '\0'},
                {'X', '\0', '\0', '\0', '\0', '\0', '\0'}
        };
        //Diagonal Left To Right
        assertTrue(connect4Logic.checkForWinner(connect4Logic.gameState));
        connect4Logic.gameState.connect4matrix = new char[][]{
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'X', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', 'X', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', 'X', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', 'X', '\0', '\0', '\0'}
        };
        //Diagonal Right To Left
        assertTrue(connect4Logic.checkForWinner(connect4Logic.gameState));
    }

    @Test
    public void testCheckForWinnerFalse() {
        connect4Logic = spy(new Connect4Logic(mockConsole));
        // Set up the game state with a winning condition
        connect4Logic.gameState.connect4matrix = new char[][]{
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'\0', '\0', '\0', '\0', '\0', '\0', '\0'},
                {'O', 'X', 'O', 'X', '\0', '\0', '\0'}
        };
        //Horizontal
        assertFalse(connect4Logic.checkForWinner(connect4Logic.gameState));
    }


}
