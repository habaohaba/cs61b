package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class UI {
    public TERenderer renderer;
    public TETile[][] world;

    public UI(TERenderer t, TETile[][] w) {
        renderer = t;
        world = w;
    }

    /**
     * get position of mouse.
     * */
    private Position getMouseP() {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        //protect from mouse index out of bound.
        if (x == world.length) {
            x = x - 1;
        }
        if (y == world[0].length) {
            y = y - 1;
        }
        return new Position(x, y);
    }

    /**
     * HUD display.
     * */
    public void HUD() {
        TETile pick = currentTile();
        if (pick != Tileset.NOTHING) {
            HudType(pick);
        } else {
            renderer.renderFrame(world);
        }
        while (currentTile() == pick) {
            StdDraw.pause(250);
        }
    }
    private TETile currentTile() {
        Position mouseP = getMouseP();
        int x = mouseP.x;
        int y = mouseP.y;
        return world[x][y];
    }
    /**
     * show information based on tile type.
     * */
    private void HudType(TETile x) {
        double width = world.length;
        double height = world[0].length;
        //set black HUD background
        StdDraw.setPenColor(Color.black);
        StdDraw.filledRectangle(width/2, height - 0.5, width/2, 0.5);
        //set white bottom line
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.line(0, height - 1, width, height - 1);
        //show information
        if (x.equals(Tileset.WALL)) {
            StdDraw.textLeft(0, height - 0.5, "wall");
        } else if (x.equals(Tileset.FLOOR)) {
            StdDraw.textLeft(0, height - 0.5, "floor");
        } else if (x.equals(Tileset.AVATAR)) {
            StdDraw.textLeft(0, height - 0.5, "avatar");
        }
        StdDraw.show();
    }
}
