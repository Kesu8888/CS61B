package byow.lab13;

import byow.Core.RandomUtils;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;
import java.util.Scanner;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private int seed;
    private boolean gameContinue;
    public final myTile Nothing = new myTile(' ', Color.BLACK, Color.BLACK);
    /**
     * Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'.
     */
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /**
     * Encouraging phrases. Used in the last section of the spec, 'Helpful UI'.
     */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
        "You got this!", "You're a star!", "Go Bears!",
        "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        /*if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }*/

        //long seed = Long.parseLong(args[0]);
        long seed = 20;
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.seed = (int)seed;
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }
    public String stringGenerator(int n) {
        String s = "";
        for (int i = 0; i < n; i++) {
            s = s + CHARACTERS[random(26)*seed%26];
        }
        return s;
    }
    public void frameReset() {
        StdDraw.clear(Color.BLACK);
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                myTile tile = Nothing;
                tile.draw(x, y);
            }
        }
    }

    public void flashSequence(String letter) {
        for (char c : letter.toCharArray()) {
            myTile tile = new myTile(c, Color.GREEN, Color.BLUE);
            frameReset();
            tile.draw(width / 2, height / 2);
            StdDraw.show();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // pause for a while
            }
            frameReset();
            StdDraw.show();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                //pause for a while
            }
        }
    }

    public String solicitNCharsInput(int n) {
        /*Scanner inputChar = new Scanner(System.in);
        String s = inputChar.nextLine();
        return s;*/
        char[] words = "Please enter the keys in 5 seconds".toCharArray();
        for (int i = 0; i < words.length; i++) {
            myTile Tile = new myTile(words[i], Color.WHITE, Color.BLACK);
            Tile.draw((width-words.length)/2 + i, height/2);
        }
        StdDraw.show();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // pause for a while
        }
        String s = "";
        while (StdDraw.hasNextKeyTyped()) {
            s = s + StdDraw.nextKeyTyped();
        }
        showWords(s, Color.YELLOW, Color.BLACK);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // pause for a while
        }
        return s;
    }

    public void startGame() {
        gameContinue = true;
        round = 1;
        StdDraw.show();
        while (gameContinue && round < 6) {
            encouraging(ENCOURAGEMENT[random(7)*seed%7]);
            String randomString = stringGenerator(round + 3);
            frameReset();
            flashSequence(randomString);
            gameContinue = solicitNCharsInput(round + 3).equals(randomString);
            if (gameContinue) {
                showWords("GOOD JOB", Color.GREEN, Color.BLACK);
            } else {
                showWords("SO REGRET", Color.RED, Color.BLACK);
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // pause for a while
            }
            round = round + 1;
        }
        showWords(ENCOURAGEMENT[random(ENCOURAGEMENT.length) * seed % ENCOURAGEMENT.length],
            Color.WHITE, Color.BLACK);
        System.out.println("Finished");
    }
    //////
    //////
    //Helper method
    public int random(int bound) {
        Random random = new Random();
        return random.nextInt(bound);
    }
    public class myTile {
        private final char character;
        private final Color textColor;
        private final Color backgroundColor;

        public myTile(char character, Color textColor, Color backgroundColor) {
            this.textColor = textColor;
            this.backgroundColor = backgroundColor;
            this.character = character;
        }

        public void draw(double x, double y) {
            edu.princeton.cs.introcs.StdDraw.setPenColor(backgroundColor);
            edu.princeton.cs.introcs.StdDraw.filledSquare(x + 0.5, y + 0.5, 0.5);
            edu.princeton.cs.introcs.StdDraw.setPenColor(textColor);
            edu.princeton.cs.introcs.StdDraw.text(x + 0.5, y + 0.5, Character.toString(character));
        }
    }
    public void encouraging(String words) {
        int length = words.length();
        char[] wordsArray = words.toCharArray();
        for (int i = 0; i < wordsArray.length; i++) {
            myTile Tile = new myTile(wordsArray[i], Color.CYAN, Color.BLACK);
            Tile.draw(width-1-(length - i), height-3);
        }
    }
    public void showWords(String words, Color textColor, Color backgroundColor) {
        frameReset();
        for(int i = 0; i < words.length(); i++) {
            myTile Tile = new myTile(words.charAt(i), textColor, backgroundColor);
            Tile.draw((width - words.length())/2 + i, height/2);
        }
        StdDraw.show();
    }
}
