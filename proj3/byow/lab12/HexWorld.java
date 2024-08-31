package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Iterator;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    public class HexObject {
        HexObject left;
        HexObject right;
        HexObject bot;
    }
    private TETile randomTile() {
        Random random = new Random();
        int tileNum = random.nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            default: return Tileset.NOTHING;
        }
    }
    private HexObject manyHex(int height) {
        HexObject initial = new HexObject();
        HexObject leftPointer = initial;
        for (int i = 0 ; i< height; i++) {
            leftPointer.left = new HexObject();
            leftPointer = leftPointer.left;
            HexObject botPointer = leftPointer;
            for (int z = 0; z < 3 - i; z++) {
                botPointer.bot = new HexObject();
                botPointer = botPointer.bot;
            }
        }
        HexObject rightPointer = initial;
        for (int i = 0 ; i< height; i++) {
            rightPointer.right = new HexObject();
            rightPointer = rightPointer.right;
            HexObject botPointer = rightPointer;
            for (int z = 0; z < 3 - i; z++) {
                botPointer.bot = new HexObject();
                botPointer = botPointer.bot;
            }
        }
        HexObject botPointer = initial;
        for (int i = 0; i< 4; i++) {
            botPointer.bot = new HexObject();
            botPointer = botPointer.bot;
        }
        return initial;
    }
    private TETile[][] drawHex(TETile[][] Tile, int x, int y, TETile randomTile) {
         for (int i = 0; i < 3; i ++) {
             for (int i2 = 0; i2 < 2*(i + 1); i2++) {
                 System.out.println(y - i);
                 Tile[x - i + i2][y - i] = randomTile;
             }
         }

        for (int i = 3; i < 6; i ++) {
            for (int i2 = 0; i2 < 2*(6 - i); i2++) {
                Tile[x - 5 + i + i2][y - i] = randomTile;
            }
        }
        return Tile;
    }
    public TETile[][] hexGeneration(int width, int length) {
        HexObject hex = manyHex(3);
        TETile[][] world = new TETile[width][length];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < length; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        return hexRecursion(hex, world, width/2 - 1, length - 1);
    }
    public TETile[][] hexRecursion(HexObject hexGroup, TETile[][] Tile, int x, int y) {
        Tile = drawHex(Tile, x, y, randomTile());
        if (hexGroup.left != null) {
            System.out.println("I am left");
            Tile = hexRecursion(hexGroup.left, Tile, x - 3, y - 3);
        }
        if (hexGroup.bot !=null) {
            System.out.println("I am bot");
            Tile = hexRecursion(hexGroup.bot, Tile, x, y - 6);
        }
        if (hexGroup.right !=null) {
            System.out.println("I am right");
            Tile = hexRecursion(hexGroup.right, Tile, x + 3, y - 3);
        }
        return Tile;
    }
    public static void main(String[] Args) {
        HexWorld world = new HexWorld();
        TERenderer ter = new TERenderer();
        ter.initialize(80, 60);
        ter.renderFrame(world.hexGeneration(80, 60));
    }
}

