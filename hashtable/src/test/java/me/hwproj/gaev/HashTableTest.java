package me.hwproj.gaev;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {
    private HashTable hashTable;

    @BeforeEach
    void setUp() {
        hashTable = new HashTable();
    }

    @Test
    void sizeShouldReturn0() {
        hashTable.put("a", "b");
        hashTable.remove("a");
        assertEquals(0, hashTable.size());
    }

    @Test
    void sizeShouldReturn1() {
        hashTable.put("a", "b");
        hashTable.put("c", "d");
        hashTable.remove("c");
        assertEquals(1, hashTable.size());
    }

    @Test
    void sizeShouldReturn2() {
        hashTable.put("a", "b");
        hashTable.put("c", "d");
        assertEquals(2, hashTable.size());
    }

    @Test
    void sizeShouldReturn5() {
        hashTable.put("a", "b");
        hashTable.put("c", "d");
        hashTable.put("e", "f");
        hashTable.put("g", "h");
        hashTable.put("i", "j");
        assertEquals(5, hashTable.size());
    }

    @Test
    void sizeShouldReturn0AfterRemove() {
        hashTable.remove("a");
        hashTable.remove("b");
        hashTable.remove("c");
        hashTable.remove("d");
        hashTable.remove("a");
        assertEquals(0, hashTable.size());
    }

    @Test
    void containsShouldReturnTrueIfStores() {
        hashTable.put("a", "b");
        assertTrue(hashTable.contains("a"));
    }

    @Test
    void containsShouldReturnFalseIfNotStores() {
        hashTable.put("a", "b");
        assertFalse(hashTable.contains("b"));
    }

    @Test
    void getShouldReturnValueIfStores() {
        hashTable.put("a", "b");
        assertEquals("b", hashTable.get("a"));
    }

    @Test
    void getShouldReturnNullIfNotStores() {
        hashTable.put("a", "b");
        assertNull(hashTable.get("b"));
    }

    @Test
    void putShouldChangeSizeIfNewItem() {
        hashTable.put("a", "b");
        assertEquals(1, hashTable.size());
    }

    @Test
    void putShouldNotChangeSizeIfNotNewItem() {
        hashTable.put("a", "b");
        hashTable.put("a", "b");
        assertEquals(1, hashTable.size());
    }

    @Test
    void putShouldReturnPreviousValue() {
        hashTable.put("a", "b");
        assertEquals("b", hashTable.put("a", "c"));
    }

    @Test
    void removeShouldChangeSizeIfStores() {
        hashTable.put("a", "b");
        hashTable.remove("a");
        assertEquals(0, hashTable.size());
    }

    @Test
    void removeShouldNotChangeSizeIfNotStores() {
        hashTable.put("a", "b");
        hashTable.remove("b");
        assertEquals(1, hashTable.size());
    }

    @Test
    void removeShouldReturnPreviousValue() {
        hashTable.put("a", "b");
        assertEquals("b", hashTable.remove("a"));
    }

    @Test
    void clearShouldChangeSize() {
        hashTable.put("a", "b");
        hashTable.put("c", "c");
        hashTable.clear();
        assertEquals(0, hashTable.size());
    }

    @Test
    void clearShouldNotChangeSizeIfEmpty() {
        hashTable.clear();
        assertEquals(0, hashTable.size());
    }
}