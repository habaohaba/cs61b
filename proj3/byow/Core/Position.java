package byow.Core;

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
