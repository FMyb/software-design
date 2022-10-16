import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LRUCacheTest {
    @Test
    public void addOneAndGetTest() {
        var lruCache = new LRUCache<Integer, String>(1);
        lruCache.put(1, "1");
        assertEquals("1", lruCache.get(1));
    }

    @Test
    public void addManyTest() {
        var lruCache = new LRUCache<Integer, String>(100);
        for (int i = 0; i < 100; i++) {
            lruCache.put(i, String.valueOf(i));
        }
        for (int i = 99; i >= 0; i--) {
            assertEquals(String.valueOf(i), lruCache.get(i));
        }
    }

    @Test
    public void getEmptyTest() {
        var lruCache = new LRUCache<Integer, String>(1);
        assertNull(lruCache.get(0));
    }

    @Test
    public void simpleScenarioTest() {
        var lruCache = new LRUCache<Integer, Integer>(2);
        lruCache.put(1, 1);
        lruCache.put(2, 2);
        assertEquals(Integer.valueOf(1), lruCache.get(1));
        lruCache.put(3, 3);
        assertNull(lruCache.get(2));
        lruCache.put(4, 4);
        assertNull(lruCache.get(1));
        assertEquals(Integer.valueOf(3), lruCache.get(3));
        assertEquals(Integer.valueOf(4), lruCache.get(4));
    }
}
