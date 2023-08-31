package deque;
import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {


    private final Comparator<T> defaultComp;
    public MaxArrayDeque(Comparator<T> c) {
        super();
        defaultComp = c;
    }

    public T max() {
        return getMax(defaultComp);
    }

    public T max(Comparator<T> com) {
        return getMax(com);
    }


    private T getMax(Comparator<T> getMaxComp) {
        if (super.size() == 0) {
            return null;
        }
        T maxItem = super.get(0);
        for (int i = 1; i < size(); i++) {
            if (getMaxComp.compare(maxItem, super.get(i)) < 0) {
                maxItem = super.get(i);
            }
        }
        return maxItem;
    }
}
