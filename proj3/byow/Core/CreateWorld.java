package byow.Core;

import byow.TileEngine.TETile;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CreateWorld {
    /**
     * all the rooms in world.
     * */
    private Set<Room> rooms;
    /**
     * all the hallways in world.
     * */
    private Set<Hallway> hallways;

    private Set<Leaf> leaves;

    public CreateWorld() {
        rooms = new HashSet<>();
        hallways = new HashSet<>();
        leaves = new HashSet<>();
    }

    /**
     * main method to create world.
     * */
    public static void make(TETile[][] world, Random random) {
        CreateWorld w = new CreateWorld();
        w.createRoom(world, random);
    }

    /**
     * create leaves based on world and RANDOM.
     * */
    private void createRoom(TETile[][] world, Random random) {
        int maxLeafSize = 20;
        int wWidth = world.length;
        int wHeight= world[0].length;
        Position rootP = new Position(0, wHeight - 1);
        Leaf root = new Leaf(rootP, wWidth, wHeight);
        leaves.add(root);
        boolean didSplit= true;
        while (didSplit) {
            didSplit = false;
            Set<Leaf> helperSet = new HashSet<>(leaves);
            for (Leaf l : helperSet) {
                if (l.leftChild == null && l.rightChild == null) {
                    if (l.width > maxLeafSize || l.height > maxLeafSize) {
                        if (l.split(random)) {
                            leaves.add(l.leftChild);
                            leaves.add(l.rightChild);
                            didSplit = true;
                        }
                    }
                }
            }
        }
        root.createRoom(world, random);
    }
}
