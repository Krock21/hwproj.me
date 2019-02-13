package me.hwproj.gaev;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BinaryTreeSetTest {

    private MyTreeSet<Integer> treeSet;

    @BeforeEach
    void setUp() {
        treeSet = new BinaryTreeSet<Integer>();
    }

    @Test
    void iterator() {
        treeSet.add(1);
        treeSet.add(2);
        treeSet.add(3);
        var iterator = treeSet.iterator();
        assertEquals(Integer.valueOf(1), iterator.next());
        assertEquals(Integer.valueOf(2), iterator.next());
        assertEquals(Integer.valueOf(3), iterator.next());
    }

    @Test
    void add() {
        treeSet.add(1);
        assertFalse(treeSet.add(1));
        assertTrue(treeSet.add(2));
    }

    @Test
    void remove() {
        treeSet.add(1);
        treeSet.add(2);
        treeSet.add(3);
        assertTrue(treeSet.remove(1));
        assertFalse(treeSet.remove(1));
        assertTrue(treeSet.remove(3));
        assertFalse(treeSet.remove(5));
    }

    @Test
    void size() {
        assertEquals(0, treeSet.size());
        treeSet.add(1);
        assertEquals(1, treeSet.size());
        treeSet.add(2);
        assertEquals(2, treeSet.size());
        treeSet.remove(1);
        assertEquals(1, treeSet.size());
    }

    @Test
    void descendingIterator() {
        treeSet.add(1);
        treeSet.add(3);
        treeSet.add(2);
        var iterator = treeSet.descendingIterator();
        assertEquals(Integer.valueOf(3), iterator.next());
        assertEquals(Integer.valueOf(2), iterator.next());
        assertEquals(Integer.valueOf(1), iterator.next());
    }

    @Test
    void descendingSet() {
        treeSet.add(3);
        treeSet.add(1);
        treeSet.add(2);
        var descendingTreeSet = treeSet.descendingSet();
        var iterator = descendingTreeSet.iterator();
        assertEquals(Integer.valueOf(3), iterator.next());
        assertEquals(Integer.valueOf(2), iterator.next());
        assertEquals(Integer.valueOf(1), iterator.next());
        descendingTreeSet = descendingTreeSet.descendingSet();
        var descendingIterator = descendingTreeSet.iterator();
        assertEquals(Integer.valueOf(1), descendingIterator.next());
        assertEquals(Integer.valueOf(2), descendingIterator.next());
        assertEquals(Integer.valueOf(3), descendingIterator.next());
    }

    @Test
    void first() {
        treeSet.add(1);
        treeSet.add(3);
        treeSet.add(5);
        treeSet.add(7);
        assertEquals(Integer.valueOf(1), treeSet.first());
    }

    @Test
    void last() {
        treeSet.add(1);
        treeSet.add(3);
        treeSet.add(5);
        treeSet.add(7);
        assertEquals(Integer.valueOf(7), treeSet.last());
    }

    @Test
    void lower() {
        treeSet.add(1);
        treeSet.add(3);
        treeSet.add(5);
        treeSet.add(7);
        assertEquals(Integer.valueOf(3), treeSet.lower(5));
        assertEquals(Integer.valueOf(3), treeSet.lower(4));
        assertEquals(Integer.valueOf(1), treeSet.lower(3));
        assertNull(treeSet.lower(1));
    }

    @Test
    void floor() {
        treeSet.add(1);
        treeSet.add(3);
        treeSet.add(5);
        treeSet.add(7);
        assertEquals(Integer.valueOf(5), treeSet.floor(5));
        assertEquals(Integer.valueOf(3), treeSet.floor(4));
        assertEquals(Integer.valueOf(3), treeSet.floor(3));
        assertNull(treeSet.floor(0));
    }

    @Test
    void ceiling() {
        treeSet.add(1);
        treeSet.add(3);
        treeSet.add(5);
        treeSet.add(7);
        assertEquals(Integer.valueOf(5), treeSet.ceiling(5));
        assertEquals(Integer.valueOf(5), treeSet.ceiling(4));
        assertEquals(Integer.valueOf(3), treeSet.ceiling(3));
        assertNull(treeSet.ceiling(8));
    }

    @Test
    void higher() {
        treeSet.add(1);
        treeSet.add(3);
        treeSet.add(5);
        treeSet.add(7);
        assertEquals(Integer.valueOf(7), treeSet.higher(5));
        assertEquals(Integer.valueOf(5), treeSet.higher(4));
        assertEquals(Integer.valueOf(5), treeSet.higher(3));
        assertNull(treeSet.higher(7));
    }
}