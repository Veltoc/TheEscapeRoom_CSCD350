package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import maze.Main;
import maze.Room.Direction;

public class SaveTests
{
    @AfterClass
    public static void cleanup()
    {
        File save = new File("game.sav");
        save.delete();
    }

    @Before
    public void createSave()
    {
        InputStream fakeIn = new ByteArrayInputStream("e\n/answer\ne\n/fail\nsave game\nq".getBytes());
        System.setIn(fakeIn);
        Main.main(null);
    }

    @Test
    public void test_load()
    {
        InputStream fakeIn = new ByteArrayInputStream("load game\nq".getBytes());
        System.setIn(fakeIn);
        Main.main(null);

        assertEquals(Main.getMaze().getRoom(1, 0), Main.getCurrentRoom());
        assertFalse(Main.getMaze().getRoom(1, 0).canTravelTo(Direction.RIGHT));
    }

    @Test
    public void test_badSave()
    {
        InputStream fakeIn = new ByteArrayInputStream("save\nq".getBytes());
        System.setIn(fakeIn);
        ByteArrayOutputStream fakeOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeOut));
        Main.main(null);

        assertTrue(fakeOut.toString().contains("Please enter a file name."));
    }

    @Test
    public void test_badLoad()
    {
        InputStream fakeIn = new ByteArrayInputStream("load\nq".getBytes());
        System.setIn(fakeIn);
        ByteArrayOutputStream fakeOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeOut));
        Main.main(null);

        assertTrue(fakeOut.toString().contains("Please enter a file name."));
    }
}
