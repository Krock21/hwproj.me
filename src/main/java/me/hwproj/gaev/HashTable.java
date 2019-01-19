package me.hwproj.gaev;

/**
 * HashTable
 */
public class HashTable {
    class StringList {
        protected class StringListItem {
            String key;
            String value;
            StringListItem next;
            StringListItem previous;

            StringListItem(String key, String value) {
                this.key = key;
                this.value = value;
                next = previous = null;
            }
        }

        private StringListItem head, tail;

        /**
         * finds StringListItem in StringList that stores s
         *
         * @param key String to find
         * @return StringListItem in StringList that stores s
         */
        private StringListItem findStringListItem(String key) {
            StringListItem cur = head;
            while (cur != null) {
                if (key.equals(cur.key)) {
                    return cur;
                }
                cur = cur.next;
            }
            return null;
        }

        /**
         * removes StringListItem from StringList
         *
         * @param item StringListItem in StringList
         * @return item
         */
        private StringListItem removeStringListItem(StringListItem item) {
            if (item == null) {
                return null;
            } else if (item == head) {
                head = head.next;
                if (head != null) {
                    head.previous = null;
                } else {
                    tail = null;
                }
            } else if (item == tail) {
                tail = item.previous;
                if (tail != null) {
                    tail.next = null;
                } else {
                    head = null;
                }
            } else {
                if (item.previous != null) {
                    item.previous.next = item.next;
                }
                if (item.next != null) {
                    item.next.previous = item.previous;
                }
            }
            return item;
        }

        /**
         * find String in StringList
         *
         * @param key String to find
         * @return null if key is not stored in the list, otherwise found String from StringList
         */
        String get(String key) {
            StringListItem item = findStringListItem(key);
            if (item == null) {
                return null;
            } else {
                return item.value;
            }
        }

        /**
         * removes String from StringList
         *
         * @param key String to remove
         * @return null if key is not stored in the list, otherwise removed String from StringList
         */
        String remove(String key) {
            StringListItem item = findStringListItem(key);
            item = removeStringListItem(item);
            if (item == null) {
                return null;
            } else {
                return item.value;
            }
        }

        /**
         * put pair (key,value) into HashTable
         *
         * @param key   String-key
         * @param value String-value
         * @return previous value for key, or null
         */
        String put(String key, String value) {
            StringListItem item = findStringListItem(key);
            if (item == null) {
                if (head == null) {
                    head = tail = new StringListItem(key, value);
                } else {
                    StringListItem previoustail = tail;
                    tail = new StringListItem(key, value);
                    tail.previous = previoustail;
                    if (previoustail != null) {
                        previoustail.next = tail;
                    }
                }
                return null;
            } else {
                removeStringListItem(item);
                put(key, value);
                return item.value;
            }
        }

        /**
         * check key for storing in hashtable
         *
         * @param key String-key
         * @return true, if HashTable stores key, false otherwise
         */
        boolean contains(String key) {
            StringListItem item = findStringListItem(key);
            return item != null;
        }

        /**
         * returns any key in list, or null
         *
         * @return String
         */
        String getany() {
            if (head == null) {
                return null;
            } else {
                return head.key;
            }
        }
    }

    private int tableItemCount, tableCapacity;
    private StringList[] table;

    HashTable() {
        this(1);
    }

    HashTable(int capacity) {
        tableItemCount = 0;
        tableCapacity = capacity;
        table = new StringList[tableCapacity];
    }

    /**
     * returns item count in HashTable
     *
     * @return item count in HashTable
     */
    public int size() {
        return tableItemCount;
    }

    /**
     * returns hashcode for String, useful for HashTable
     *
     * @param key String-key
     * @return hashcode
     */
    private int getHashCode(String key) {
        return key.hashCode() % tableCapacity;
    }

    /**
     * returns true, if key is stored in HashTable
     *
     * @param key String-key
     * @return boolean
     */
    public boolean contains(String key) {
        int hashcode = getHashCode(key);
        if (table[hashcode] == null) {
            return false;
        }
        return table[hashcode].contains(key);
    }

    /**
     * returns value for key, or null
     *
     * @param key String-key
     * @return String or null
     */
    public String get(String key) {
        int hashcode = getHashCode(key);
        if (table[hashcode] == null) {
            return null;
        }
        return table[hashcode].get(key);
    }

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
                while (cur.getany() != null) {
                    String key = cur.getany();
                    String value = cur.get(key);
                    cur.remove(key);
                    put(key, value);
                }
            }
        }
    }

    /**
     * save value for key, or replace
     *
     * @param key   String-key
     * @param value String-value
     * @return previous value for key, or null
     */
    public String put(String key, String value) {
        int hashcode = getHashCode(key);
        String previousValue;
        if (table[hashcode] == null) {
            table[hashcode] = new StringList();
        }
        previousValue = table[hashcode].put(key, value);
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
     * @return previous value
     */

    public String remove(String key) {
        int hashcode = getHashCode(key);
        if (table[hashcode] == null) {
            return null;
        }
        String value = table[hashcode].remove(key);
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