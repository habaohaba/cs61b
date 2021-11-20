package deque;

public class LinkedListDeque<T> implements Deque<T> {

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
        sentinel = new Node(null, sentinel, sentinel);
        size = 0;
    }

    public T getRecursive(int index){

    }

    public void addFirst(T item){
        Node new_node = new Node(item, sentinel.next, sentinel);
        size ++;
    }

    public void addLast(T item){
        Node new_node = new Node(item, sentinel, sentinel.last);
    }

    public boolean isEmpty(){

    }

    public int size(){

    }

    public void printDeque(){

    }

    public T removeFirst(){

    }

    public T removeLast(){

    }

    public T get(int index){

    }

}
