package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CreateWorld {
    /**
     * all the rooms in the world.
     * */
    private Set<Room> rooms;

    public CreateWorld(TETile[][] world, Random random) {
        rooms = new HashSet<>();
        //create num rooms, num between 20 and 30
        int roomNum = RandomUtils.uniform(random, 30, 40);
        Room.room(world, random, roomNum, rooms);
    }

    /**
     * main method to create world.
     * */
    public static void make(TETile[][] world, Random random) {
        CreateWorld w = new CreateWorld(world, random);
    }
}
