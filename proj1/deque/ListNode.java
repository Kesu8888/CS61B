package deque;

public class ListNode<T> {
    protected T item;
    protected ListNode<T> prev;
    protected ListNode<T> next;

    public ListNode(ListNode<T> p, T i, ListNode<T> n) {
        item = i;
        prev = p;
        next = n;
    }
}
