package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T>{

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

    private Node sentinel;
    private int size;

    public LinkedListDeque(){
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.last = sentinel;
        size = 0;
    }

    public LinkedListDeque(int input_size, Node n, Node l){
        sentinel = new Node(null, n, l);
        sentinel.next = sentinel;
        sentinel.last = sentinel;
        size = input_size;
    }

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

    public void addFirst(T item){
        Node new_node = new Node(item, sentinel.next, sentinel);
        sentinel.next.last = new_node;
        sentinel.next = new_node;
        size ++;
    }

    public void addLast(T item){
        Node new_node = new Node(item, sentinel, sentinel.last);
        sentinel.last.next = new_node;
        sentinel.last = new_node;
        size ++;
    }

    public boolean isEmpty(){
        if (size == 0){
            return true;
        }else{
            return false;
        }
    }

    public int size(){
        return size;
    }

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

    private class LinkedListDeque_iterator implements Iterator<T>{
        int index = 0;
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
    public Iterator<T> iterator(){
        return new LinkedListDeque_iterator();
    }
}
