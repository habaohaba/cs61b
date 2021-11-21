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
        int new_nextFirst = (nextFirst-1)% items.length;
        nextFirst = new_nextFirst < 0? new_nextFirst + items.length : new_nextFirst;
        size ++;
    }

    public void addLast(T item){
        if(nextLast == nextFirst){
            re_amount(items.length*2);
        }
        items[nextLast] = item;
        nextLast = (nextLast+1)% items.length;
        size ++;
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
        for(int i = 0, j = nextFirst; i < size; i++ ){
            System.out.print(items[(j+1)% items.length] + " ");
        }
        System.out.println();
    }

    public T removeFirst(){
        if (size == 0){
            return null;
        }else {
            T out = get(0);
            nextFirst = (nextFirst + 1) % items.length;
            size--;
            if (4*size <= items.length && items.length >= 16){
                re_amount(items.length/2);
            }
            return out;
        }
    }

    public T removeLast(){
        if (size == 0){
            return null;
        }else {
            T out = get(size - 1);
            int new_nextLast = (nextLast-1)% items.length;
            nextLast = new_nextLast < 0? new_nextLast + items.length : new_nextLast;
            size --;
            if (4*size <= items.length && items.length >= 16){
                re_amount(items.length/2);
            }
            return out;
        }
    }
    public T get(int index){
        if (index > size-1){
            return null;
        }else {
            return items[(nextFirst + index + 1) % items.length];
        }
    }
}