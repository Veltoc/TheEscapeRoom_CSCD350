package tests;

import maze.Main;

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
        InputStream fakeIn = new ByteArrayInputStream("right\n/answer\nq".getBytes());
        System.setIn(fakeIn);
        Main.main(null);

        assertEquals(Main.getMaze().getRoom(1, 0), Main.getCurrentRoom());
    }

    @Test
    public void test_MoveOutOfMaze()
    {
        InputStream fakeIn = new ByteArrayInputStream("up\n/answer\nleft\n/answer\nq".getBytes());
        System.setIn(fakeIn);
        Main.main(null);

        // Confirming still at start
        assertEquals(Main.getCurrentRoom(), Main.getMaze().getStart());
    }

    @Test
    public void test_RoomLockedOnFail()
    {
        InputStream fakeIn = new ByteArrayInputStream("down\n/fail\ndown\n/answer\nq".getBytes());
        System.setIn(fakeIn);
        Main.main(null);

        // Confirming still at start
        assertEquals(Main.getCurrentRoom(), Main.getMaze().getStart());
    }

    @Test
    public void test_LoseState()
    {
        InputStream fakeIn = new ByteArrayInputStream("down\n/fail\nright\n/fail\nn".getBytes());
        System.setIn(fakeIn);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Main.main(null);

        // Confirming output
        System.out.println(outContent.toString());
        assertTrue(outContent.toString().contains("There is no escape..."));
    }

    @Test
    public void test_WinState()
    {
        InputStream fakeIn = new ByteArrayInputStream("/escape\nn".getBytes());
        System.setIn(fakeIn);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Main.main(null);

        // Confirming output
        System.out.println(outContent.toString());
        assertTrue(outContent.toString().contains("You escaped! Congratulations!"));
    }
}
