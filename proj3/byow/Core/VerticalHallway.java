package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/**
 * vertical hallway.
 * */
public class VerticalHallway extends Room{
    int hallWidth;
    public VerticalHallway(Position p, int x, int y) {
        super(p, x, y);
        hallWidth = x;
    }

    /**
     * practically print hallway.
     * */
    public void hallwayPrint(TETile[][] world) {
        //set hallway floor
        for (int i = p.x + 1; i < right(); i++) {
            for (int j = p.y - 1; j > bottom(); j--) {
                world[i][j] = Tileset.FLOOR;
            }
        }
        //set hallway wall
        for (int i = p.y; i >= bottom(); i--) {
            hallwayPrintHelper(world, p.x, i);
            hallwayPrintHelper(world, right(), i);
        }
        //solve open hallway case.
        for (int i = 1; i <= hallWidth - 2; i++){
            hallwayPrintHelper(world, p.x + i, p.y);
            hallwayPrintHelper(world, p.x + i, bottom());
        }
    }

}
