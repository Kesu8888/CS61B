package deque;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.Iterable;
public class ALtest {

    @Test
    public void iteratorTest() {
        ArrayDeque<Integer> AL1 = new ArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            AL1.addLast(i);
        }
        for (int i = 10; i < 20; i++) {
            AL1.addFirst(i);
        }
        for (int i : AL1) {
            System.out.println(i);
        }
    }
}
