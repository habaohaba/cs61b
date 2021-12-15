package bstmap;

import java.util.Iterator;
import java.util.Set;
/** Binary search tree with map.
 * k is the type of key
 * v is the type of value
 * @author lin zhuo
 * */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    /** Node for tree. */
    private class BSTNode {
        K key;
        V value;
        BSTNode left, right;
        /** number fo nodes in subtree */
        int size = 0;
        /** construct a node. */
        BSTNode(K key, V value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
        }
    }
    /** root node for the tree */
    private BSTNode root = null;
    /** initialize an empty map */
    public BSTMap() {
    }
    /** check whether map is empty */
    public boolean isEmpty() {
        return size() == 0;
    }
    /** remove all the mapping */
    @Override
    public void clear() {
        root = null;
    }
    /** Returns true if map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        return getNode(root, key) != null;
    }
    /** get specific node having specific key
     *  @return specific node or null */
    public BSTNode getNode(BSTNode x, K key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return getNode(x.left, key);
        }
        else if (cmp > 0) {
            return getNode(x.right, key);
        }
        else {
            return x;
        }
    }
    /**  */
    @Override
    public V get(K key) {
        return value(getNode(root, key));
    }
    /** return the size of map */
    @Override
    public int size() {
        return size(root);
    }
    /** return size of a node or null */
    public int size(BSTNode x) {
        if (x == null) {
            return 0;
        }
        else {
            return x.size;
        }
    }
    /** return value of a node or null */
    public V value(BSTNode x) {
        if (x == null) {
            return null;
        }
        else {
            return x.value;
        }
    }
    /** Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
    }
    /** helper method for put
     *  add node as leaves
     *  upgrade one size of every node
     *  */
    private BSTNode put(BSTNode x, K key, V value) {
        if (x == null) {
            return new BSTNode(key, value, 1);
        }
        int cmp = key.compareTo(x.key);
        if (cmp > 0) {
            x.right = put(x.right, key, value);
        }
        else if (cmp < 0) {
            x.left = put(x.left, key, value);
        }
        else {
            x.value = value;
        }
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }
    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
    public void printInOrder() {
        throw new UnsupportedOperationException();
    }
}
