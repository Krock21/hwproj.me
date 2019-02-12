package me.hwproj.gaev;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {
    private Trie trie;

    // i can't test for Throw(null too), because don't know about Throws from @NotNull

    @BeforeEach
    void setUp() {
        trie = new Trie();
    }

    @Test
    void addShouldChangeSize() {
        trie.add("trie");
        assertEquals(1, trie.size(), "bad add");
    }

    @Test
    void addShouldReturnFalseIfContains() {
        trie.add("trie");
        assertFalse(trie.add("trie"), "bad add");
    }

    @Test
    void addShouldReturnTrueIfNotContains() {
        trie.add("trie");
        assertTrue(trie.add("trie2"), "bad add");
    }

    @Test
    void containsShouldReturnTrueIfContains() {
        trie.add("trie");
        assertTrue(trie.contains("trie"), "bad contains");
    }

    @Test
    void containsShouldReturnFalseIfNotContains() {
        trie.add("trie");
        assertFalse(trie.contains("trie2"), "bad contains");
    }

    @Test
    void containsShouldReturnFalseIfNotContains2() {
        trie.add("trie");
        assertFalse(trie.contains("tri"), "bad contains");
    }

    @Test
    void removeShouldChangeSizeIfSuccess() {
        trie.add("trie");
        trie.remove("trie");
        assertEquals(0, trie.size(), "bad remove");
    }

    @Test
    void removeShouldNotChangeSizeIfFail() {
        trie.add("trie");
        trie.remove("trie2");
        assertEquals(1, trie.size(), "bad remove");
    }

    @Test
    void removeShouldNotChangeSizeIfFail2() {
        trie.add("trie");
        trie.remove("tri");
        assertEquals(1, trie.size(), "bad remove");
    }

    @Test
    void removeShouldReturnTrueIfExists() {
        trie.add("trie");
        assertTrue(trie.remove("trie"), "bad remove");
    }

    @Test
    void removeShouldReturnFalseIfNotExists() {
        trie.add("trie");
        assertFalse(trie.remove("trie2"), "bad remove");
    }

    @Test
    void removeShouldReturnFalseIfNotExists2() {
        trie.add("trie");
        assertFalse(trie.remove("tri"), "bad remove");
    }

    @Test
    void sizeShouldReturnZeroOnEmptyTrie() {
        assertEquals(0, trie.size(), "bad size");
    }

    @Test
    void sizeShouldReturnCorrectSize() {
        trie.add("1");
        trie.add("2");
        trie.add("3");
        assertEquals(3, trie.size(), "bad size");
    }

    @Test
    void howManyStartsWithPrefixShouldReturnCorrectAnswer() {
        trie.add("1234");
        trie.add("1235");
        trie.add("123");
        assertEquals(3, trie.howManyStartsWithPrefix("123"), "bad howManyStartsWithPrefix");
    }

    @Test
    void howManyStartsWithPrefixShouldReturnZero() {
        trie.add("1234");
        trie.add("1235");
        trie.add("123");
        assertEquals(0, trie.howManyStartsWithPrefix("1236"), "bad howManyStartsWithPrefix");
    }

    @Test
    void serializeShouldBeDetermined() { // why i can don't write "throws IllegalStateException here??
        trie.add("123");
        trie.add("456");
        try (var stream1 = new ByteArrayOutputStream(); var stream2 = new ByteArrayOutputStream()) {
            trie.serialize(stream1);
            trie.serialize(stream2);
            assertArrayEquals(stream1.toByteArray(), stream2.toByteArray(), "bad serialize");
        } catch (IOException e) { // because of style-guide(inspections settings)
            throw new IllegalStateException(e);
        }
    }

    @Test
    void deserializeShouldBeDetermined() throws IOException { // but must write "throws IOException" here??
        trie.add("123");
        trie.add("456");
        try (var stream = new ByteArrayOutputStream()) {
            trie.serialize(stream);
            Trie trie1 = new Trie();
            try (var inputStream = new ByteArrayInputStream(stream.toByteArray())) {
                trie1.deserialize(inputStream);
            }
            Trie trie2 = new Trie();
            try (var inputStream = new ByteArrayInputStream(stream.toByteArray())) {
                trie2.deserialize(inputStream);
            }
            assertEquals(trie1.size(), trie2.size(), "bad deserialize");
        }
    }
}