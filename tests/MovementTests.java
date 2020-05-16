package tests;

import maze.Main;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class MovementTests
{
    @Test
    public void test_MoveRooms()
    {
        InputStream fakeIn = new ByteArrayInputStream("right\n/unlock\np@ssword\nq".getBytes());
        System.setIn(fakeIn);
        Main.main(null);
        assertNotEquals(Main.getCurrentRoom(), Main.getMaze().getStart());
    }
    @Test
    public void test_MoveOutOfMaze()
    {
        InputStream fakeIn = new ByteArrayInputStream("up\nleft\nq".getBytes());
        System.setIn(fakeIn);
        Main.main(null);
        // Confirming still at start
        assertEquals(Main.getCurrentRoom(),Main.getMaze().getStart());
    }
    @Test
    public void test_RoomLockedOnFail()
    {
        InputStream fakeIn = new ByteArrayInputStream("down\n/AdminFail\ndown\nq".getBytes());
        System.setIn(fakeIn);
        Main.main(null);
        // Confirming still at start
        assertEquals(Main.getCurrentRoom(),Main.getMaze().getStart());
    }
    @Test
    public void test_LoseState()
    {
        InputStream fakeIn = new ByteArrayInputStream("down\n/AdminFail\nright\n/AdminFail\nn".getBytes());
        System.setIn(fakeIn);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Main.main(null);
        // Confirming still at start
        System.out.println(outContent.toString());
        assertTrue(outContent.toString().contains("There is no escape, do you want to try again? (y/n)"));
    }
}