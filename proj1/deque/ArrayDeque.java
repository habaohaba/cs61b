package deque;

import java.util.Iterator;

/** create a class ArrayDeque is Deque, Iterable.
 *  @author lin zhuo */
public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    /** the array to store items. */
    private T[] items;
    /** the size of the array. */
    private int size;
    /** the index of the next first item. */
    private int nextFirst;
    /** the index of the next last item. */
    private int nextLast;
    /** make an empty ArrayDeque. */
    public ArrayDeque() {
        items = (T []) new Object[8];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }
    /** change the amount of the array.
     *  first item is on the 0th location
     * @param desAmount the amount that need
     * */
    private void reAmount(int desAmount) {
        T[] newObject = (T[]) new Object[desAmount];
        for (int i = 0, j = nextFirst; i < size; i++) {
            if (j == items.length - 1) {
                j = 0;
                newObject[i] = items[j];
            } else {
                newObject[i] = items[j + 1];
                j++;
            }
        }
        items = newObject;
        nextFirst = items.length - 1;
        nextLast = size;
    }
    /** add item at first of array.
     *  @param item item to be put */
    public void addFirst(T item) {
        if (nextLast == nextFirst) {
            reAmount(items.length * 2);
        }
        items[nextFirst] = item;
        int newNextFirst = (nextFirst - 1) % items.length;
        if (newNextFirst < 0) {
            nextFirst = newNextFirst + items.length;
        } else {
            nextFirst = newNextFirst;
        }
        size++;
    }
    /** add item at last of array.
     *  @param item item to be put */
    public void addLast(T item) {
        if (nextLast == nextFirst) {
            reAmount(items.length * 2);
        }
        items[nextLast] = item;
        nextLast = (nextLast + 1) % items.length;
        size++;
    }
    /** return the size of instance.
     * @return size of instance */
    public int size() {
        return size;
    }
    /** print instance in required style. */
    public void printDeque() {
        for (int i = 0, j = nextFirst; i < size; i++) {
            System.out.print(items[(j + 1) % items.length] + " ");
        }
        System.out.println();
    }
    /** remove first item in the array.
     * @return item */
    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            T out = get(0);
            nextFirst = (nextFirst + 1) % items.length;
            size--;
            if (4 * size <= items.length && items.length >= 16) {
                reAmount(items.length / 2);
            }
            return out;
        }
    }
    /** remove last item in the array.
     * @return item */
    public T removeLast() {
        if (size == 0) {
            return null;
        } else {
            T out = get(size - 1);
            int newNextLast = (nextLast - 1) % items.length;
            if (newNextLast < 0) {
                nextLast = newNextLast + items.length;
            } else {
                nextLast = newNextLast;
            }
            size--;
            if (4 * size <= items.length && items.length >= 16) {
                reAmount(items.length / 2);
            }
            return out;
        }
    }
    /** get specific index item.
     * @param index int
     * @return item */
    public T get(int index) {
        if (index > size - 1) {
            return null;
        } else {
            return items[(nextFirst + index + 1) % items.length];
        }
    }
    /** class of iterator for the ArrayDeque. */
    private class ArrayDequeIterator implements Iterator<T> {
        /** index of the iterator. */
        private int index;
        /** initialize the iterator. */
        ArrayDequeIterator() {
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return (index < size - 1);
        }

        @Override
        public T next() {
            T out = get(index);
            index++;
            return out;
        }

    }
    /** return an iterator.
     * @return Iterator */
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }
    /** check if o equal this instance. */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (o instanceof Deque && ((Deque<T>) o).size() == size) {
            for (int i = 0; i < size; i++) {
                if (!((Deque<T>) o).get(i).equals(this.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
