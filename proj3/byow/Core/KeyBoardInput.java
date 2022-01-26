package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

/**
 * class represent keyboard input.
 * */
public class KeyBoardInput implements InputSource{

    @Override
    public char getNextKey() {
        return StdDraw.nextKeyTyped();
    }

    @Override
    public boolean possibleNextInput() {
        return StdDraw.hasNextKeyTyped();
    }
}
