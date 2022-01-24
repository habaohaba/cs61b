package byow.Core;

import byow.TileEngine.TETile;

/**
 * Avatar user use to move.
 * @author lin zhuo
 * */
public class Avatar {
    Position p;
    TETile tile;

    public Avatar(Position p, TETile tile) {
        this.p = p;
        this.tile = tile;
    }

}
