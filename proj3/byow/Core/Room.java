package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;
import java.util.Set;
import java.util.Spliterators;

public class Room {
    private Position p;
    private int width;
    private int height;
    private boolean connected = false;

    /**
     * create a room with specific position, width and height.
     * */
    public Room(Position p, int x, int y) {
        this.p = p;
        width = x;
        height = y;
    }

    /**
     * put a room in world map if it doesn't conflict.
     * */
    public static void room(TETile[][] world, Random random, int num, Set<Room> roomSet) {
        int n = 0;
        while(n < num) {
            Room r = randomCreate(world, random);
            if (r.valid(world)) {
                r.roomPrint(world);
                roomSet.add(r);
                n++;
            }
        }
    }

    /**
     * randomly create integer between X and Y based on uniform.
     * */
    private static int randomCreate(Random random, int x , int y) {
        return RandomUtils.uniform(random, x, y);
    }
    /**
     * randomly create room based on world size and RANDOM.
     * */
    private static Room randomCreate(TETile[][] world, Random random) {
        int wWidth = world.length;
        int wHeight = world[0].length;
        int x = randomCreate(random, 0, wWidth);
        int y = randomCreate(random, 0, wHeight);
        //width of room between 5 and 10
        int w = randomCreate(random, 5, 10);
        //height of room between 5 and 10
        int h = randomCreate(random, 5, 10);
        Position p = new Position(x, y);
        Room n = new Room(p, w, h);
        return n;
    }

    /**
     * check whether room is valid.
     * in scope and do not conflict with other room.
     * */
    private boolean valid(TETile[][] world) {
        int w = world.length;
        //in scope
        if (p.x + width > w || p.y - height + 1 < 0) {
            return false;
        }
        //check whether conflict with other room.
        for (int i = p.x; i < p.x + width; i++) {
            for (int j = p.y; j > p.y - height; j--) {
                if (world[i][j] != Tileset.NOTHING) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * practically print room on map.
     * */
    private void roomPrint(TETile[][] world) {
        //print floor.
        for (int i = p.x + 1; i < p.x + width - 1; i++) {
            for (int j = p.y - 1; j > p.y - height + 1; j--) {
                world[i][j] = Tileset.FLOOR;
            }
        }
        //print wall.
        for (int i = p.x; i < p.x + width; i++) {
            world[i][p.y] = Tileset.WALL;
            world[i][p.y - height + 1] = Tileset.WALL;
        }
        for (int i = p.y; i > p.y - height; i--) {
            world[p.x][i] = Tileset.WALL;
            world[p.x + width - 1][i] = Tileset.WALL;
        }
    }

    /**
     * clear room, replace with NOTHING.
     * */
    public void roomClear(TETile[][] world) {
        for (int i = p.x; i < p.x + width; i++) {
            for (int j = p.y; j > p.y - height; j--) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        Room it = (Room) o;
        return this.p.equals(it.p) && this.width == it.width && this.height == it.height;
    }
    @Override
    public int hashCode() {
        return this.height + this.width + p.x + p.y;
    }
}
