package tqs.midterm;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNot.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tqs.midterm.cache.CacheManager;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class CacheManagerTest {

    private CacheManager cache;

    @BeforeEach
    void setup(){cache = new CacheManager();}

    // Assert that the cache is empty upon creation
    @Test
    void isEmptyOnConstruction(){
        Assertions.assertTrue(cache.isEmpty());
    }

    // Assert that the cache has zero hits and zero misses upon creation
    @Test
    void isEmptyHitsMisses(){
        assertThat(cache.getHits(), is(0));
        assertThat(cache.getMisses(),is(0));
    }

    // Assert that a new value can be added and retrieved
    // and that the size of the cache changes to 1
    @Test
    void isAdded(){
        String key = "test";
        cache.add(key, LocalDateTime.now().plusMinutes(10),"value");
        Assertions.assertNotNull(cache.get(key));
        assertThat(cache.size(),is(1));
    }

    // Assert that when an expired value is accessed, it is removed and null is returned
    @Test
    void removesExpired(){
        String key = "test";
        // add expired value
        cache.add(key,LocalDateTime.now().minusMinutes(1),"value");
        // retrieve expired value
        cache.get(key);
        // assert that the value is no longer cached and null is returned
        Assertions.assertNull(cache.get(key));
    }

    // Assert that when a value is accessed
    // and is not cached, it increments the miss count
    @Test
    void miss(){
        // get non cached value
        cache.get("test");

        assertThat(cache.getMisses(),is(1));
    }

    // Assert that when a value is accessed
    // and is cached, it increments the hit count
    @Test
    void hit(){
        String key = "test";
        cache.add(key,LocalDateTime.now().plusMinutes(1),"value");
        // get cached value
        cache.get(key);

        assertThat(cache.getHits(),is(1));
    }
}
