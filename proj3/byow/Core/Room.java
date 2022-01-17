package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;
import java.util.Set;

public class Room {
    private Position p;
    private int width;
    private int height;

    /**
     * create a room with specific position, width and height.
     * */
    public Room(Position p, int x, int y) {
        this.p = p;
        width = x;
        height = y;
    }

    /**
     * randomly create room based on leaf size and RANDOM.
     * */
    public static Room randomCreate(Leaf space, Random random) {
        int w = RandomUtils.uniform(random, 6, space.width - 2);
        int h = RandomUtils.uniform(random, 6, space.height - 2);
        int x = RandomUtils.uniform(random, 1, space.width - w);
        int y = RandomUtils.uniform(random, 1, space.height - h);
        Position p = space.p.shift(x, -y);
        return new Room(p, w, h);
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
    public void roomPrint(TETile[][] world) {
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
     * find room in set by floor [i, j].
     * */
    public static Room roomByFloor(Set<Room> rooms, int i, int j) {
        for (Room r : rooms) {
            if (r.p.x < i && r.p.y > j && r.p.x + r.width - 1 > i && r.p.y - r.height + 1 > j) {
                return r;
            }
        }
        return null;
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
