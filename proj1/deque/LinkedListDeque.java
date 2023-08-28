package deque;
import java.lang.*;
import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private int size;
    private final ListNode<T> Sentinel;

    public LinkedListDeque() {
        size = 0;
        Sentinel = new ListNode<T>(null, null, null);
        Sentinel.prev = Sentinel;
        Sentinel.next = Sentinel;
    }

    public T getFirst() {
        ListNode<T> first = Sentinel.next;
        return first.item;
    }

    public T getLast() {
        ListNode<T> last = Sentinel.prev;
        return last.item;
    }

    public void addFirst(T item) {
        size++;
        ListNode<T> oldFront = Sentinel.next;
        ListNode<T> newFront = new ListNode<T>(Sentinel, item, oldFront);
        oldFront.prev = newFront;
        Sentinel.next = newFront;
    }

    public void addLast(T item) {
        size++;
        ListNode<T> oldBack = Sentinel.prev;
        ListNode<T> newBack = new ListNode<T>(oldBack, item, Sentinel);
        oldBack.next = newBack;
        Sentinel.prev = newBack;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (Iterator<T> i = iterator(); i.hasNext(); ) {
            System.out.print(i.next() + " ");
        }
        ;
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        ListNode<T> oldFront = Sentinel.next;
        ListNode<T> newFront = oldFront.next;
        T removed = oldFront.item;
        Sentinel.next = newFront;
        newFront.prev = Sentinel;
        size--;
        return removed;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        ListNode<T> oldBack = Sentinel.prev;
        ListNode<T> newBack = oldBack.prev;
        T removed = oldBack.item;
        Sentinel.prev = newBack;
        newBack.next = Sentinel;
        size--;
        return removed;
    }

    public T get(int index) {
        if (index > size) {
            return null;
        }
        ListNode<T> getIndex = Sentinel;
        if (index < (size % 2)) {
            for (int i = 0; i <= index; i++) {
                getIndex = getIndex.next;
            }
            return getIndex.item;
        }
        else {
            for (int i = 0; i <= (size - index); i++) {
                getIndex = getIndex.prev;
            }
            return getIndex.item;
        }
    }

        public Iterator<T> iterator () {
            return new Iterator<T>() {
                ListNode<T> current = Sentinel;

                @Override
                public boolean hasNext() {
                    return current.next != Sentinel;
                }

                @Override
                public T next() {
                    if (hasNext()) {
                        current = current.next;
                        return current.item;
                    }
                    return null;
                }

                @Override
                public void remove() {
                    ListNode<T> prevNode = current.prev;
                    ListNode<T> nextNode = current.next;
                    prevNode.next = nextNode;
                    nextNode.prev = prevNode;
                    size--;
                }
            };
        }

        public T getRecursion ( int index){
            if (index >= size) {
                return null;
            }
            return RecursionHelper(Sentinel.next, index);
        }

        private T RecursionHelper (ListNode < T > Node,int index){
            if (index == 0) {
                return Node.item;
            }
            return RecursionHelper(Node.next, index - 1);
        }

        public boolean equals (Object o){
            if (o instanceof LinkedListDeque) {
                LinkedListDeque<?> comDeque = (LinkedListDeque<?>) o;
                if (comDeque.size == this.size) {
                    return allItems() == comDeque.allItems();
                }
                return false;
            }
            return false;
        }

        public T[] allItems() {
        T[] items = (T[]) new Object[size];
        ListNode<T> Node = Sentinel;
        for (int i = 0; i< size; i++) {
            Node = Node.next;
            items[i] = Node.item;
        }
        return items;
        }
    }



