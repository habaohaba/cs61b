package byow.Core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WeightedQuickUnionDS<T> {
    private Map<T , T> parent;

    public WeightedQuickUnionDS(Set<T> rooms) {
        parent = new HashMap<>();
        for (T r : rooms) {
            parent.put(r , null);
        }
    }

    /**
     * find the root.
     * */
    private T find(T p) {
        T r = p;
        while (parent.get(r) != null) {
            r = parent.get(r);
        }
        return r;
    }

    public boolean connected() {
        return true;
    }

}
