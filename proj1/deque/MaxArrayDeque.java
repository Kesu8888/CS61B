package deque;

import java.util.Comparator;
import java.util.Iterator;

public class MaxArrayDeque<T> extends ArrayDeque<T> implements Deque<T>{


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


    public T getMax(Comparator<T> getMaxComp){
            if (size <= 0){
                return null;
            }
            T MaxItem = items[last];
            for (Iterator<T> iterator = this.iterator(); iterator.hasNext(); ) {
                T IteratorItem = iterator.next();
                if (getMaxComp.compare(MaxItem, IteratorItem) < 0){
                    MaxItem = IteratorItem;
                }
            }
            return MaxItem;
        }
}
