package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;
import java.util.Set;

public class Hallway {
    public int length;
    public int width;
    public Position p;
    public boolean connected = false;

    /**
     * randomly create hallway.
     * */
    public Hallway(TETile[][] world, Random random) {
        //hallway anchored position randomly created.
        Position p = Position.randomCreate(world, random);
        //hallway length between 5 and 10.
        int l = RandomUtils.uniform(random, 5, 15);
        //hallway width between 3 and 5.
        int w = RandomUtils.uniform(random, 3, 5);
        this.p = p;
        this.length = l;
        this.width = w;
    }

    /**
     * put one hallway in world randomly horizontal or vertical.
     * */
    public static void hallway(TETile[][] world, Random random, Set<Room> rooms, Set<Object> connected) {
        int type = RandomUtils.uniform(random, 0, 2);
        if (type == 0) {
            HorizontalHallway.hallway(world, random, rooms, connected);
        } else {
            VerticalHallway.hallway(world, random, rooms, connected);
        }
    }

    /**
     * if tile[i, j] is empty, set it as wall.
     * */
    public void helper(TETile[][] world, int i, int j) {
        if (world[i][j] == Tileset.NOTHING) {
            world[i][j] = Tileset.WALL;
        }
    }
    @Override
    public boolean equals(Object o) {
        Hallway n = (Hallway) o;
        return length == n.length && width == n.width && p.equals(n.p);
    }
    @Override
    public int hashCode() {
        return length + width + p.y + p.x;
    }
}
