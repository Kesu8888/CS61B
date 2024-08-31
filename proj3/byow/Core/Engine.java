package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.io.File;
import static byow.Core.Utils.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;
    public static World world;
    public static TETile[][] Tile;
    public static File worldsStore = join(System.getProperty("user.dir"), "wordsStore");
    public static String Seed;
    public Engine() {
        if (!worldsStore.exists()) {
            worldsStore.mkdirs();
        }
    }
    public void interactWithKeyboard() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] Interface = UI.Starter(WIDTH, HEIGHT);
        ter.renderFrame(Interface);
        while (true) {
            StringBuilder sb = new StringBuilder();
            //Make sure the user has keyed in something
            if (StdDraw.hasNextKeyTyped()) {
                Seed = sb.append(StdDraw.nextKeyTyped()).toString();
                instruction("-k");
            }
        }
    }

    public void interactWithInputString(String input) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] Interface = UI.Starter(WIDTH, HEIGHT);
        ter.renderFrame(Interface);
        Seed = input;
        while (Seed.length() > 0) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException IE) {
                //
            }
            instruction("-s");
        }
    }

    public void instruction(String mode) {
        if (world == null) {
            switch (Character.toLowerCase(Seed.charAt(0))) {
                case 'n':
                    Seed = Seed.substring(1);
                    String seedStr = "";
                    if (mode.equals("-s")) {
                        seedStr = getSeedWithString();
                    } else {
                        seedStr = getSeedWithKeyboard();
                    }
                    try {
                        world = worldCreation(Integer.parseInt(seedStr));
                    } catch (NumberFormatException nf) {
                        System.out.println("the seed is not a correct number");
                    }
                    Tile = world.Tile();
                    ter.renderFrame(Tile);
                    return;
                case 'l':
                    File loading = join(worldsStore, "loading");
                    if (loading.exists()) {
                        world = readObject(loading, World.class);
                    }
                    break;
                case 'q':
                    break;
            }
        } else {
            switch (Character.toLowerCase(Seed.charAt(0))) {
                case 'w':
                    Tile = world.move('w');
                    break;
                case 'a':
                    Tile = world.move('a');
                    break;
                case 's':
                    Tile = world.move('s');
                    break;
                case 'd':
                    Tile = world.move('d');
                    break;
                case ':':
                    Seed = Seed.substring(1);
                    if (mode.equals("-s")) {

                    } else {

                    }
                    break;
            }
            if (Tile == null) {
                reward(ter);
                gameClear();
            }
            ter.renderFrame(Tile);
        }
    }

    private String getSeedWithString() {
        StringBuilder sb = new StringBuilder();
        while (Character.isDigit(Seed.charAt(0)) && Seed.length() > 0) {
            sb.append(Seed.charAt(0));
            Seed = Seed.substring(1);
        }
        return sb.toString();
    }

    private String getSeedWithKeyboard() {
        boolean isDigit = true;
        StringBuilder sb = new StringBuilder();
        ter.renderFrame(UI.seedEnteringUI(WIDTH, HEIGHT, sb.toString()));
        while (isDigit) {
            if(StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (Character.isDigit(c)) {
                    sb.append(c);
                    ter.renderFrame(UI.seedEnteringUI(WIDTH, HEIGHT, sb.toString()));
                } else {
                    isDigit = false;
                }
            }
        }
        return sb.toString();
    }

    private static World worldCreation(int seed) {
        World newWorld = new World(seed, WIDTH, HEIGHT);
        newWorld.construct();
        return newWorld;
    }

    private static void reward(TERenderer ter) {
        ter.renderFrame(UI.VoidArea(WIDTH, HEIGHT));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException IE) {
            //
        }
    }

    private static void worldNotExist(char S) {
    }
    private static void gameClear() {
        File loading = join(worldsStore, "loading");
        loading.delete();
        world = null;
        Tile = null;
    }
}
