package gitlet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

public class Graph {
    private HashMap<String, ListNode> graph;
    private String vertex;
    private HashMap<String, Integer> map;

    public Graph() {
        graph = new HashMap<>(0);
    }

    public Set<String> keySet() {
        return graph.keySet();
    }
    public class ListNode {
        private String p1;
        private String p2;

        public ListNode(String parent1, String parent2) {
            p1 = parent1;
            p2 = parent2;
        }
    }

    public void addPoint(String point, String parent1, String parent2) {
        if (vertex == null) {
            vertex = point;
        }
        graph.put(point, new ListNode(parent1, parent2));
    }

    public boolean contains(String pointName) {
        return graph.containsKey(pointName);
    }

    public int distanceTo(String destination) {
        if (map == null) {
            map = new HashMap<>(0);
            createMap(vertex, 0);
        }
        return map.get(destination);
    }

    private void createMap(String point, Integer distance) {
        if (map.containsKey(point)) {
            if (map.get(point) > distance) {
                map.put(point, distance);
            }
            return;
        }
        map.put(point, distance);
        ListNode parent = graph.get(point);
        if (parent.p1 != null) {
            createMap(parent.p1, distance + 1);
        }
        if (parent.p2 != null) {
            createMap(parent.p2, distance + 1);
        }
    }
}

