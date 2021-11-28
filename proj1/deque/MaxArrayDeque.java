package deque;


import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T>{
    private Comparator<T> comp;

    public MaxArrayDeque(Comparator<T> c){
        comp = c;
    }

    public T max(){
        return max(comp);
    }

    public T max(Comparator<T> c){
        if(size()==0){
            return null;
        }
        T max = get(0);
        for(int i =1; i < size(); i++){
            T current = get(i);
            if(comp.compare(max, current) < 0){
                max = current;
            }
        }
        return max;
    }
}
