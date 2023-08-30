package deque;

import java.util.Comparator;
import java.util.Iterator;

public class MaxArrayDeque<T> extends ArrayDeque<T>{


    private Comparator<T> DefaultComp;
    public MaxArrayDeque(Comparator<T> c){
        super();
        DefaultComp= c;
    }

    public T max() {
        return getMax(DefaultComp);
    }

    public T max(Comparator<T> Com) {
        return getMax(Com);
    }


    private T getMax(Comparator<T> getMaxComp){
            if (size() <= 0){
                return null;
            }
            T maxItem = get(0);
            for (int i = 1; i < size(); i++) {
                if (getMaxComp.compare(maxItem, get(i)) < 0) {
                    maxItem = get(i);
                }
            }
            return maxItem;
        }
}
