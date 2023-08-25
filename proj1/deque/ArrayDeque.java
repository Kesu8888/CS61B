package deque;
import java.lang.*;
import java.util.Arrays;
import java.util.Iterator;


public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    protected int size = 0;
    public T[] items;
    protected int first = 0;
    protected int bounds;
    protected int last = 0;

    public ArrayDeque() {
        bounds = 8;
        items = (T[]) new Object[bounds];
    }

    public ArrayDeque(int InitialSize){
        bounds = InitialSize;
        items = (T[]) new Object[bounds];
    }

    public void addFirst(T item) {
        size = size + 1;
        // If the array is full, increase the size of the array.
        if (size == bounds) {
            ResizeExpand();
        }
        items[first] = item;
        // If first is at position zero of the array, put addFirst item at the end of the array.
        if (first - 1 < 0) {
            first = bounds - 1;
        } else {
            first = first - 1;
        }
    }

    public void addLast(T item) {
        // If the array is full, expand the array.
        size = size + 1;
        if (size == bounds) {
            ResizeExpand();
        }
        last = last + 1;
        items[last] = item;
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        } else {
            return false;
        }
    }

    ;

    public int size() {
        return size;
    }

    ;

    public void printDeque() {
        for (Iterator<T> iterator = this.iterator(); iterator.hasNext();){
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
    }

    ;

    public T removeFirst() {
        // Test whether the array is empty
        if (size == 0) {
            return null;
        }
        //Save the item that is going to be removed, and delete it from the array.
        T returnFirst;
        if ((first + 1) == bounds) {
            first = 0;
            returnFirst = items[first];
            items[first] = null;
        } else {
            first = first + 1;
            returnFirst = items[first];
            items[first] = null;
        }
        size = size - 1;
        if (size < (bounds / 4)) {
            resizeShrink();
        }
        return returnFirst;
    }

    ;

    // Shrink the arraydeque if the usage is low
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        //Save the item that is going to be removed, and delete it from the array.
        T returnLast;
        returnLast = items[last];
        items[last] = null;
        // Check if the previous item is at zero position. If yes, put the last at the back of the array.
        if (last == 0) {
            last = bounds - 1;
        } else {
            last = last - 1;
        }
        size = size - 1;
        if (size < (bounds / 4)) {
            resizeShrink();
        }
        return returnLast;
    }

    public T get(int index) {
        if (index > size) {
            return null;
        }
        // Check if the index item is at front or the back of the array.
        if ((bounds - first) > (index + 2)) {
            // The index item is at the back
            System.out.println("Return items[first + index + 1]");
            return items[first + index + 1];

        }
        System.out.println("Last is " + last + ", First is " + first);
        System.out.println("Return items[last - (size - (index + 1))]");
        return items[last - (size - (index + 1))];
    }

    // Expand the arraydeque if overload.
    private void ResizeExpand() {
        T[] itemscopy = items;
        items = (T[]) new Object[bounds * 4];
        // Check if the first item is at the back of the array.
        int IfFirstAtBack = (first + size);
        if (IfFirstAtBack <= bounds) {
            System.arraycopy(itemscopy, first, items, 0, size);
        } else {
            int Backsize = bounds - (first + 1);
            // The position of the first item;
            int RealFirst = first + 1;
            System.arraycopy(itemscopy, (RealFirst), items, 1, (Backsize));
            System.arraycopy(itemscopy, 0, items, (Backsize + 1), (size - Backsize));
        }
        first = 0;
        last = size - 1;
        bounds = bounds * 4;
    }

    public void resizeShrink() {
        T[] itemscopy = items;
        items = (T[]) new Object[bounds / 2];
        //Check if the first item is at the back of the array.
        int FirstNotAtBack = (first + size);
        if (FirstNotAtBack <= bounds) {
            System.arraycopy(itemscopy, first, items, 0, size + 1);
        } else {
            int Backsize = bounds - (first + 1);
            // The position of the first item;
            int RealFirst = first + 1;
            System.arraycopy(itemscopy, (RealFirst), items, 1, (Backsize));
            System.arraycopy(itemscopy, 0, items, (Backsize + 1), (size - Backsize));
        }
        first = 0;
        last = size;
        bounds = bounds / 2;
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int current = first;

            @Override
            public boolean hasNext() {
                // First is now at the last position of the array.
                if ((current + 1) == bounds) {
                    return items[0] != null;
                } else {
                    return items[current + 1] != null;
                }
            }

            @Override
            public T next() {
                if (hasNext()) {
                    if ((current + 1) == bounds) {
                        current = 0;
                        return items[current];
                    } else {
                        current = current + 1;
                        return items[current];
                    }
                }
                return null;
            }

            @Override
            // Remove the item returned by last .next() call.
            public void remove() {
                if (first == (bounds - 1)) {
                    // The remove item is the first item of the arraylist.
                    if (current == 0) {
                        removeFirst();
                    } else {
                        throw new IllegalStateException("You cannot remove item that is at the middle of the array.");
                    }
                } else {
                    // The remove item is the first item of the arraylist.
                    if (current == (first + 1)) {
                        removeFirst();
                    } else {
                        throw new IllegalStateException("You cannot remove item that is at the middle of the array.");
                    }
                }
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

    public String toString(){
        return Arrays.toString(items);
    }

    public int TakeFirst(){
        return first;
    }
}



