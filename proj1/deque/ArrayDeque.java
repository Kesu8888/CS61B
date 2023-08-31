package deque;
import java.util.Iterator;


public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private int size;
    private T[] items;
    private int first;
    private int last;
    private final int factor;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        first = 0;
        last = 0;
        factor = 2;
    }

    private void dequeClear() {
        items = (T[]) new Object[8];
        size = 0;
        first = 0;
        last = 0;
    }
    public void addFirst(T item) {
        if (size + 1 >= items.length) {
            resize(items.length * factor);
        }
        if (items[first] == null) {
            items[first] = item;
            size++;
        }else {
            if (first - 1 < 0) {
                first = items.length - 1;
            }else {
                first = first - 1;
            }
            items[first] = item;
            size++;
        }
    }

    public void addLast(T item) {
        if (size + 1 >= items.length) {
            resize(items.length * factor);
        }
        if (items[last] == null) {
            items[last] = item;
            size++;
        }else {
            if (last + 1 == items.length) {
                last = 0;
            }else {
                last = last + 1;
            }
            items[last] = item;
            size++;
        }
    }

    // Helper method for addFirst and addLast.
    private void resize(int newCapacity) {
        T[] itemsCopy = items;
        if (newCapacity <= 8) {
            items = (T[]) new Object[8];
        }else {
            items = (T[]) new Object[newCapacity];
        }
        // Check if the first item is at the back of the array.
        if (first <= last) {
            System.arraycopy(itemsCopy, first, items, 0, size);
        }else {
            System.arraycopy(itemsCopy, first, items, 0, itemsCopy.length - first);
            System.arraycopy(itemsCopy, 0, items, itemsCopy.length - first, last + 1);
        }
        first = 0;
        last = size - 1;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (Iterator<T> iterator = this.iterator(); iterator.hasNext();) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size - 1 <= 0) {
            T removed = items[first];
            dequeClear();
            return removed;
        }
        if (size - 1 < items.length % 3) {
            resize(items.length % factor);
        }
        T firstItem = items[first];
        items[first] = null;
        size --;
        if (first + 1 == items.length) {
            first = 0;
        }else {
            first = first + 1;
        }
        return firstItem;
    }

    public T removeLast() {
        if (size - 1 <= 0) {
            return null;
        }
        if (size - 1 <= items.length % 3)
        {
            resize(items.length % factor);
        }
        T lastItem = items[last];
        items[last] = null;
        size --;
        if (last - 1 < 0) {
            last = items.length - 1;
        }else {
            last = last - 1;
        }
        return lastItem;
    }

    public T get(int index) {
        if (index > size - 1) {
            return null;
        }
        if (first + index >= items.length) {
            return items[index - (items.length - first)];
        }
        return items[first + index];
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int current = -1;

            @Override
            public boolean hasNext() {
                return get(current + 1) != null;
            }

            @Override
            public T next() {
                T nextItem = get(current + 1);
                current++;
                return nextItem;
            }

            @Override
            // Remove the item returned by last .next() call.
            public void remove() {
                throw new IllegalStateException("You cannot remove item that is at the middle of the array.");
            }
        };
    }

    public boolean equals(Object o) {
        if (o instanceof ArrayDeque) {
            ArrayDeque<?> ComDeque = (ArrayDeque<?>) o;
            if (size == ComDeque.size) {
                for (int i = 0; i < size; i++) {
                    if (this.get(i) != ComDeque.get(i)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }
}



