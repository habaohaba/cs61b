package byow.Core;

import afu.org.checkerframework.checker.oigj.qual.O;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;
import java.util.Set;

/**
 * horizontal hallway
 * */
public class HorizontalHallway extends Hallway{
    public HorizontalHallway(TETile[][] world, Random random) {
        super(world, random);
    }

    /**
     * set one hallway in world.
     * */
    public static void hallway(TETile[][] world, Random random, Set<Room> rooms, Set<Object> connected) {
        HorizontalHallway n = new HorizontalHallway(world, random);
        while (!n.valid(world, rooms, connected)) {
            n = new HorizontalHallway(world, random);
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
        if (rightUp().x > wWidth - 1 || leftBottom().y < 0) {
            return false;
        }
        //connect things.
        for (int i = p.x + 1; i < rightUp().x; i++) {
            for (int j = p.y - 1; j > leftBottom().y; j--) {
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
        for (int i = p.x; i <= rightUp().x; i++) {
            helper(world, i, p.y);
            helper(world, i, leftBottom().y);
        }
        //solve open hallway case.
        for (int i = 1; i <= width - 2; i++){
            helper(world, p.x, p.y - i);
            helper(world, rightUp().x, p.y - i);
        }
    }

    private Position rightUp() {
        return new Position(p.x + length - 1 ,p.y);
    }
    private Position rightBottom() {
        return new Position(p.x + length - 1, p.y - width + 1);
    }
    private Position leftBottom() {
        return new Position(p.x, p.y - width + 1);
    }

}
