package deque;
import org.junit.Test;
import static org.junit.Assert.*;
public class LinkedListTest {
    @Test
    public void equalsTest() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        Deque<Integer> lld2 = new LinkedListDeque<>();
        lld1.addLast(10);
        lld1.addLast(9);
        lld1.addFirst(8);
        lld2.addLast(10);
        lld2.addLast(9);
        lld2.addFirst(8);
        assertEquals("They are not equals", true, lld1.equals(lld2));
    }
    @Test
    public void ArrayDequeTest() {
        Deque<Integer> ad1 = new ArrayDeque<>();
        ad1.addFirst(0);
        assertEquals("They are not equal", 0, (int)ad1.removeLast());
    }
}