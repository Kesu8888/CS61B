package deque;

public class ListNode<T> {
    public T item;
    public ListNode<T> prev;
    public ListNode<T> next;

    public ListNode(ListNode<T> p, T i, ListNode<T> n) {
        item = i;
        prev = p;
        next = n;
    }
}
