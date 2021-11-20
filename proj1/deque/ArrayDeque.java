package deque;

import edu.princeton.cs.algs4.StdRandom;

public class ArrayDeque<T> implements Deque<T> {
    /** contents of ArrayDeque */
    T[] items;
    int size;
    int nextFirst;
    int nextLast;
    /** make an empty ArrayDeque */
    public ArrayDeque(){
        items =(T []) new Object[8];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }
    /** change the amount of the array */
    public void re_amount(int des_amount){
        T[] new_object =(T[]) new Object[des_amount];
        for(int i = 0, j = nextFirst; i < size; i++){
            if (j == items.length-1){
                j = 0;
                new_object[i] = items[j];
            }else{
                new_object[i] = items[j+1];
                j ++;
            }
        }
        items = new_object;
        nextFirst = items.length - 1;
        nextLast = size;
    }

    public void addFirst(T item){
        if(nextLast == nextFirst){
            re_amount(items.length*2);
        }
        items[nextFirst] = item;
        nextFirst = (nextFirst-1)%items.length;
    }

    public void addLast(T item){
        if (4*size <= items.length && items.length >= 16){
            re_amount(items.length/2);
        }
        items[nextLast] = item;
        nextLast = (nextLast+1)% items.length;
    }

    public boolean isEmpty(){
        if(size == 0){
            return true;
        }else{
            return false;
        }
    }

    public int size(){
        return size;
    }

    public void printDeque(){

    }

    public T removeFirst(){
        T out = get(0);
        nextFirst = (nextFirst+1)% items.length;
        return out;
    }

    public T removeLast(){
        T out = get(size-1);
        nextLast = (nextLast-1)% items.length;
        return out;
    }

    public T get(int index){
        return items[(nextFirst+index+1)%items.length];
    }
}