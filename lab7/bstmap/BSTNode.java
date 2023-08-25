package bstmap;

import edu.princeton.cs.algs4.BST;

public class BSTNode<K extends Comparable<K>, V> {
    public final K key;
    public final V value;
    public BSTNode<K, V> left;
    public BSTNode<K, V> right;
    public BSTNode<K, V> prev;

    BSTNode(K keyIn, V valueIn) {
        this.key = keyIn;
        this.value = valueIn;
    }

    BSTNode(K keyIn, V valueIn, BSTNode<K, V> previous) {
        this.key = keyIn;
        this.value = valueIn;
        this.prev = previous;
    }
}
