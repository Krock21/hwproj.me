package me.hwproj.gaev;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    @Test
    void sizeShouldReturn0() {
        HashTable hashTable = new HashTable();
        hashTable.put("a", "b");
        hashTable.remove("a");
        assertEquals(0, hashTable.size());
    }

    @Test
    void sizeShouldReturn1() {
        HashTable hashTable = new HashTable();
        hashTable.put("a", "b");
        hashTable.put("c", "d");
        hashTable.remove("c");
        assertEquals(1, hashTable.size());
    }

    @Test
    void sizeShouldReturn2() {
        HashTable hashTable = new HashTable();
        hashTable.put("a", "b");
        hashTable.put("c", "d");
        assertEquals(2, hashTable.size());
    }

    @Test
    void sizeShouldReturn5() {
        HashTable hashTable = new HashTable();
        hashTable.put("a", "b");
        hashTable.put("c", "d");
        hashTable.put("e", "f");
        hashTable.put("g", "h");
        hashTable.put("i", "j");
        assertEquals(5, hashTable.size());
    }

    @Test
    void sizeShouldReturn0AfterRemove() {
        HashTable hashTable = new HashTable();
        hashTable.remove("a");
        hashTable.remove("b");
        hashTable.remove("c");
        hashTable.remove("d");
        hashTable.remove("a");
        assertEquals(0, hashTable.size());
    }

    @Test
    void containsShouldReturnTrueIfStores() {
        HashTable hashTable = new HashTable();
        hashTable.put("a", "b");
        assertTrue(hashTable.contains("a"));
    }

    @Test
    void containsShouldReturnFalseIfNotStores() {
        HashTable hashTable = new HashTable();
        hashTable.put("a", "b");
        assertFalse(hashTable.contains("b"));
    }

    @Test
    void getShouldReturnValueIfStores() {
        HashTable hashTable = new HashTable();
        hashTable.put("a", "b");
        assertEquals("b", hashTable.get("a"));
    }

    @Test
    void getShouldReturnNullIfNotStores() {
        HashTable hashTable = new HashTable();
        hashTable.put("a", "b");
        assertNull(hashTable.get("b"));
    }

    @Test
    void putShouldChangeSizeIfNewItem() {
        HashTable hashTable = new HashTable();
        hashTable.put("a", "b");
        assertEquals(1, hashTable.size());
    }

    @Test
    void putShouldNotChangeSizeIfNotNewItem() {
        HashTable hashTable = new HashTable();
        hashTable.put("a", "b");
        hashTable.put("a", "b");
        assertEquals(1, hashTable.size());
    }

    @Test
    void putShouldReturnPreviousValue() {
        HashTable hashTable = new HashTable();
        hashTable.put("a", "b");
        assertEquals("b", hashTable.put("a", "c"));
    }

    @Test
    void removeShouldChangeSizeIfStores() {
        HashTable hashTable = new HashTable();
        hashTable.put("a", "b");
        hashTable.remove("a");
        assertEquals(0, hashTable.size());
    }

    @Test
    void removeShouldNotChangeSizeIfNotStores() {
        HashTable hashTable = new HashTable();
        hashTable.put("a", "b");
        hashTable.remove("b");
        assertEquals(1, hashTable.size());
    }

    @Test
    void removeShouldReturnPreviousValue() {
        HashTable hashTable = new HashTable();
        hashTable.put("a", "b");
        assertEquals("b", hashTable.remove("a"));
    }

    @Test
    void clearShouldChangeSize() {
        HashTable hashTable = new HashTable();
        hashTable.put("a", "b");
        hashTable.put("c", "c");
        hashTable.clear();
        assertEquals(0, hashTable.size());
    }

    @Test
    void clearShouldNotChangeSizeIfEmpty() {
        HashTable hashTable = new HashTable();
        hashTable.clear();
        assertEquals(0, hashTable.size());
    }
}