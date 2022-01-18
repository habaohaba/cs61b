package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/**
 * horizontal hallway
 * */
public class HorizontalHallway extends Room{
    int hallWidth;
    public HorizontalHallway(Position p, int x, int y) {
        super(p, x, y);
        hallWidth = y;
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
        for (int i = p.x; i <= right(); i++) {
            hallwayPrintHelper(world, i, p.y);
            hallwayPrintHelper(world, i, bottom());
        }
        //solve open hallway case.
        for (int i = 1; i <= hallWidth - 2; i++){
            hallwayPrintHelper(world, p.x, p.y - i);
            hallwayPrintHelper(world, right(), p.y - i);
        }
    }
}
