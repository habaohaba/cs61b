package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import static java.awt.event.KeyEvent.*;

import java.io.File;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 80;
    public static final File saveFile = new File("savefile.txt");

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        //create world and filled with NOTHING.
        World world = new World(WIDTH, HEIGHT);
        //seed.
        StringBuilder seed = new StringBuilder();
        //manipulate input.
        InputSource in = new StringInput(input);
        while (in.possibleNextInput()) {
            char x = in.getNextKey();
            //capture seed and create world.
            if (x == 'N' || x == 'n') {
                x = in.getNextKey();
                while (x != 's' && x != 'S') {
                    seed.append(x);
                    x = in.getNextKey();
                }
                world.random = new Random(Long.parseLong(seed.toString()));
                //create original world.
                world.create();
            } else if (x == 'l' || x == 'L') {
                //load last state.
                world.readState(saveFile);
            } else if (x == ':') {
                //save and quit.
                x = in.getNextKey();
                if (x == 'Q' || x == 'q') {
                    world.saveState(saveFile);
                    break;
                }
            }
        }
        return world.world;
    }

    /**
     * test main method.
     * */
    public static void main(String[] args) {
        //create engine
        Engine engine = new Engine();
        engine.ter.initialize(WIDTH, HEIGHT);
        //main test
        TETile[][] test = engine.interactWithInputString(args[0]);
        engine.ter.renderFrame(test);
        while (!StdDraw.isKeyPressed(VK_Q)) {
            UI.HUD(test);
        }
    }
}
