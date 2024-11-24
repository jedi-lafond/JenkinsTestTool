package test;

import static org.junit.jupiter.api.Assertions.*;

import core.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {
    //Tests for Player class
    private Player playerTest;
    @BeforeEach
    public void setUp() {
        playerTest = new Player("Test Player", 'X');
    }
    @AfterEach
    public void tearDown() {
        playerTest = null;
    }

    @Test
    public void testGetName() {
        assertEquals("Test Player", playerTest.getName());
    }

    @Test
    public void testSetName() {
        playerTest.setName("New Name");
        assertEquals("New Name", playerTest.getName());
    }

    @Test
    public void testGetMarking() {
        assertEquals('X', playerTest.getMarking());
    }

    @Test
    public void testGetRecord() {
        int[] expectedRecord = {0, 0, 0};
        assertArrayEquals(expectedRecord, playerTest.getRecord());
    }

    @Test
    public void testSetRecord(){
        playerTest.setRecord(1);
        int[] expectedRecord = {0, 1, 0};
        assertArrayEquals(expectedRecord, playerTest.getRecord());
    }
    @Test
    public void testPlayerConstructor(){
        Player player = new Player("Test Player", 'X');
        assertEquals("Test Player", player.getName());
        assertEquals('X', player.getMarking());
        int[] expectedRecord = {0, 0, 0};
        assertArrayEquals(expectedRecord, player.getRecord());
    }
    @Test
    public void testPlayerConstructorWithInvalidMarking(){
        Player player = new Player();
        assertEquals("Default", player.getName());
        assertEquals('O', player.getMarking());
        int[] expectedRecord = {0, 0, 0};
        assertArrayEquals(expectedRecord, player.getRecord());
    }
}
