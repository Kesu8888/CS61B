package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{
    private BSTNode<K, V> Root;
    private int Size;

    public BSTMap() {
        Size = 0;
    }

    public BSTMap(K key, V value) {
        Root = new BSTNode<>(key, value);
        Size = 1;
    }

    //
    public void clear() {
        Root = null;
        Size = 0;
    }

    public boolean containsKey(K key) {
        if (Size == 0) {
            return false;
        }
        BSTNode<K, V> keyExist = searchNode(key, Root);
        if (keyExist == null) {
            return false;
        }
        return true;
    }

    public V get(K key) {
        if (Size == 0) {
            System.out.println("The key is not exist in the map" + this);
            return null;
        }
        BSTNode<K, V> getValue= searchNode(key, Root);
        if (getValue == null) {
            throw new NullPointerException("The key is not exist in the Map: " + this);
        }
        return getValue.value;
    }

    public int size() {
        return Size;
    }

    public void put(K key, V value) {
        if (Root == null){
            Root = new BSTNode<>(key, value);
            Size ++;
            return;
        }
        keyInsert(key, value, Root);
        Size ++;
    }

    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    public V remove(K key) {
        if (Size == 0) {
            throw new NullPointerException("The key is not exist in the Map: " + this);
        }
        BSTNode<K, V> keyRemove = searchNode(key, Root);
        if (keyRemove == null) {
            throw new NullPointerException("The key is not exist in the Map: " + this);
        }
        V removeValue = keyRemove.value;
        BSTNode<K, V> prevNode = keyRemove.prev;
        if (prevNode.left == keyRemove) {
            prevNode.left = null;
        }
        else {
            prevNode.right = null;
        }
        Size --;
        return removeValue;
    }

    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public Iterator<K> iterator() {
      throw new UnsupportedOperationException();
    }





    /* Helper methods for "put", "containsKey", "remove" and "get"  */
    private void keyInsert(K key, V value, BSTNode<K, V> BSTTree) {
        if (key.compareTo(BSTTree.key) == 0) {
            System.out.println("The key "+ key + " is already exist in the map.");
            return;
        }

        if (key.compareTo(BSTTree.key) < 0) {
            if (BSTTree.left != null) {
                keyInsert(key, value, BSTTree.left);
            }
            else {
                BSTTree.left = new BSTNode<>(key, value,BSTTree);
            }
        }

        if (key.compareTo(BSTTree.key) > 0) {
            if (BSTTree.right != null) {
                keyInsert(key, value, BSTTree.right);
            }
            else {
                BSTTree.right = new BSTNode<>(key, value, BSTTree);
            }
        }
    }

    //The parameter BSTNode must not be null for this helper function.
    private BSTNode<K, V> searchNode(K key, BSTNode<K, V> BSTSearch) {
        if (key.compareTo(BSTSearch.key) == 0) {
            return BSTSearch;
        }

        if (key.compareTo(BSTSearch.key) < 0) {
            if (BSTSearch.left != null) {
                return searchNode(key, BSTSearch.left);
            }
            return null;
        }

        else {
            if (BSTSearch.right != null) {
                return searchNode(key, BSTSearch.right);
            }
        }
        return null;
    }
}
