package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import maze.Maze;
import maze.Room.Direction;

public class MazeTests
{
    Maze testMaze;

    @Before
    public void setup_maze()
    {
        this.testMaze = new Maze(4, 4);
    }

    @Test
    public void test_init1x1()
    {
        this.testMaze = new Maze(1, 1);

        // Confirming start/finish
        assertEquals(testMaze.getRoom(0, 0), testMaze.getStart());
        assertEquals(testMaze.getRoom(0, 0), testMaze.getFinish());

        // Confirming display
        assertEquals("*--*\n|  |\n*--*\n", testMaze.toString());
    }

    @Test
    public void test_init4x4()
    {
        this.testMaze = new Maze(4, 4);

        // Confirming start/finish
        assertEquals(testMaze.getRoom(0, 0), testMaze.getStart());
        assertEquals(testMaze.getRoom(3, 3), testMaze.getFinish());

        // Confirming display
        assertEquals("*--**--**--**--*\n|              |\n*  **  **  **  *\n*  **  **  **  *\n|              |\n*  **  **  **  *\n*  **  **  **  *\n|              |\n*  **  **  **  *\n*  **  **  **  *\n|              |\n*--**--**--**--*\n",
                testMaze.toString());
    }

    @Test
    public void test_openPath()
    {
        // Checking path
        assertTrue(testMaze.isPathToFinish(0, 0));
    }

    @Test
    public void test_closedPath()
    {
        // Closing doors
        testMaze.getRoom(0, 0).closeDoor(Direction.RIGHT);
        testMaze.getRoom(0, 0).closeDoor(Direction.DOWN);

        // Confirming no path
        assertFalse(testMaze.isPathToFinish(0, 0));
    }
}
