package me.hwproj.gaev;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringListTest {
    private StringList stringList;

    @BeforeEach
    void setUp() {
        stringList = new StringList();
    }

    @Test
    void getShouldReturnNullIfEmpty() {
        assertNull(stringList.get("a"));
    }

    @Test
    void getShouldReturnValueIfStores() {
        stringList.put("a", "b");
        assertEquals("b", stringList.get("a"));
    }

    @Test
    void removeShouldReturnRemovedValue() {
        stringList.put("a", "b");
        assertEquals("b", stringList.remove("a"));
    }

    @Test
    void removeShouldReturnNullIfKeyNotFound() {
        stringList.put("a", "b");
        assertNull(stringList.remove("b"));
    }

    @Test
    void putShouldReturnNullIfWasNotPreviousKey() {
        assertNull(stringList.put("a", "b"));
    }

    @Test
    void putShouldReturnPreviousKey() {
        stringList.put("a", "b");
        assertEquals("b", stringList.put("a", "c"));
    }

    @Test
    void containsShouldReturnFalseIfNotStores() {
        stringList.put("a", "b");
        assertFalse(stringList.contains("b"));
    }

    @Test
    void containsShouldReturnTrueIfStores() {
        stringList.put("a", "b");
        assertTrue(stringList.contains("a"));
    }

    @Test
    void getAnyShouldReturnNullIfEmpty() {
        assertNull(stringList.getAny());
    }

    @Test
    void getAnyShouldNotReturnNullIfNotEmpty() {
        stringList.put("a", "b");
        stringList.put("b", "c");
        assertNotNull(stringList.getAny());
    }
}