package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class Room {
    private final int size;
    private final RoomNode start;
    private ArrayList<RoomNode> floors = new ArrayList<>(0);
    private int current;
    private final int Width;
    private final int Height;
    private final String direct;
    private TETile[][] Tile;
    private Random random;

    public Room(int size, RoomNode startLocation, int Width, int Height, String direction, Random random) {
        this.size = size;
        start = startLocation;
        floors.add(start);
        this.Width = Width;
        this.Height = Height;
        direct = direction;
        this.random = random;
    }
    
    public TETile[][] startConstruct(TETile[][] original) {
        System.out.println("Start X = " + start.X + "Start Y = " + start.Y);
        if (Tile != null) {
            return Tile;
        }
        original[floors.get(current).X][floors.get(current).Y] = Tileset.FLOOR;
        this.current = 0;
        while (floors.size() < size && current >= 0) {
            RoomNode nextLocation = possibleNextLocation(floors.get(current), direct, original);
            if (nextLocation != null) {
                original[nextLocation.X][nextLocation.Y] = Tileset.FLOOR;
                floors.add(nextLocation);
                current = floors.size() - 1;
            } else {
                current = current - 1;
            }
        }
        if (floors.size() >= size) {
            Tile = original;
            // For test only
            makeWalls();
            return Tile;
        } else {
            return null;
        }
    }
    
    public Room nextRoom(TETile[][] original, int size) {
        ArrayList<String> directions = new ArrayList<>();
        directions.add("Left");
        directions.add("Right");
        directions.add("Up");
        directions.add("Down");
        directions.remove(oppositeDirection());
        String nextRoomDirect = directions.get(RandomUtils.uniform(random, directions.size()));
        Room nextRoom = new Room(size, getMost(nextRoomDirect), Width, Height, nextRoomDirect, random);
        //Check all directions are valid
        while (nextRoom.startConstruct(original) == null) {
            directions.remove(nextRoomDirect);
            if (directions.size() < 1) {
                return null;
            }
            nextRoomDirect = directions.get(RandomUtils.uniform(random, directions.size()));
            nextRoom = new Room(size, getMost(nextRoomDirect), Width, Height, nextRoomDirect, random);
        }
        return nextRoom;
    }

    public RoomNode createLoot() {
        RoomNode roomOrDoor = floors.get(RandomUtils.uniform(random, floors.size()));
        return roomOrDoor;
    }

    public TETile[][] returnTile() {
        return Tile;
    }
    /////////Private methods
    private boolean chance(int numerator, int denominator) {
        return random.nextInt(denominator) < numerator;
    }

    //Pass in a list of next possible locations, and select on randomly
    private RoomNode randomFloor(RoomNode[] roomNodes) {
        for (int i = 0; i < roomNodes.length; i++) {
            if (chance(1, roomNodes.length)) {
                return roomNodes[i];
            }
        }
        return null;
    }

    private String oppositeDirection() {
        return switch (direct) {
            case "Left" -> "Right";
            case "Up" -> "Down";
            case "Down" -> "Up";
            default -> "Left";
        };
    }
    private boolean validFloor(TETile[][] original, RoomNode point) {
        if (point.X < 1 || point.X > original.length - 2) {
            return false;
        }
        if (point.Y < 1 || point.Y > original[0].length - 2) {
            return false;
        }
        return original[point.X][point.Y] == Tileset.NOTHING | original[point.X][point.Y] == Tileset.WALL;
    }

    private void makeWalls() {
        for (RoomNode rm : floors) {
            for (RoomNode surr: surrounding(rm)) {
                if (Tile[surr.X][surr.Y] == Tileset.NOTHING) {
                    System.out.println("Wall X = " + surr.X + "Wall Y = " + surr.Y);
                    Tile[surr.X][surr.Y] = Tileset.WALL;
                }
            }
        }
    }

    private RoomNode[] surrounding(RoomNode rm) {
        RoomNode[] surrounding = new RoomNode[8];
        for (int i = 0; i < 3; i++) {
            surrounding[i] = new RoomNode(rm.X - 1 + i, rm.Y + 1);
        }
        surrounding[3] = new RoomNode(rm.X - 1, rm.Y);
        surrounding[4] = new RoomNode(rm.X + 1, rm.Y);
        for (int i = 5; i < 8; i++) {
            surrounding[i] = new RoomNode(rm.X - 1 + i - 5, rm.Y - 1);
        }
        return surrounding;
    }
    //Find the valid possible next locations and sort them in the preferred growing location.
    private RoomNode possibleNextLocation(RoomNode current, String sortingDirection, TETile[][] original) {
        int start = 0;
        int end = 3;
        RoomNode[] possibleLocation = new RoomNode[4];
        possibleLocation[0] = new RoomNode(current.X+1, current.Y);
        possibleLocation[1] = new RoomNode(current.X-1, current.Y);
        possibleLocation[2] = new RoomNode(current.X, current.Y+1);
        possibleLocation[3] = new RoomNode(current.X, current.Y-1);
        // remove invalid points
        while (start <= end) {
            if (validFloor(original, possibleLocation[start])) {
                start += 1;
            } else {
                possibleLocation[start] = possibleLocation[end];
                possibleLocation[end] = null;
                end -= 1;
            }
        }
        RoomNode[] sortedNodes = sortNodes(Arrays.copyOfRange(possibleLocation, 0, start)
            , sortingDirection);
        return randomFloor(sortedNodes);
    }

    private RoomNode[] sortNodes(RoomNode[] points, String direction) {
        for (int i = 0; i < points.length; i++) {
            switch (direction) {
                case "Left":
                    points = sortLeft(i, points);
                    break;
                case "Right":
                    points = sortRight(i, points);
                    break;
                case "Up":
                    points = sortUp(i, points);
                    break;
                case "Down":
                    points = sortDown(i, points);
                    break;
            }
        }
        return points;
    }
    
    private RoomNode[] sortLeft(int current, RoomNode[] points) {
        while (current > 0) {
            if (points[current].X < points[current - 1].X) {
                RoomNode temp = points[current];
                points[current] = points[current - 1];
                points[current - 1] = temp;
                current--;
            } else if (points[current].X == points[current - 1].X) {
                if (Math.abs(start.Y - points[current].Y) > Math.abs(start.Y - points[current - 1].Y)) {
                    RoomNode temp = points[current];
                    points[current] = points[current - 1];
                    points[current - 1] = temp;
                }
                return points;
            } else {
                return points;
            }
        }
        return points;
    }

    private RoomNode[] sortRight(int current, RoomNode[] points) {
        while (current > 0) {
            if (points[current].X > points[current - 1].X) {
                RoomNode temp = points[current];
                points[current] = points[current - 1];
                points[current - 1] = temp;
                current--;
            } else if (points[current].X == points[current - 1].X) {
                if (Math.abs(start.Y - points[current].Y) > Math.abs(start.Y - points[current - 1].Y)) {
                    RoomNode temp = points[current];
                    points[current] = points[current - 1];
                    points[current - 1] = temp;
                }
                return points;
            } else {
                return points;
            }
        }
        return points;
    }

    private RoomNode[] sortUp(int current, RoomNode[] points) {
        while (current > 0) {
            if (points[current].Y > points[current - 1].Y) {
                RoomNode temp = points[current];
                points[current] = points[current - 1];
                points[current - 1] = temp;
                current--;
            } else if (points[current].Y == points[current - 1].Y) {
                if (Math.abs(start.X - points[current].X) > Math.abs(start.X - points[current - 1].X)) {
                    RoomNode temp = points[current];
                    points[current] = points[current - 1];
                    points[current - 1] = temp;
                }
                return points;
            } else {
                return points;
            }
        }
        return points;
    }

    private RoomNode[] sortDown(int current, RoomNode[] points) {
        while (current > 0) {
            if (points[current].Y < points[current - 1].Y) {
                RoomNode temp = points[current];
                points[current] = points[current - 1];
                points[current - 1] = temp;
                current--;
            } else if (points[current].Y == points[current - 1].Y) {
                if (Math.abs(start.X - points[current].X) > Math.abs(start.X - points[current - 1].X)) {
                    RoomNode temp = points[current];
                    points[current] = points[current - 1];
                    points[current - 1] = temp;
                }
                return points;
            } else {
                return points;
            }
        }
        return points;
    }
    
    private RoomNode getMost(String direction) {
        switch (direction) {
            case "Left":
                RoomNode leftMost = floors.get(0);
                for (RoomNode rm: floors) {
                    if (rm.X < leftMost.X) {
                        leftMost = rm;
                    }
                }
                return leftMost;
            case "Right":
                RoomNode rightMost = floors.get(0);
                for (RoomNode rm: floors) {
                    if (rm.X > rightMost.X) {
                        rightMost = rm;
                    }
                }
                return rightMost;
            case "Up":
                RoomNode upMost = floors.get(0);
                for (RoomNode rm: floors) {
                    if (rm.Y > upMost.Y) {
                        upMost = rm;
                    }
                }
                return upMost;
            default :
                RoomNode downMost = floors.get(0);
                for (RoomNode rm: floors) {
                    if (rm.Y < downMost.Y) {
                        downMost = rm;
                    }
                }
                return downMost;
        }
    }
}

