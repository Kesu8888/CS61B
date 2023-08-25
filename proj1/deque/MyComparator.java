package deque;
import java.util.*;


public class MyComparator<T> implements Comparator<T>{
@Override
    public int compare(T i1, T i2) {
    String L1 = i1.toString();
    String L2 = i2.toString();
    return L1.compareTo(L2);
    }
}


