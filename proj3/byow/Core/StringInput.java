package byow.Core;

public class StringInput implements InputSource {
    private String input;
    private int index;

    public StringInput(String s) {
        input = s;
        index = 0;
    }

    @Override
    public char getNextKey() {
        char c = input.charAt(index);
        index += 1;
        return c;
    }

    @Override
    public boolean possibleNextInput() {
        return index < input.length();
    }
}
