package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T>{
    /** define node class
     *  item is the value
     *  next is the next node
     *  last is the lase one node */
    private class Node{
        public T item;
        public Node next;
        public Node last;
        public Node(T i, Node n, Node l){
            item = i;
            next = n;
            last = l;
        }
    }
    /** sentinel is the helper node
     *  size is the amount of the nodes */
    private Node sentinel;
    private int size;
    /** initialize LinkListDeque
     *  only have sentinel node point to itself */
    public LinkedListDeque(){
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.last = sentinel;
        size = 0;
    }
    /** helper method for the getRecursive
     *  make a new LinkListDeque which remove first one */
    public LinkedListDeque(int input_size, Node n, Node l){
        sentinel = new Node(null, n, l);
        sentinel.next.last = sentinel;
        sentinel.last.next = sentinel;
        size = input_size;
    }
    /** get the index value by recursive method */
    public T getRecursive(int index){
        if (size == 0){
            return null;
        }else if(index == 0){
            return sentinel.next.item;
        }else{
            LinkedListDeque<T> new_object = new LinkedListDeque<>(size-1, sentinel.next.next, sentinel.last);
            return new_object.getRecursive(index -1);
        }
    }
    /** add item in the first location */
    public void addFirst(T item){
        Node new_node = new Node(item, sentinel.next, sentinel);
        sentinel.next.last = new_node;
        sentinel.next = new_node;
        size ++;
    }
    /** add item in the last location */
    public void addLast(T item){
        Node new_node = new Node(item, sentinel, sentinel.last);
        sentinel.last.next = new_node;
        sentinel.last = new_node;
        size ++;
    }
    /** return the size of the instance */
    public int size(){
        return size;
    }
    /** print instance */
    public void printDeque(){
        Node test = sentinel;
        while(test.next.item != null){
            test = test.next;
            System.out.print(test.item + " ");
        }
        System.out.println();
    }

    public T removeFirst(){
        if (size == 0){
            return null;
        }else{
            T out = sentinel.next.item;
            sentinel.next.next.last = sentinel;
            sentinel.next = sentinel.next.next;
            size --;
            return out;
        }
    }

    public T removeLast(){
        if (size == 0){
            return null;
        }else{
            T out = sentinel.last.item;
            sentinel.last.last.next = sentinel;
            sentinel.last = sentinel.last.last;
            size --;
            return out;
        }
    }

    public T get(int index){
        Node test = sentinel;
        for(int i = -1; i != index; i++){
            if (test.next.item == null) {
                return null;
            }else{
                test = test.next;
            }
        }
        return test.item;
    }
    /** define the iterator fo the LinkListDeque */
    private class LinkedListDeque_iterator implements Iterator<T>{
        int index ;
        LinkedListDeque_iterator(){
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return(index < size-1);
        }

        @Override
        public T next() {
            T out = get(index);
            index ++;
            return out;
        }

    }
    /** return the iterator of the LinkListDeque */
    public Iterator<T> iterator(){
        return new LinkedListDeque_iterator();
    }
    /** check if o equal this instance */
    @Override
    public boolean equals(Object o){
        if (o == null){
            return false;
        }
        if (this == o){
            return true;
        }
        if (o instanceof LinkedListDeque && ((LinkedListDeque<?>) o).size == size){
            for(int i = 0; i < size; i ++){
                if (!((LinkedListDeque<?>) o).get(i).equals(this.get(i))){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
