import org.assertj.core.api.Assertions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LRUCache<K, V> implements Cache<K, V> {
    private final int capacity;
    private final Map<K, V> map;
    private final LinkedList<K> list;

    public LRUCache(int capacity) {
        Assertions.assertThat(capacity).isGreaterThan(0);
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.list = new LinkedList<>();
    }

    @Override
    public void put(K key, V value) {
        Assertions.assertThat(map.size()).isLessThanOrEqualTo(capacity);
        Assertions.assertThat(list.size()).isLessThanOrEqualTo(capacity);
        Assertions.assertThat(list.size()).isEqualTo(map.size());
        V oldValue = map.get(key);
        if (oldValue != null) {
            list.remove(key);
        } else if (list.size() == capacity) {
            K last = list.removeLast();
            map.remove(last);
        }
        list.addFirst(key);
        map.put(key, value);
        Assertions.assertThat(map).containsKey(key);
        Assertions.assertThat(map.get(key)).isEqualTo(value);
        Assertions.assertThat(map.size()).isLessThanOrEqualTo(capacity);
        Assertions.assertThat(list.size()).isLessThanOrEqualTo(capacity);
        Assertions.assertThat(list.size()).isEqualTo(map.size());
    }

    @Override
    public V get(K key) {
        Assertions.assertThat(map.size()).isLessThanOrEqualTo(capacity);
        Assertions.assertThat(list.size()).isLessThanOrEqualTo(capacity);
        Assertions.assertThat(list.size()).isEqualTo(map.size());
        V oldValue = map.get(key);
        if (oldValue == null) {
            return null;
        }
        list.remove(key);
        list.addFirst(key);
        Assertions.assertThat(map.size()).isLessThanOrEqualTo(capacity);
        Assertions.assertThat(list.size()).isLessThanOrEqualTo(capacity);
        Assertions.assertThat(list.size()).isEqualTo(map.size());
        Assertions.assertThat(map).containsKey(key);
        return oldValue;
    }
}
