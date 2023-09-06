package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int numOfBuckets;
    private int size;
    private double loadFactor;
    private final int resizeFactor = 2;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        buckets = createTable(16);
        numOfBuckets = 16;
        loadFactor = 0.75;
    }

    public MyHashMap(int initialSize) {
        buckets = createTable(initialSize);
        numOfBuckets = initialSize;
        loadFactor = 0.75;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        createTable(initialSize);
        numOfBuckets = initialSize;
        loadFactor = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {

        return null;
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<Node>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = new Collection[tableSize];
        for (int i = 0; i < table.length; i++) {
            table[i] = createBucket();
        }
        return table;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    public void clear() {
        buckets = createTable(16);
        numOfBuckets = 16;
        size = 0;
    }

    public boolean containsKey(K key) {
        if (getKeyNode(key) != null) {
            return true;
        }
        return false;
    }

    protected Node getKeyNode(K key) {
        int keyIndex = Math.floorMod(key.hashCode(), numOfBuckets);
        for (Node keyExist : buckets[keyIndex]) {
            if (keyExist.key.equals(key)){
                return keyExist;
            }
        }
        return null;
    }

    public V get(K key) {
        Node getValue = getKeyNode(key);
        if (getValue != null) {
            return getValue.value;
        }
        return null;
    }

    public int size() {
        return size;
    }

    public void put(K key, V value) {
        Node putKeyNode = getKeyNode(key);
        if (putKeyNode != null) {
            putKeyNode.value = value;
            return;
        }
        if ((double) (size + 1) /numOfBuckets > loadFactor) {
            resize(numOfBuckets * resizeFactor);
        }
        int keyIndex = Math.floorMod(key.hashCode(), numOfBuckets);
        buckets[keyIndex].add(new Node(key, value));
        size++;
    }

    private void resize(int newBucketNum) {
        Collection<Node>[] bucketCopy = buckets;
        buckets = createTable(newBucketNum);
        numOfBuckets = newBucketNum;
        for(int i = 0; i < bucketCopy.length; i++) {
            Iterator<Node> bucketItr = bucketCopy[i].iterator();
            while (bucketItr.hasNext()) {
                Node node = bucketItr.next();
                fastPut(node.key, node.value);
            }
        }
    }

    private void fastPut(K key, V value) {
        int put = Math.floorMod(key.hashCode(), numOfBuckets);
        buckets[put].add(new Node(key, value));
    }

    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (Iterator<K> MapIterator = this.iterator(); MapIterator.hasNext();) {
            keySet.add(MapIterator.next());
        }
        System.out.println(keySet.size());
        return keySet;
    }

    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public Iterator<K> iterator() {
        return new Iterator<K>() {

            int currentBucket = 0;
            Iterator<Node> bucketItr = buckets[currentBucket].iterator();
            @Override
            public boolean hasNext() {
                if (bucketItr.hasNext()) {
                    return true;
                }
                if (currentBucket + 1 < numOfBuckets) {
                        return moveBucket();
                }
                return true;
            }
            public boolean moveBucket() {
                currentBucket++;
                bucketItr = buckets[currentBucket].iterator();
                if (bucketItr.hasNext()) {
                    return true;
                }
                if (currentBucket + 1 < numOfBuckets) {
                    return moveBucket();
                }
                return false;
            }

            @Override
            public K next() {
                return bucketItr.next().key;
            }
        };
    }
}
