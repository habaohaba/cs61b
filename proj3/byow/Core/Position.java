package byow.Core;

import byow.TileEngine.TETile;

import java.util.Random;

public class Position {
    public int x;
    public int y;

    /**
     * create a position.
     * */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * randomly create position in scope.
     * */
    public static Position randomCreate(TETile[][] world, Random random) {
        int wWidth = world.length;
        int wHeight = world[0].length;
        int x = RandomUtils.uniform(random, 0, wWidth);
        int y = RandomUtils.uniform(random, 0, wHeight);
        return new Position(x, y);
    }

    /**
     * return a new position based on this position
     * change x, y.
     * */
    public Position shift(int x, int y) {
        return new Position(this.x + x, this.y + y);
    }
    @Override
    public boolean equals(Object o) {
        Position p = (Position) o;
        return this.x == p.x && this.y == p.y;
    }
}
