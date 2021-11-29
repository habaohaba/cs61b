package deque;

/** Deque API.
 * @author lin zhuo */
public interface Deque<T> {
    /** add item at first. */
    void addFirst(T item);
    /** add item at last. */
    void addLast(T item);
    /** check if is empty. */
    default boolean isEmpty() {
        return (size() == 0);
    }
    /** return size of instance. */
    int size();
    /** print Deque in required style. */
    void printDeque();
    /** remove and return the first item. */
    T removeFirst();
    /** remove and return the last item. */
    T removeLast();
    /** get specific index item. */
    T get(int index);
}
