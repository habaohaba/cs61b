package deque;

import java.util.Comparator;

/** create a comparator to test MaxArrayDeque.
 * @author lin zhuo  */
public class MaxComparator<T> implements Comparator<T> {
    /** comparator for int. */
    public int compare(T o1, T o2) {
        return (int) o1 - (int) o2;
    }
}
