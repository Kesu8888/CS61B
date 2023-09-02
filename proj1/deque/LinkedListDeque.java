package deque;
import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private int size;
    private final ListNode<T> sentinel;

    public LinkedListDeque() {
        size = 0;
        sentinel = new ListNode<T>(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    public void addFirst(T item) {
        size++;
        ListNode<T> oldFront = sentinel.next;
        ListNode<T> newFront = new ListNode<T>(sentinel, item, oldFront);
        oldFront.prev = newFront;
        sentinel.next = newFront;
    }

    public void addLast(T item) {
        size++;
        ListNode<T> oldBack = sentinel.prev;
        ListNode<T> newBack = new ListNode<T>(oldBack, item, sentinel);
        oldBack.next = newBack;
        sentinel.prev = newBack;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (Iterator<T> i = iterator(); i.hasNext(); ) {
            System.out.print(i.next() + " ");
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        ListNode<T> oldFront = sentinel.next;
        ListNode<T> newFront = oldFront.next;
        T removed = oldFront.item;
        sentinel.next = newFront;
        newFront.prev = sentinel;
        size--;
        return removed;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        ListNode<T> oldBack = sentinel.prev;
        ListNode<T> newBack = oldBack.prev;
        T removed = oldBack.item;
        sentinel.prev = newBack;
        newBack.next = sentinel;
        size--;
        return removed;
    }

    public T get(int index) {
        if (index > size) {
            return null;
        }
        ListNode<T> getIndex = sentinel;
        if (index < (size % 2)) {
            getIndex = sentinel.next;
            for (int i = 1; i < index; i++) {
                getIndex = getIndex.next;
            }
            return getIndex.item;
        } else {
            getIndex = sentinel.prev;
            for (int i = 1; i < (size - index); i++) {
                getIndex = getIndex.prev;
            }
            return getIndex.item;
        }
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
                ListNode<T> current = sentinel;

                @Override
                public boolean hasNext() {
                    return current.next != sentinel;
                }

                @Override
                public T next() {
                    if (hasNext()) {
                        current = current.next;
                        System.out.println(current.item);
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

    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return recursionHelper(sentinel, index);
    }

    private T recursionHelper(ListNode<T> node, int index) {
        if (index < 0) {
            return node.item;
        }
        return recursionHelper(node.next, index - 1);
    }

    public boolean equals(Object o) {
        if (o instanceof Deque) {
            Deque<?> comDeque = (Deque<?>) o;
            if (comDeque.size() == size) {
                MyComparator comparator = new MyComparator();
                for (int i = 0; i < size; i++) {
                    if (!comparator.compare(this.get(i), comDeque.get(i))) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }
    /*public boolean equals(Object o) {
        if (o instanceof LinkedListDeque) {
            LinkedListDeque<?> comDeque = (LinkedListDeque<?>) o;
            if (comDeque.size == this.size) {
              Iterator<?> itr2 = comDeque.iterator();
              for (Iterator<T> itr1 = this.iterator(); itr1.hasNext();) {
                  if (itr1.next() != itr2.next()) {
                      return false;
                  }
              }
              return true;
            }
            return false;
        }
        return false;
    }
    */
}



