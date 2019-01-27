package me.hwproj.gaev;

public class HashTable {
    private int tableItemCount;
    private int tableCapacity;

    private StringList[] table;

    /**
     * keep invariant that (item count)*2 < capacity by increasing capacity by 2 times if needed
     */
    private void rebalance() {
        if (tableItemCount * 2 >= tableCapacity) {
            StringList[] previousTable = table;
            tableItemCount = 0;
            tableCapacity *= 2;
            table = new StringList[tableCapacity];
            for (StringList cur : previousTable) {
                if (cur == null) {
                    continue;
                }
                while (cur.getAny() != null) {
                    String key = cur.getAny();
                    String value = cur.get(key);
                    cur.remove(key);
                    put(key, value);
                }
            }
        }
    }

    public HashTable() {
        this(1);
    }

    public HashTable(int capacity) {
        tableItemCount = 0; // for better understanding
        tableCapacity = capacity;
        table = new StringList[tableCapacity];
    }

    /**
     * returns item count in HashTable
     */
    public int size() {
        return tableItemCount;
    }

    /**
     * returns hashCode for String, useful for HashTable
     *
     * @param key String-key
     */
    private int getHashCode(String key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        return Math.abs(key.hashCode() % tableCapacity);
    }

    /**
     * returns true, if key is stored in HashTable
     *
     * @param key String-key
     */
    public boolean contains(String key) throws IllegalArgumentException {
        int hashCode = getHashCode(key);
        if (table[hashCode] == null) {
            return false;
        }
        return table[hashCode].contains(key);
    }

    /**
     * returns value for key, or null
     *
     * @param key String-key
     */
    public String get(String key) throws IllegalArgumentException {
        int hashCode = getHashCode(key);
        if (table[hashCode] == null) {
            return null;
        }
        return table[hashCode].get(key);
    }

    /**
     * save value for key, or replace
     *
     * @param key   String-key
     * @param value String-value
     * @return previous value for key, or null
     */
    public String put(String key, String value) throws IllegalArgumentException {
        int hashCode = getHashCode(key);
        if(value == null) {
            throw new IllegalArgumentException("Value can't be null");
        }
        String previousValue;
        if (table[hashCode] == null) {
            table[hashCode] = new StringList();
        }
        previousValue = table[hashCode].put(key, value);
        if (previousValue == null) {
            tableItemCount++;
        }
        rebalance();
        return previousValue;
    }

    /**
     * removes key
     *
     * @param key String-key
     * @return previous value for key, or null
     */

    public String remove(String key) throws IllegalArgumentException {
        int hashCode = getHashCode(key);
        if (table[hashCode] == null) {
            return null;
        }
        String value = table[hashCode].remove(key);
        if (value != null) {
            tableItemCount--;
        }
        return value;
    }

    /**
     * clears HashTable
     */
    public void clear() {
        tableItemCount = 0;
        tableCapacity = 1;
        table = new StringList[tableCapacity];
    }
}