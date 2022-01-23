package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import java.util.Random;

/**
 * partition world into leaves. Binary partition.
 * @author lin zhuo
 * */
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
    /**
     * room in this leaf if existed.
     * */
    public Room room;

    /**
     * constructor
     * */
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
        int split = RandomUtils.uniform(random, MIN_SIZE, max + 1);
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
     * recursively find end leaf and print room in it.
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
            //create room.
            Room r = randomCreateR(random);
            room = r;
            r.roomPrint(world);
        }
    }

    /**
     * for every leaf find two rooms in separate leaf and build hallway connect it.
     * */
    public void createHallway(TETile[][] world, Random random) {
        if (leftChild != null && rightChild != null) {
            Room room1 = leftChild.getRoom(random);
            Room room2 = rightChild.getRoom(random);
            assert room1 != null;
            Position p1 = room1.randomCreateP(random);
            assert room2 != null;
            Position p2 = room2.randomCreateP(random);
            connectPoint(p1, p2, random, world);
        }
    }

    /**
     *  randomly find room beneath current room.
     * */
    private Room getRoom(Random random) {
        if (room != null) {
            return room;
        } else {
            Room leftRoom = null;
            Room rightRoom = null;
            //find room
            if (leftChild != null) {
                leftRoom = leftChild.getRoom(random);
            }
            if (rightChild != null) {
                rightRoom = rightChild.getRoom(random);
            }
            //return room
            if (leftRoom == null && rightRoom == null) {
                return null;
            } else if (leftRoom == null) {
                return rightRoom;
            } else if (rightRoom == null) {
                return  leftRoom;
            } else if (RandomUtils.uniform(random) > 0.5) {
                return leftRoom;
            } else {
                return rightRoom;
            }
        }
    }

    /**
     * connect two points using hallway and print hallway.
     * */
    private void connectPoint(Position p1, Position p2, Random random, TETile[][] world) {
        int dx = p1.x - p2.x;
        int dy = p1.y - p2.y;
        int w1 = randomWidth(random);
        int w2 = randomWidth(random);
        if (dx < 0) {
            dx = Math.abs(dx);
            if (dy < 0) {
                dy = Math.abs(dy);
                if (RandomUtils.uniform(random) > 0.5) {
                    HorizontalHallway room1 = new HorizontalHallway(p1.shift(0, w1), dx, w1);
                    VerticalHallway room2 = new VerticalHallway(p2.shift(-w2, 0), w2, dy);
                    room1.hallwayPrint(world);
                    room2.hallwayPrint(world);
                } else {
                    VerticalHallway room1 = new VerticalHallway(p1.shift(0, dy), w1, dy);
                    HorizontalHallway room2 = new HorizontalHallway(p2.shift(-dx, 0), dx, w2);
                    room1.hallwayPrint(world);
                    room2.hallwayPrint(world);
                }
            } else if (dy > 0) {
                if (RandomUtils.uniform(random) > 0.5) {
                    HorizontalHallway room1 = new HorizontalHallway(p1, dx, w1);
                    VerticalHallway room2 = new VerticalHallway(p2.shift(-w2, dy), w2, dy);
                    room1.hallwayPrint(world);
                    room2.hallwayPrint(world);
                } else {
                    VerticalHallway room1 = new VerticalHallway(p1, w1, dy);
                    HorizontalHallway room2 = new HorizontalHallway(p2.shift(-dx, w2), dx, w2);
                    room1.hallwayPrint(world);
                    room2.hallwayPrint(world);
                }
            } else {
                HorizontalHallway room = new HorizontalHallway(p1, dx, w1);
                room.hallwayPrint(world);
            }
        } else if (dx > 0) {
            if (dy < 0) {
                dy = Math.abs(dy);
                if (RandomUtils.uniform(random) > 0.5) {
                    HorizontalHallway room1 = new HorizontalHallway(p1.shift(-w1, -dx), dx, w1);
                    VerticalHallway room2 = new VerticalHallway(p2, w2, dy);
                    room1.hallwayPrint(world);
                    room2.hallwayPrint(world);
                } else {
                    VerticalHallway room1 = new VerticalHallway(p1.shift(-w1, -dy), w1, dy);
                    HorizontalHallway room2 = new HorizontalHallway(p2, dx, w2);
                    room1.hallwayPrint(world);
                    room2.hallwayPrint(world);
                }
            } else if (dy > 0) {
                if (RandomUtils.uniform(random) > 0.5) {
                    HorizontalHallway room1 = new HorizontalHallway(p1.shift(-dx, 0), dx, w1);
                    VerticalHallway room2 = new VerticalHallway(p2.shift(0, dy), w2, dy);
                    room1.hallwayPrint(world);
                    room2.hallwayPrint(world);
                } else {
                    VerticalHallway room1 = new VerticalHallway(p1.shift(-w1, 0), w1, dy);
                    HorizontalHallway room2 = new HorizontalHallway(p2.shift(0, w2), dx, w2);
                    room1.hallwayPrint(world);
                    room2.hallwayPrint(world);
                }
            } else {
                HorizontalHallway room = new HorizontalHallway(p2, dx, w2);
            }
        } else {
            if (dy > 0) {
                VerticalHallway room = new VerticalHallway(p1, w1, dy);
                room.hallwayPrint(world);
            } else if (dy < 0) {
                dy = Math.abs(dy);
                VerticalHallway room = new VerticalHallway(p2, w2, dy);
                room.hallwayPrint(world);
            }
        }
    }

    /**
     * create width between 3, 4.
     * */
    private int randomWidth(Random random) {
        return RandomUtils.uniform(random, 3,5);
    }

    /**
     * randomly create room based on leaf size and RANDOM.
     * */
    private Room randomCreateR(Random random) {
        //width between 6, space.width - 2
        int w = RandomUtils.uniform(random, 6, width - 1);
        //height between 6, space.height - 2
        int h = RandomUtils.uniform(random, 6, height - 1);
        //dx between 1, space.width - width - 1
        int dx = RandomUtils.uniform(random, 1, width - w);
        //dy between 1, space.height - height - 1
        int dy = RandomUtils.uniform(random, 1, height - h);
        Position c = p.shift(dx, -dy);
        return new Room(c, w, h);
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
