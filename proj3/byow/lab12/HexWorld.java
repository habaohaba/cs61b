package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    private static class position {
        int x;
        int y;
        position(int x, int y) {
            this.x = x;
            this.y = y;
        }
        position shift(int dx, int dy) {
            return new position(x + dx, y + dy);
        }
    }

    /**
     * add a row
     * @param p : start position of row.
     * @param l : length of row.
     * */
    private static void drawRow(TETile[][] world, position p, TETile tileset, int l) {
        for (int i = 0; i < l; i++) {
            world[p.x + i][p.y] = tileset;
        }
    }

    /**
     * recursively add hexagon.
     * @param p : anchored position of hexagon.
     * */
    private static void addHexagonHelper(TETile[][] world, position p, TETile tileset, int b, int t) {
        position startOfRow = p.shift(b, 0);
        drawRow(world, startOfRow, tileset, t);

        if (b > 0) {
            position nextP = p.shift(0, -1);
            addHexagonHelper(world, nextP, tileset, b - 1, t + 2);
        }

        position startOfReflectedRow = startOfRow.shift(0, -(2*b + 1));
        drawRow(world, startOfReflectedRow, tileset, t);
    }
    /**
     * add specific length hexagon on position.
     * @param p : anchored position of hexagon.
     * @param length : length of hexagon.
     * */
    private static void addHexagon(TETile[][] world, int length, position p, TETile tileset) {
        addHexagonHelper(world, p, tileset, length - 1, length);
    }
    /**
     * add num column of specific size hexagon on position.
     * @param p : anchored position of column.
     * @param num : number of hexagon.
     * @param size : size of hexagon.
     * */
    private static void addColumn(TETile[][] world, position p, int size, int num) {
        if (num < 1) return;
        addHexagon(world, size, p, randomTile());
        if (num > 1) {
            position bottomNeighbor = getBottomNeighbor(p, size);
            addColumn(world, bottomNeighbor, size, num - 1);
        }
    }
    /**
     * the position under give n size hexagon.
     * */
    private static position getBottomNeighbor(position p, int n) {
        return p.shift(0, -2*n);
    }
    private static position getTopRightNeighbor(position p, int n) {
        return p.shift(2*n - 1, n);
    }
    private static position getBottomRightNeighbor(position p, int n) {
        return p.shift(2*n - 1, -n);
    }
    private static void drawAll(TETile[][] world, position p, int size, int tessSize) {
        for (int i = 0; i < tessSize; i++) {
            p = getTopRightNeighbor(p, size);
            addColumn(world, p, size, tessSize + i);
        }
        for (int i = 1; i < tessSize; i++) {
            p = getBottomRightNeighbor(p, size);
            addColumn(world, p, size, 2*tessSize - 1 - i);
        }
    }
    public static void fillWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.SAND;
            case 4: return Tileset.TREE;
            default: return Tileset.NOTHING;
        }
    }
    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        fillWithNothing(world);

        position p = new position(30,70);
        drawAll(world, p, 3, 4);

        // draws the world to the screen
        ter.renderFrame(world);
    }
}
