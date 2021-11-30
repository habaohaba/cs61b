package deque;
//import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

/** Test for the MaxArrayDeque and exercise for the comparator.
 * @author lin zhuo  */
public class MaxArrayDequeTest {
    @Test
    /* basic test for the MaxArrayDeque. */
    public void basicTest() {
        Comparator<Integer> testComparator = new MaxComparator<>();
        MaxArrayDeque<Integer> testMaxArrayDeque = new MaxArrayDeque<>(testComparator);
        for (int i = 0; i < 100; i++) {
            testMaxArrayDeque.addLast(i);
        }
        assertEquals(99, (int) testMaxArrayDeque.max());
    }
}
