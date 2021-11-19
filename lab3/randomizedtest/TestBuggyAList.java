package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    AListNoResizing<Integer> a = new AListNoResizing<>();
    BuggyAList<Integer> b = new BuggyAList<>();

    @Test
    public void testThreeAddThreeRemove() {
        a.addLast(4);
        a.addLast(5);
        a.addLast(6);
        b.addLast(4);
        b.addLast(5);
        b.addLast(6);
        assertEquals(a.removeLast(), b.removeLast());
        assertEquals(a.removeLast(), b.removeLast());
        assertEquals(a.removeLast(), b.removeLast());
    }

    @Test
    public void randomizedTest() {
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                a.addLast(randVal);
                b.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                assertEquals(a.size(), b.size());
            } else if (operationNumber == 2){
                if (a.size() != 0){
                    assertEquals(a.getLast(), b.getLast());
                }
            } else if (operationNumber == 3){
                if (a.size() != 0){
                    a.removeLast();
                    b.removeLast();
                }
            }
        }
    }
}
