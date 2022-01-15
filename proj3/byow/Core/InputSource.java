package byow.Core;

public interface InputSource {
    /**
     * output current key and increase index.
     * */
    char getNextKey();
    /**
     * check whether input have next key.
     * */
    boolean possibleNextInput();
}
