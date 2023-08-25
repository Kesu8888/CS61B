package deque;

import java.util.Comparator;

/** Performs some basic linked list tests. */
public class MaxArrayDequeFunction {

    public static void NormalFunctionTest() {
        Comparator<Integer> Comp = new MyComparator<>();
        var MAD1 = new MaxArrayDeque<Integer>(Comp);
        for (int i = 5; i < 10; i++) {
            MAD1.addLast(i);
        }
        for (int i = 4; i >= 0; i--){
            MAD1.addFirst(i);
        }
        MAD1.printDeque();
        System.out.println("Max Value is " + MAD1.max());
    }
    public static void main(String[] args){
        NormalFunctionTest();
    }
}
