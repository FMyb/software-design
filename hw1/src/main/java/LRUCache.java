import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LRUCache<K, V> implements Cache<K, V> {
    private final int capacity;
    private final Map<K, LinkedListNode.Node<Pair<K, V>>> map;
    private final LinkedListNode<Pair<K, V>> list;

    public LRUCache(int capacity) {
        assert capacity > 0;
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.list = new LinkedListNode<>();
    }

    @Override
    public void put(K key, V value) {
        assert map.size() <= capacity;
        assert list.size() <= capacity;
        assert list.size() == map.size();
        LinkedListNode.Node<Pair<K, V>> oldNode = map.get(key);
        if (oldNode != null) {
            list.removeNode(oldNode);
        } else if (list.size() == capacity) {
            Pair<K, V> last = list.removeLast();
            map.remove(last.key());
        }
        LinkedListNode.Node<Pair<K, V>> firstNode = list.addFirst(new Pair<>(key, value));
        map.put(key, firstNode);
        assert map.containsKey(key);
        assert Objects.equals(map.get(key).value().value(), value);
        assert map.size() <= capacity;
        assert list.size() <= capacity;
        assert list.size() == map.size();
    }

    @Override
    public V get(K key) {
        assert map.size() <= capacity;
        assert list.size() <= capacity;
        assert list.size() == map.size();
        LinkedListNode.Node<Pair<K, V>> oldValue = map.get(key);
        if (oldValue == null) {
            return null;
        }
        list.removeNode(oldValue);
        list.addFirst(oldValue);
        assert map.size() <= capacity;
        assert list.size() <= capacity;
        assert list.size() == map.size();
        assert map.containsKey(key);
        return oldValue.value().value();
    }

    private record Pair<K, V>(K key, V value) {}
}
