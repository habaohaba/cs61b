package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * class to create world
 * @source : https://gamedevelopment.tutsplus.com/tutorials/how-to-use-bsp-trees-to-generate-game-maps--gamedev-12268
 * @author lin zhuo
 * */
public class CreateWorld {

    /**
     * create world
     * */
    public static void make(TETile[][] world, Random random) {
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
    }

    /**
     * create 2D world index when debugging.
     * */
    private static void indexHelper(TETile[][] world) {
        for(int i = 4; i < world.length; i = i + 5) {
            for (int j = 4; j < world[0].length; j = j + 5) {
                world[i][j] = Tileset.FLOWER;
            }
        }
    }
}
