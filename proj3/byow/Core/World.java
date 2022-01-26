package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import static byow.Core.Utils.*;

/**
 * class to manipulate world.
 * @source : https://gamedevelopment.tutsplus.com/tutorials/how-to-use-bsp-trees-to-generate-game-maps--gamedev-12268
 * @author lin zhuo
 * */
public class World implements Serializable {
    /**
     * 2D map
     * */
    public TETile[][] world;
    /**
     * random to use
     * */
    public Random random;
    /**
     * position of avatar
     * */
    public Position avatar;

    /**
     * setup basic world width and height.
     * fill with nothing.
     * */
    public World(int x, int y) {
        world = new TETile[x][y];
        fillWithNothing();
    }

    /**
     * create world
     * */
    public void create() {
        //test render
        //TERenderer test = new TERenderer();
        //test.initialize(80, 80);

        //main algorithm
        Set<Leaf> leaves = new HashSet<>();
        int maxLeafSize = 20;

        //create root leaf which is the same size of world
        int wWidth = world.length;
        int wHeight= world[0].length;
        Position rootP = new Position(0, wHeight - 1);
        Leaf root = new Leaf(rootP, wWidth, wHeight);
        leaves.add(root);

        //binary partition world and store every leaf.
        boolean didSplit= true;
        while (didSplit) {
            didSplit = false;
            Set<Leaf> helperSet = new HashSet<>(leaves);
            for (Leaf l : helperSet) {
                if (l.leftChild == null && l.rightChild == null) {
                    if (l.width > maxLeafSize || l.height > maxLeafSize) {
                        //split leaf
                        if (l.split(random)) {
                            leaves.add(l.leftChild);
                            leaves.add(l.rightChild);
                            didSplit = true;
                        }
                    }
                }
            }
        }

        //create rooms.
        root.createRoom(world, random);

        //indexHelper(world);

        //create hallways
        for (Leaf l : leaves) {
            l.createHallway(world, random);
        }
        createAvatar(root.getRoom(random));
    }

    public void move(char m) {
        if (m == 'w' || m == 'W') {

        } else if (m == 'a' || m == 'A') {

        } else if (m == 's' || m == 'S') {

        } else if (m == 'd' || m == 'D') {

        }
    }
    private void moveHelper() {
        int x
    }
    /**
     * save current state to file f;
     * */
    public void saveState(File f) {
        writeObject(f, this);
    }

    /**
     * read file f as a new world.
     * */
    public void readState(File f) {
         World w = readObject(f, World.class);
         world = w.world;
         random = w.random;
         avatar = w.avatar;
    }

    /**
     * fill world with NOTHING.
     * */
    private void fillWithNothing() {
        int width = world.length;
        int height = world[0].length;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j< height; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    /**
     * create avatar.
     * */
    private void createAvatar(Room r) {
        boolean done = false;
        int x = r.p.x;
        int y = r.p.y;
        while (!done) {
            int target = RandomUtils.uniform(random, 0, r.width + r.height);
            for (int i = 0; i < r.width; i++) {
                for (int j = 0; j < r.height; j++) {
                    if ((i + j) == target && world[i + x][j + y] != Tileset.WALL && !done) {
                        world[i + x][j + y] = Tileset.AVATAR;
                        avatar = new Position(i + x, j + y);
                        done = true;
                    }
                }
            }
        }
    }

    /**
     * create 2D world visible index when debugging.
     * */
    private void indexHelper() {
        for(int i = 4; i < world.length; i = i + 5) {
            for (int j = 4; j < world[0].length; j = j + 5) {
                world[i][j] = Tileset.FLOWER;
            }
        }
    }
}
