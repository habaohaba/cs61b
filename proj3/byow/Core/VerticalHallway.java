package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;
import java.util.Set;

/**
 * vertical hallway.
 * */
public class VerticalHallway extends Hallway{
    public VerticalHallway(TETile[][] world, Random random) {
        super(world, random);
    }

    /**
     * set one hallway in world.
     * */
    public static void hallway(TETile[][] world, Random random, Set<Room> rooms, Set<Object> connected) {
        VerticalHallway n = new VerticalHallway(world, random);
        while (!n.valid(world, rooms, connected)) {
            n = new VerticalHallway(world, random);
        }
        n.hallwayPrint(world);
    }

    /**
     * check hallway is in the scope and connect things.
     * which means hallway floor meet other floor.
     * */
    public boolean valid(TETile[][] world, Set<Room> rooms, Set<Object> connected) {
        int wWidth = world.length;
        //in scope.
        if (p.x + width > wWidth || p.y - length + 1 < 0) {
            return false;
        }
        //connect things.
        for (int i = p.x + 1; i < p.x + width - 1; i++) {
            for (int j = p.y - 1; j > p.y - length + 1; j--) {
                if (world[i][j] == Tileset.FLOOR) {
                    //mark room and hallway connected.
                    Room r = Room.roomByFloor(rooms, i, j);
                    if (r != null) {
                        return connected.add(r);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * practically print hallway.
     * */
    public void hallwayPrint(TETile[][] world) {
        //set hallway floor
        for (int i = p.x + 1; i < rightUp().x; i++) {
            for (int j = p.y - 1; j > leftBottom().y; j--) {
                world[i][j] = Tileset.FLOOR;
            }
        }
        //set hallway wall
        for (int i = p.y; i >= leftBottom().y; i--) {
            helper(world, p.x, i);
            helper(world, rightBottom().x, i);
        }
        //solve open hallway case.
        for (int i = 1; i <= width - 2; i++){
            helper(world, p.x + i, p.y);
            helper(world, p.x + i, leftBottom().y);
        }
    }

    private Position rightUp() {
        return new Position(p.x + width - 1 ,p.y);
    }
    private Position rightBottom() {
        return new Position(p.x + width - 1, p.y - length + 1);
    }
    private Position leftBottom() {
        return new Position(p.x, p.y - length + 1);
    }

}
