package deque;


import java.util.Comparator;

/** MaxArrayDeque that produce max item.
 * @author lin zhuo */
public class MaxArrayDeque<T> extends ArrayDeque<T> {
    /** the comparator. */
    private Comparator<T> comp;
    /** initialize a MaxArrayDeque.
     * @param c comparator */
    public MaxArrayDeque(Comparator<T> c) {
        comp = c;
    }
    /**  return max item. */
    public T max() {
        if (size() == 0) {
            return null;
        }
        T max = get(0);
        for (int i = 1; i < size(); i++) {
            T current = get(i);
            if (comp.compare(max, current) < 0) {
                max = current;
            }
        }
        return max;
    }
    /** return max item.
     * @param c comparator. */
    public T max(Comparator<T> c) {
        if (size() == 0) {
            return null;
        }
        comp = c;
        T max = get(0);
        for (int i = 1; i < size(); i++) {
            T current = get(i);
            if (comp.compare(max, current) < 0) {
                max = current;
            }
        }
        return max;
    }
}
