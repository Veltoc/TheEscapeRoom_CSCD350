package tests;

import maze.Main;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class MovementTests
{
    @Test
    public void test_MoveRooms() {
        InputStream fakeIn = new ByteArrayInputStream("down\nAdminPass\nq".getBytes());
        System.setIn(fakeIn);
        Main.main(null);
        // Confirming still at start
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
        InputStream fakeIn = new ByteArrayInputStream("down\nAdminFail\ndown\nq".getBytes());
        System.setIn(fakeIn);
        Main.main(null);
        // Confirming still at start
        assertEquals(Main.getCurrentRoom(),Main.getMaze().getStart());
    }

}