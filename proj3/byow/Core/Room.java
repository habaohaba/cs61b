package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;
import java.util.Set;

public class Room {
    public Position p;
    public int width;
    public int height;

    /**
     * create a room with specific position, width and height.
     * */
    public Room(Position p, int x, int y) {
        this.p = p;
        width = x;
        height = y;
    }


    /**
     * randomly create position in room.
     * */
    public Position randomCreateP(Random random) {
        int dx = RandomUtils.uniform(random, 2, width - 2);
        int dy = RandomUtils.uniform(random,  2, height - 2);
        return p.shift(dx, -dy);
    }

    /**
     * practically print room on world.
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
     * return room bottom y index.
     * */
    public int bottom() {
        return p.y - height + 1;
    }
    /**
     * return room right x index.
     * */
    public int right() {
        return p.x + width - 1;
    }

    /**
     * set wall if [i, j] is empty.
     * */
    public void hallwayPrintHelper(TETile[][] world, int i, int j) {
       if (world[i][j] == Tileset.NOTHING) {
           world[i][j] = Tileset.WALL;
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
