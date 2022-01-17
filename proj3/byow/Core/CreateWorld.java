package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

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

    /**
     * set mark all connected rooms and hallways.
     * */
    private Set<Object> connected;

    public CreateWorld(TETile[][] world, Random random) {
        rooms = new HashSet<>();
        hallways = new HashSet<>();
        connected = new HashSet<>();
        //create num rooms, num between 30 and 40
        int roomNum = RandomUtils.uniform(random, 30, 40);
        Room.room(world, random, roomNum, rooms);

        TERenderer test = new TERenderer();
        test.initialize(world.length,  world[0].length);
        test.renderFrame(world);

        //if there is room unconnected, create hallway.
        while(!connected()) {
            Hallway.hallway(world, random, rooms, connected);
            test.renderFrame(world);
        }
    }

    /**
     * check whether all the room get connected.
     * */
    private boolean connected() {
        for (Room r : rooms) {
            if (!connected.contains(r)) {
                return false;
            }
        }
        return true;
    }
    /**
     * main method to create world.
     * */
    public static void make(TETile[][] world, Random random) {
        CreateWorld w = new CreateWorld(world, random);
    }
}
