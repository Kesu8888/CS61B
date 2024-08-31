package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;

public class UI {
    public static final TETile[][] Starter(int maxWidth,int maxHeight) {
        TETile[][] Starter= VoidArea(maxWidth, maxHeight);
        String[] gameDesc = new String[2];
        gameDesc[0] = "Build Your";
        gameDesc[1] = "Own Fantasy World";
        Starter = addWords(Starter, maxWidth, maxHeight*3/4, gameDesc);
        String[] gameMode = new String[3];
        gameMode[0] = "Press N to start a new game";
        gameMode[1] = "Press L to load a game";
        gameMode[2] = "Press Q to Quit a game";
        Starter = addWords(Starter, maxWidth, maxHeight/2, gameMode);
        return Starter;
    }
    private static TETile[][] addWords(TETile[][] original, int Width, int Height, String[] lines) {
        for (int linesNo = 0; linesNo < lines.length; linesNo++) {
            for (int i = 0; i < lines[linesNo].length(); i++) {
                original[(Width - lines[linesNo].length()) / 2 + i][Height - linesNo]
                    = new TETile(lines[linesNo].charAt(i), Color.WHITE, Color.BLACK, "Starter");
            }
        }
        return original;
    }
    public static TETile[][] VoidArea(int maxWidth,int maxHeight) {
        TETile[][] voidSpace= new TETile[maxWidth][maxHeight];
        for (int x = 0; x < maxWidth; x++) {
            for (int y = 0; y < maxHeight; y++) {
                voidSpace[x][y] = Tileset.NOTHING;
            }
        }
        return voidSpace;
    }
    public static TETile[][] seedEnteringUI(int maxWidth, int maxHeight, String seed) {
        TETile[][] seedEnteringUI = VoidArea(maxWidth, maxHeight);
        String[] seedEnteringWords = new String[2];
        seedEnteringWords[0] = "Please enter your Words, End with any non integer character i.e., E";
        seedEnteringWords[1] = "You have entered   " + seed;
        return addWords(seedEnteringUI, maxWidth, maxHeight/2, seedEnteringWords);
    }
}
