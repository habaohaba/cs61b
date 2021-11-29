package deque;

import java.util.Iterator;

/** double link list.
 * @author lin zhuo */
public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    /** define node class. */
    private class Node {
        /** item of the node. */
        private T item;
        /** next node. */
        private Node next;
        /** last node. */
        private Node last;
        /** initialize a node.
         * @param i item
         * @param n next node
         * @param l last node */
        private Node(T i, Node n, Node l) {
            item = i;
            next = n;
            last = l;
        }
    }
    /** sentinel is the helper node. */
    private Node sentinel;
    /** size of the list. */
    private int size;
    /** initialize LinkListDeque. */
    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.last = sentinel;
        size = 0;
    }
    /** helper method for the getRecursive.
     *  @param start start node
     *  @param index index to find */
    private T getRecursive(Node start, int index) {
        if (index == 0) {
            return start.item;
        } else {
            return getRecursive(start.next, index - 1);
        }
    }
    /** get the index value by recursive method.
     * @param index required index
     * @return item */
    public T getRecursive(int index) {
        if (index > size - 1) {
            return null;
        }
        return getRecursive(sentinel.next, index);
    }
    /** add item in the first location.
     * @param item item */
    public void addFirst(T item) {
        Node newNode = new Node(item, sentinel.next, sentinel);
        sentinel.next.last = newNode;
        sentinel.next = newNode;
        size++;
    }
    /** add item in the last location.
     * @param item item */
    public void addLast(T item) {
        Node newNode = new Node(item, sentinel, sentinel.last);
        sentinel.last.next = newNode;
        sentinel.last = newNode;
        size++;
    }
    /** return the size of the instance.
     * @return int */
    public int size() {
        return size;
    }
    /** print instance. */
    public void printDeque() {
        Node test = sentinel;
        while (test.next.item != null) {
            test = test.next;
            System.out.print(test.item + " ");
        }
        System.out.println();
    }
    /** remove and get first item.
     * @return item */
    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            T out = sentinel.next.item;
            sentinel.next.next.last = sentinel;
            sentinel.next = sentinel.next.next;
            size--;
            return out;
        }
    }
    /** remove and get last item.
     * @return item */
    public T removeLast() {
        if (size == 0) {
            return null;
        } else {
            T out = sentinel.last.item;
            sentinel.last.last.next = sentinel;
            sentinel.last = sentinel.last.last;
            size--;
            return out;
        }
    }
    /** get specific item.
     * @param index index
     * @return item */
    public T get(int index) {
        Node test = sentinel;
        for (int i = -1; i != index; i++) {
            if (test.next.item == null) {
                return null;
            } else {
                test = test.next;
            }
        }
        return test.item;
    }
    /** define the iterator fo the LinkListDeque. */
    private class LinkedListDequeIterator implements Iterator<T> {
        /** index of teh iterator. */
        private int index;
        /** initialize a iterator. */
        LinkedListDequeIterator() {
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return (index < size);
        }

        @Override
        public T next() {
            T out = get(index);
            index++;
            return out;
        }

    }
    /** return the iterator of the LinkListDeque. */
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
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
