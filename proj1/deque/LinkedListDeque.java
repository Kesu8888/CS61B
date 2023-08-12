package deque;
import java.lang.Iterable;
import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T>{
    private int size;
    private final ListNode<T> FrontSentinel;
    private final ListNode<T> BackSentinel;
    private ListNode<T> recursion;
    public LinkedListDeque() {
        FrontSentinel = new ListNode<>(null, null, null);
        BackSentinel = new ListNode<>(null, null, null);
    }

    public T getFirst() {
        ListNode<T> getFirst= FrontSentinel.next;
        return getFirst.item;
    }

    public T getLast() {
        ListNode<T> getLast= BackSentinel.prev;
        return getLast.item;
    }

    public void addFirst(T item) {
        size += 1;
        ListNode<T> New;
        if (BackSentinel.prev != null) {
            New = new ListNode<T>(FrontSentinel, item, FrontSentinel.next);
        } else {
            New = new ListNode<T>(FrontSentinel, item, BackSentinel);
            BackSentinel.prev = New;
        }
        ListNode<T> p;
        if (FrontSentinel.next != null) {
            p = FrontSentinel.next;
            p.prev = New;
        }
        FrontSentinel.next = New;
    }

    public void addLast(T item) {
        size += 1;
        ListNode<T> New;
        if (FrontSentinel.next != null) {
            New = new ListNode<T>(BackSentinel.prev, item, BackSentinel);
        } else {
            New = new ListNode<T>(FrontSentinel, item, BackSentinel);
            FrontSentinel.next = New;
        }
        ListNode<T> p;
        if (BackSentinel.prev != null) {
            p = BackSentinel.prev;
            p.next = New;
        }
        BackSentinel.prev = New;
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (Iterator<T> i=iterator(); i.hasNext();){
            System.out.print(i.next() + " ");
        } ;
        System.out.println();
    }

    public T removeFirst() {
        if (FrontSentinel.next == null) {
            return null;
        }
        size = size - 1;
        ListNode<T> Current= FrontSentinel.next;
        T i= Current.item;
        if (Current.next== BackSentinel) {
            FrontSentinel.next= null;
            BackSentinel.prev= null;
        }
        else {
            ListNode<T> Next=Current.next;
            FrontSentinel.next= Next;
            Next.prev= FrontSentinel;
        }
        return i;
    }

    public T removeLast() {
        // if the last item is null, returns null
        if (BackSentinel.prev == null) {
            return null;
        }
        size = size - 1;
        ListNode<T> Current= BackSentinel.prev;
        T i= Current.item;
        //If there is no item after last item, turn both Front and back Sentinel to null
        if (Current.prev== FrontSentinel) {
            FrontSentinel.next= null;
            BackSentinel.prev= null;
        }
        //There is a ListNode after last ListNode
        else {
            ListNode<T> Prev=Current.prev;
            BackSentinel.prev= Prev;
            Prev.next= BackSentinel;
        }
        return i;
    }

    public T get(int index) {
        if ((index < 0) | (index > size)) {
            throw new UnsupportedOperationException("The index you have input is not existed");
        }
        int Serial;
        ListNode<T> current = null;
        if (index < ((double) size/(double) 2)) {
            Serial= index;
            for (int i= 0; i<= Serial; i++) {
                if (i == 0) {
                    current = FrontSentinel.next;
                }
                else {
                    current = current.next;
                }
            }
        }
        else {
            Serial = size - index;
                for (int i=0; i< Serial; i++) {
                    if (i == 0) {
                        current = BackSentinel.prev;
                    }
                    else {
                    current = current.prev;
                    }
            }
        }
       return current.item;
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            ListNode<T> current= FrontSentinel;

            @Override
            public boolean hasNext() {
                return current.next != BackSentinel;
            }

            @Override
            public T next() {
                if (hasNext()) {
                    current=current.next;
                    return current.item;
                }
            return null;
            }

            @Override
            public void remove() {
                if (current == FrontSentinel.next){
                    removeFirst();
                    current = FrontSentinel;
                }
                throw new UnsupportedOperationException();
            }
        };
    }

    public T getRecursion(int index){
        if (recursion == null) {
            recursion = FrontSentinel.next;
        }
        if (index == 1) {
            return recursion.item;
        }
        else {
            recursion = recursion.next;
            return getRecursion(index - 1);
        }
    }

    public boolean equals(Object o) {
        if (o instanceof LinkedListDeque){
            LinkedListDeque<?> ComDeque= (LinkedListDeque<?>) o;
            if (size == ComDeque.size()){
                for (int i =0; i<size; i++){
                    if (get(i) != ComDeque.get(i)){
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



