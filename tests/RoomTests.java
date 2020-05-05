package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import maze.Room;
import maze.Room.Direction;


public class RoomTests
{
    Room fakeRoom;

    @Before
    public void setup_fakeRoom()
    {
        this.fakeRoom = new Room(null, null, null, null);
    }

    @Test
    public void test_noRooms()
    {
        Room testRoom = new Room(null, null, null, null);

        // Confirming this room's connections
        assertTrue(testRoom(testRoom, null, Direction.UP));
        assertTrue(testRoom(testRoom, null, Direction.RIGHT));
        assertTrue(testRoom(testRoom, null, Direction.DOWN));
        assertTrue(testRoom(testRoom, null, Direction.LEFT));

        // Confirming display
        assertEquals("*--*\n|  |\n*--*", testRoom.toString());

        // Confirming open connections
        Room[] connections = testRoom.getOpenConnections();
        assertTrue(connections.length == 0);
    }

    @Test
    public void test_oneRoom()
    {
        Room testRoom = new Room(this.fakeRoom, null, null, null);

        // Confirming this room's connections
        assertTrue(testRoom(testRoom, this.fakeRoom, Direction.UP));
        assertTrue(testRoom(testRoom, null, Direction.RIGHT));
        assertTrue(testRoom(testRoom, null, Direction.DOWN));
        assertTrue(testRoom(testRoom, null, Direction.LEFT));

        // Confirming other room connections
        assertTrue(testRoom(this.fakeRoom, testRoom, Direction.DOWN));

        // Confirming display
        assertEquals("*  *\n|  |\n*--*", testRoom.toString());

        // Confirming open connections
        Room[] connections = testRoom.getOpenConnections();
        assertTrue(connections.length == 1);
        assertEquals(this.fakeRoom, connections[0]);
    }

    @Test
    public void test_twoRooms()
    {
        Room testRoom = new Room(this.fakeRoom, this.fakeRoom, null, null);

        // Confirming this room's connections
        assertTrue(testRoom(testRoom, this.fakeRoom, Direction.UP));
        assertTrue(testRoom(testRoom, this.fakeRoom, Direction.RIGHT));
        assertTrue(testRoom(testRoom, null, Direction.DOWN));
        assertTrue(testRoom(testRoom, null, Direction.LEFT));

        // Confirming other room connections
        assertTrue(testRoom(this.fakeRoom, testRoom, Direction.DOWN));
        assertTrue(testRoom(this.fakeRoom, testRoom, Direction.LEFT));

        // Confirming display
        assertEquals("*  *\n|   \n*--*", testRoom.toString());

        // Confirming open connections
        Room[] connections = testRoom.getOpenConnections();
        assertTrue(connections.length == 2);
        assertEquals(this.fakeRoom, connections[0]);
        assertEquals(this.fakeRoom, connections[1]);
    }

    @Test
    public void test_threeRooms()
    {
        Room testRoom = new Room(this.fakeRoom, this.fakeRoom, this.fakeRoom, null);

        // Confirming this room's connections
        assertTrue(testRoom(testRoom, this.fakeRoom, Direction.UP));
        assertTrue(testRoom(testRoom, this.fakeRoom, Direction.RIGHT));
        assertTrue(testRoom(testRoom, this.fakeRoom, Direction.DOWN));
        assertTrue(testRoom(testRoom, null, Direction.LEFT));

        // Confirming other room connections
        assertTrue(testRoom(this.fakeRoom, testRoom, Direction.DOWN));
        assertTrue(testRoom(this.fakeRoom, testRoom, Direction.LEFT));
        assertTrue(testRoom(this.fakeRoom, testRoom, Direction.UP));

        // Confirming display
        assertEquals("*  *\n|   \n*  *", testRoom.toString());

        // Confirming open connections
        Room[] connections = testRoom.getOpenConnections();
        assertTrue(connections.length == 3);
        assertEquals(this.fakeRoom, connections[0]);
        assertEquals(this.fakeRoom, connections[1]);
        assertEquals(this.fakeRoom, connections[2]);
    }

    @Test
    public void test_fourRooms()
    {
        Room testRoom = new Room(this.fakeRoom, this.fakeRoom, this.fakeRoom, this.fakeRoom);

        // Confirming this room's connections
        assertTrue(testRoom(testRoom, this.fakeRoom, Direction.UP));
        assertTrue(testRoom(testRoom, this.fakeRoom, Direction.RIGHT));
        assertTrue(testRoom(testRoom, this.fakeRoom, Direction.DOWN));
        assertTrue(testRoom(testRoom, this.fakeRoom, Direction.LEFT));

        // Confirming other room connections
        assertTrue(testRoom(this.fakeRoom, testRoom, Direction.DOWN));
        assertTrue(testRoom(this.fakeRoom, testRoom, Direction.LEFT));
        assertTrue(testRoom(this.fakeRoom, testRoom, Direction.UP));
        assertTrue(testRoom(this.fakeRoom, testRoom, Direction.RIGHT));

        // Confirming display
        assertEquals("*  *\n    \n*  *", testRoom.toString());

        // Confirming open connections
        Room[] connections = testRoom.getOpenConnections();
        assertTrue(connections.length == 4);
        assertEquals(this.fakeRoom, connections[0]);
        assertEquals(this.fakeRoom, connections[1]);
        assertEquals(this.fakeRoom, connections[2]);
        assertEquals(this.fakeRoom, connections[3]);
    }

    @Test
    public void test_closeRoom()
    {
        Room testRoom = new Room(this.fakeRoom, null, null, null);

        assertTrue(testRoom(testRoom, this.fakeRoom, Direction.UP));

        testRoom.closeDoor(Direction.UP);
        assertFalse(testRoom.canTravelTo(Direction.UP));
        assertFalse(this.fakeRoom.canTravelTo(Direction.DOWN));
    }

    private boolean testRoom(Room origin, Room target, Direction dir)
    {
        // Checking identity
        if (origin.getRoomIn(dir) != target) {
            return false;
        }

        // Checking if travel is possible
        if (target == null) {
            return !origin.canTravelTo(dir); // Should be false
        } else {
            return origin.canTravelTo(dir); // Should be true
        }
    }
}
