package byow.Core;

import afu.org.checkerframework.checker.oigj.qual.O;
import byow.TileEngine.TETile;

import java.util.Random;

public class Leaf {
    private final static int MIN_SIZE = 10;
    /**
     * anchored position.
     * */
    public Position p;
    public int width;
    public int height;
    public Leaf leftChild;
    public Leaf rightChild;

    public Leaf(Position p, int w, int h) {
        this.p = p;
        width = w;
        height = h;
    }

    /**
     * randomly split current leaf.
     * */
    public boolean split(Random random) {
        if (leftChild != null || rightChild != null) {
            //already split.
            return false;
        }
        //choose split horizontally or vertically
        boolean splitH = RandomUtils.uniform(random) > 0.5;
        if (width > height && width / (double) height >= 1.25) {
            splitH = false;
        } else if (height > width && height / (double) width >= 1.25) {
            splitH = true;
        }
        int max = (splitH ? height : width) - MIN_SIZE;
        if (max < MIN_SIZE) {
            //area too small
            return false;
        }
        //randomly choose split number
        int split = RandomUtils.uniform(random, MIN_SIZE, max);
        //split
        if (splitH) {
            leftChild = new Leaf(p, width, split);
            rightChild = new Leaf(p.shift(0, -split), width, height - split);
        }
        else {
            leftChild = new Leaf(p, split, height);
            rightChild = new Leaf(p.shift(split, 0), width - split, height);
        }
        return true;
    }

    /**
     * recursively find leaf and print room in it.
     * */
    public void createRoom(TETile[][] world, Random random) {
        if (leftChild != null || rightChild != null) {
            if (leftChild != null) {
                leftChild.createRoom(world, random);
            }
            if (rightChild != null) {
                rightChild.createRoom(world, random);
            }
        } else {
            Room r = Room.randomCreate(this, random);
            r.roomPrint(world);
        }
    }

    @Override
    public boolean equals(Object o) {
        Leaf l = (Leaf) o;
        return p.equals(l.p) && width == l.width && height == l.height
                && leftChild.equals(l.leftChild) && rightChild.equals(l.rightChild);
    }
    @Override
    public int hashCode() {
        return p.x + p.y + width + height;
    }
}
