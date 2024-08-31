package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.*;

public class World implements Serializable {
    private final Room[] roomList;
    private final int maxRoomSize;
    private TETile[][] original;
    private final int offSetX;
    private final int Width;
    private final int Height;
    private final Random random;
    private RoomNode start;
    private boolean hasLoot = false;
    private RoomNode loot;
    private World next;

    public World(int seed, int Width, int Height) {
        random = new Random(seed);
        offSetX = 2;
        this.Width = Width;
        this.Height = Height - offSetX;
        int numberOfRooms = RandomUtils.uniform(random, 5, 20);
        maxRoomSize = Width*Height*2/numberOfRooms/5;
        roomList = new Room[numberOfRooms];
        if (RandomUtils.uniform(random, 2) < 1) {
            hasLoot = true;
        }
    }
    public void construct() {
        if (original != null) {
            return;
        }
        /* Initialize the tiles */
        original = new TETile[Width][Height];
        for (int x = 0; x < Width; x++) {
            for (int y = 0; y < Height; y++) {
                original[x][y] = Tileset.NOTHING;
            }
        }
        start = startLocation();
        constructRooms();
        loot = constructLoot();
        original[start.X][start.Y] = Tileset.AVATAR;
    }

    public TETile[][] move(char movement) {
        RoomNode nextMove = moveToNode(movement);
        if (isLoot(nextMove)) {
            return null;
        }
        if (validFloor(nextMove)) {
            original[start.X][start.Y] = Tileset.FLOOR;
            original[nextMove.X][nextMove.Y] = Tileset.AVATAR;
            start = nextMove;
        }
        return original;
    }
    public RoomNode moveToNode(char movement) {
        RoomNode nextMove;
        switch (movement) {
            case 'w':
                nextMove = new RoomNode(start.X, start.Y + 1);
                break;
            case 's':
                nextMove = new RoomNode(start.X, start.Y - 1);
                break;
            case 'a':
                nextMove = new RoomNode(start.X - 1, start.Y);
                break;
            default:
                nextMove = new RoomNode(start.X + 1, start.Y);
                break;
        }
        return nextMove;
    }
    
    public boolean isLoot(RoomNode rm) {
        return rm.X == loot.X && rm.Y == loot.Y;
    }
    public TETile[][] Tile() {
        return original;
    }

    private RoomNode constructLoot() {
        RoomNode lootOrDoor = roomList[RandomUtils.uniform(random, roomList.length)].createLoot();
        if (hasLoot) {
            original[lootOrDoor.X][lootOrDoor.Y] = Tileset.LOOT;
        } else {
            original[lootOrDoor.X][lootOrDoor.Y] = Tileset.UNLOCKED_DOOR;
        }
        return lootOrDoor;
    }

    private void constructRooms() {
        roomList[0] = new Room(RandomUtils.uniform(random, maxRoomSize), start, Width,
            Height, getDirection(start), random);
        original = roomList[0].startConstruct(original);
        int i1 = 0;
        for (int i = 1; i < roomList.length; i++) {
            int size = RandomUtils.uniform(random, maxRoomSize);
            roomList[i] = roomList[i1].nextRoom(original, size);
            while (roomList[i] == null) {
                i1++;
                if (i1 >= i) {
                    System.out.println("cannot find a place for new room");
                }
                roomList[i] = roomList[i1].nextRoom(original, size);
            }
            original = roomList[i].returnTile();
        }
    }

    private boolean validFloor(RoomNode nextLocation) {
        return original[nextLocation.X][nextLocation.Y] == Tileset.FLOOR;
    }
    
    private RoomNode startLocation() {
        int startX = RandomUtils.uniform(random, Width);
        int startY;
        if (startX < Width/8 | startX > Width*7/8) {
            startY = RandomUtils.uniform(random, Height);
        } else {
            if (RandomUtils.uniform(random, 2) < 1) {
                startY = RandomUtils.uniform(random, Height/6);
            } else {
                startY = RandomUtils.uniform(random, Height*3/4, Height);
            }
        }
        return new RoomNode(startX, startY);
    }

    private String getDirection(RoomNode startLocation) {
        int X = startLocation.X;
        int Y = startLocation.Y;
        if (X < Width/4) {
            return "Right";
        } else if (X > Width/4 && Width < Width*3/4) {
            if (Y < Height/3) {
                return "Up";
            } else if (Y > Height*2/3) {
                return "down";
            } else if (X < Width/2) {
                return "Right";
            } else {
                return "Left";
            }
        } else {
            return "Left";
        }
    }
}
