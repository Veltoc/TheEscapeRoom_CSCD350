package maze;

import java.io.Serializable;
import java.util.ArrayList;

public class Room
implements Serializable
{
    private static final long serialVersionUID = -7819848474590916374L;

    public enum Direction {
        UP(0), RIGHT(1), DOWN(2), LEFT(3);
        int index;

        Direction(int index)
        {
            this.index = index;
        }

        public int getIndex()
        {
            return this.index;
        }

        public int getOppositeIndex()
        {
            return (this.index + 2) % 4;
        }
    }


    private Room[] connections;
    private boolean[] openConnections;


    public Room(Room top, Room right, Room bottom, Room left)
    {
        this.connections = new Room[4];
        this.openConnections = new boolean[4];

        setConnection(top, Direction.UP);
        setConnection(right, Direction.RIGHT);
        setConnection(bottom, Direction.DOWN);
        setConnection(left, Direction.LEFT);
    }
    
    @Override
    public String toString()
    {
        String[] rows = getDisplay(false);
        return rows[0] + "\n" + rows[1] + "\n" + rows[2];
    }

    public boolean canTravelTo(Direction dir)
    {
        if (openConnections[dir.getIndex()]) {
            return true;
        } else {
            return false;
        }
    }

    public void closeDoor(Direction dir)
    {
        // Closing this door
        this.openConnections[dir.getIndex()] = false;

        // Closing other door
        this.connections[dir.getIndex()].openConnections[dir.getOppositeIndex()] = false;
    }

    public void openDoor(Direction dir)
    {
        // Closing this door
        this.openConnections[dir.getIndex()] = true;

        // Closing other door
        this.connections[dir.getIndex()].openConnections[dir.getOppositeIndex()] = true;
    }

    public Room getRoomIn(Direction dir)
    {
        return connections[dir.getIndex()];
    }

    public String[] getDisplay(boolean isCurrentRoom)
    {
        String[] str = {
            String.format("*%s*", this.openConnections[Direction.UP.getIndex()] ? "  " : "--"),
            String.format("%s%s %s",
                    this.openConnections[Direction.LEFT.getIndex()] ? " " : "|",
                    isCurrentRoom ? "X" : " ",
                    this.openConnections[Direction.RIGHT.getIndex()] ? " " : "|"),
            String.format("*%s*", this.openConnections[Direction.DOWN.getIndex()] ? "  " : "--")
        };

        return str;
    }

    public Room[] getOpenConnections()
    {
        ArrayList<Room> openConnections = new ArrayList<Room>(4);

        for (int i = 0; i < 4; i++) {
            if (this.openConnections[i]) {
                openConnections.add(this.connections[i]);
            }
        }

        return openConnections.toArray(new Room[openConnections.size()]);
    }

    private void setConnection(Room room, Direction dir)
    {
        if (room != null) {
            // Creating this connection
            this.connections[dir.getIndex()] = room;
            this.openConnections[dir.getIndex()] = true;

            // Creating other connection
            room.connections[dir.getOppositeIndex()] = this;
            room.openConnections[dir.getOppositeIndex()] = true;
        }
    }
}
