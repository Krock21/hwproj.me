package me.hwproj.gaev;

class StringList {
    protected class StringListItem {
        String key;
        String value;
        StringListItem next;
        StringListItem previous;

        StringListItem(String key, String value) {
            this.key = key;
            this.value = value;
            next = null;
            previous = null;
        }
    }

    private StringListItem head;
    private StringListItem tail;

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
     */
    private void removeStringListItem(StringListItem item) {
        if (item != null) {
            if (item == head) {
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
        }
    }

    /**
     * find String in StringList
     *
     * @param key String to find
     * @return null if key is not stored in the list, otherwise found String from StringList
     */
    public String get(String key) {
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
    public String remove(String key) {
        StringListItem item = findStringListItem(key);
        removeStringListItem(item);
        if (item == null) {
            return null;
        } else {
            return item.value;
        }
    }

    /**
     * put pair (key,value) into StringList
     *
     * @param key   String-key
     * @param value String-value
     * @return previous value for key, or null
     */
    String put(String key, String value) {
        StringListItem item = findStringListItem(key);
        if (item == null) {
            if (head == null) {
                head = new StringListItem(key, value);
                tail = head;
            } else {
                StringListItem previousTail = tail;
                tail = new StringListItem(key, value);
                tail.previous = previousTail;
                if (previousTail != null) {
                    previousTail.next = tail;
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
     */
    String getAny() {
        if (head == null) {
            return null;
        } else {
            return head.key;
        }
    }
}