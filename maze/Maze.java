package maze;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class Maze
implements Serializable
{
    private static final long serialVersionUID = 1917404425459845405L;

    private int width;
    private int height;
    private Room[][] rooms;
    private Room start;
    private Room finish;


    public Maze(int width, int height)
    {
        this.width = width;
        this.height = height;
        initMaze();
    }

    @Override
    public String toString()
    {
        return getDisplay(null);
    }

    public boolean isPathToFinish(int originX, int originY)
    {
        return isPathBetween(getRoom(originX, originY), getFinish());
    }

    public boolean isPathToFinish(Room current)
    {
        return isPathBetween(current, getFinish());
    }

    public Room getRoom(int x, int y)
    {
        return this.rooms[y][x];
    }

    public Room getStart()
    {
        return this.start;
    }

    public Room getFinish()
    {
        return this.finish;
    }

    public String getDisplay(Room currentRoom)
    {
        StringBuilder str = new StringBuilder(this.width * 4 * this.height * 4);
        StringBuilder row1 = new StringBuilder(this.width * 4);
        StringBuilder row2 = new StringBuilder(this.width * 4);
        StringBuilder row3 = new StringBuilder(this.width * 4);
        String[] roomDisp;

        for (int y = 0; y < this.height; y++) {
            row1.delete(0, row1.length());
            row2.delete(0, row2.length());
            row3.delete(0, row3.length());

            for (int x = 0; x < this.width; x++) {
                roomDisp = this.rooms[y][x].getDisplay(this.rooms[y][x] == currentRoom);
                row1.append(roomDisp[0]);
                row2.append(roomDisp[1]);
                row3.append(roomDisp[2]);
            }

            str.append(row1.toString() + "\n" + row2.toString() + "\n" + row3.toString() + "\n");
        }

        return str.toString();
    }

    private void initMaze()
    {
        this.rooms = initRooms();

        this.start = this.rooms[0][0];
        this.finish = this.rooms[this.height - 1][this.width - 1];
    }

    private Room[][] initRooms()
    {
        Room[][] rooms = new Room[this.height][this.width];
        Room up, left;

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                // Getting connections
                up = null;
                if (y > 0) {
                    up = rooms[y - 1][x];
                }

                left = null;
                if (x > 0) {
                    left = rooms[y][x - 1];
                }

                // Creating room
                // Down and right are ignored because
                // later rooms will add that connection
                rooms[y][x] = new Room(up, null, null, left);
            }
        }

        return rooms;
    }

    private boolean isPathBetween(Room origin, Room goal)
    {
        ArrayList<Room> exploredRooms = new ArrayList<Room>();
        ArrayDeque<Room> roomPath = new ArrayDeque<Room>();
        exploredRooms.add(origin);
        roomPath.push(origin);

        Room curRoom;
        while (roomPath.size() > 0) {
            // Checking for goal
            curRoom = roomPath.pop();
            if (curRoom == goal) {
                return true;
            }

            // Adding adjacent rooms
            for (Room room: curRoom.getOpenConnections()) {
                if (!exploredRooms.contains(room)) {
                    exploredRooms.add(room);
                    roomPath.add(room);
                }
            }
        }

        return false;
    }
}
