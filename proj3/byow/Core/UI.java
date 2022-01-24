package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

public class UI {
    /**
     * get position of mouse.
     * */
    public static Position getMouseP() {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        return new Position(x, y);
    }

    public static void HUD(TETile[][] world) {
        Position mouseP = getMouseP();
        int x = mouseP.x;
        int y = mouseP.y;
        TETile pick = world[x][y];
        if (pick != Tileset.NOTHING) {

        } else {
            
        }
    }
}
