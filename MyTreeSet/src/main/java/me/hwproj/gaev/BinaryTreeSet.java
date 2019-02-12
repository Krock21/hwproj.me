package me.hwproj.gaev;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * class implements interface MyTreeSet using binary tree.
 * All operations run in O(size) time.
 *
 * @param <E> type of values in set
 */
public class BinaryTreeSet<E> extends AbstractSet<E> implements MyTreeSet<E> {
    private int size;
    private int version;
    private boolean isInvert;
    private @NotNull Comparator<E> comparator;
    private Node<E> rootNode;

    public BinaryTreeSet() {
        comparator = new Comparator<E>() {
            @Override
            @SuppressWarnings("unchecked")
            public int compare(@NotNull E o1, @NotNull E o2) {
                return ((Comparable<E>) o1).compareTo(o2);
            }
        };
    }

    public BinaryTreeSet(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    private static class Node<E> {
        private E value;
        private Node<E> parent;
        private Node<E> left;
        private Node<E> right;

        private @Nullable Node<E> getNext() {
            Node<E> answer;
            if (right == null) {
                answer = parent;
                Node<E> previous = this;
                while (answer != null && answer.left != previous) {
                    previous = answer;
                    answer = answer.parent;
                }
            } else {
                answer = right;
                while (answer.left != null) {
                    answer = answer.left;
                }
            }
            return answer;
        }

        private @Nullable Node<E> getPrevious() {
            Node<E> answer;
            if (left == null) {
                answer = parent;
                Node<E> previous = this;
                while (answer != null && answer.right != previous) {
                    previous = answer;
                    answer = answer.parent;
                }
            } else {
                answer = left;
                while (answer.right != null) {
                    answer = answer.right;
                }
            }
            return answer;
        }
    }

    private static class BinaryTreeSetIterator<E> implements Iterator<E> { // doesn't need access to outter class
        protected Node<E> position;

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return position.getNext() != null;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public E next() {
            if (position == null) {
                throw new NoSuchElementException();
            }
            E answer = position.value;
            position = position.getNext();
            return answer;
        }
    }

    private static class DescendingBinaryTreeSetIterator<E> extends BinaryTreeSetIterator<E> {
        @Override
        public boolean hasNext() {
            return position.getPrevious() != null;
        }

        @Override
        public E next() {
            if (position == null) {
                throw new NoSuchElementException();
            }
            E answer = position.value;
            position = position.getPrevious();
            return answer;
        }

    }

    /**
     * Returns an iterator over the elements contained in this collection.
     *
     * @return an iterator over the elements contained in this collection
     */
    @Override
    public Iterator<E> iterator() {
        if (isInvert) {
            @Nullable Node<E> maximumNode = rootNode;
            while (maximumNode.right != null) {
                maximumNode = maximumNode.right;
            }
            var answer = new DescendingBinaryTreeSetIterator<E>();
            answer.position = maximumNode;
            return answer;
        } else {
            @Nullable Node<E> minimumNode = rootNode;
            while (minimumNode.left != null) {
                minimumNode = minimumNode.left;
            }
            var answer = new BinaryTreeSetIterator<E>();
            answer.position = minimumNode;
            return answer;
        }
    }

    /**
     * Adds the specified element to this set if it is not already present
     * (optional operation).  More formally, adds the specified element
     * {@code e} to this set if the set contains no element {@code e2}
     * such that
     * {@code Objects.equals(e, e2)}.
     * If this set already contains the element, the call leaves the set
     * unchanged and returns {@code false}.  In combination with the
     * restriction on constructors, this ensures that sets never contain
     * duplicate elements.
     *
     * <p>The stipulation above does not imply that sets must accept all
     * elements; sets may refuse to add any particular element, including
     * {@code null}, and throw an exception, as described in the
     * specification for {@link Collection#add Collection.add}.
     * Individual set implementations should clearly document any
     * restrictions on the elements that they may contain.
     *
     * @param e element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     * element
     * @throws UnsupportedOperationException if the {@code add} operation
     *                                       is not supported by this set
     * @throws ClassCastException            if the class of the specified element
     *                                       prevents it from being added to this set
     * @throws NullPointerException          if the specified element is null and this
     *                                       set does not permit null elements
     * @throws IllegalArgumentException      if some property of the specified element
     *                                       prevents it from being added to this set
     */
    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        if (rootNode == null) {
            rootNode = new Node<>();
            rootNode.value = e;
            return true;
        }
        Node<E> current = rootNode;
        while (current != null && current.value != e) {
            if (comparator.compare(e, current.value) < 0) {
                if (current.left == null) {
                    current.left = new Node<>();
                    current.left.value = e;
                    current.left.parent = current;
                    return true;
                } else {
                    current = current.left;
                }
            } else {
                if (current.right == null) {
                    current.right = new Node<>();
                    current.right.value = e;
                    current.right.parent = current;
                    return true;
                } else {
                    current = current.right;
                }
            }
        }
        return false;
    }

    /**
     * Removes the specified element from this set if it is present
     * (optional operation).  More formally, removes an element {@code e}
     * such that
     * {@code Objects.equals(o, e)}, if
     * this set contains such an element.  Returns {@code true} if this set
     * contained the element (or equivalently, if this set changed as a
     * result of the call).  (This set will not contain the element once the
     * call returns.)
     *
     * @param o object to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws ClassCastException            if the type of the specified element
     *                                       is incompatible with this set
     *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if the specified element is null and this
     *                                       set does not permit null elements
     *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws UnsupportedOperationException if the {@code remove} operation
     *                                       is not supported by this set
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        var currentNode = rootNode;
        if (currentNode == null) {
            return false;
        }
        while (currentNode != null && !currentNode.value.equals(o)) {
            if (comparator.compare((E) o, currentNode.value) < 0) {
                currentNode = currentNode.left;
            } else {
                currentNode = currentNode.right;
            }
        }
        if (currentNode == null) {
            return false;
        }
        if (currentNode.right == null) {
            if (currentNode.parent != null) {
                if (currentNode.parent.left == currentNode) {
                    currentNode.parent.left = currentNode.left;
                } else {
                    currentNode.parent.right = currentNode.left;
                }
            }
            currentNode.left.parent = currentNode.parent;
        } else {
            @NotNull var nextNode = currentNode.getNext();
            if (nextNode.parent != null) {
                if (nextNode.parent.left == nextNode) {
                    nextNode.parent.left = nextNode.right;
                } else {
                    nextNode.parent.right = nextNode.right;
                }
            }
            nextNode.right.parent = nextNode.parent;

            if (currentNode.parent != null) {
                if (currentNode.parent.left == currentNode) {
                    currentNode.parent.left = nextNode;
                } else {
                    currentNode.parent.right = nextNode;
                }
            }
            nextNode.left = currentNode.left;
            nextNode.right = currentNode.right;
            if (nextNode.left != null) {
                nextNode.left.parent = nextNode;
            }
            if (nextNode.right != null) {
                nextNode.right.parent = nextNode;
            }
        }
        return true;
    }

    /**
     * returns count of different elements in set
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * {@link TreeSet#descendingIterator()}
     **/
    @Override
    public Iterator<E> descendingIterator() {
        isInvert = !isInvert;
        var answer = iterator();
        isInvert = !isInvert;
        return answer;
    }

    /**
     * {@link TreeSet#descendingSet()}
     **/
    @Override
    public MyTreeSet<E> descendingSet() {
        var answer = new BinaryTreeSet<E>();
        answer.size = size;
        answer.rootNode = rootNode;
        answer.comparator = comparator;
        answer.version = version;
        answer.isInvert = !isInvert;
        return answer;
    }

    /**
     * {@link TreeSet#first()}
     **/
    @Override
    public E first() {
        var iterator = iterator();
        if (!iterator.hasNext()) {
            throw new NoSuchElementException();
        }
        return iterator.next();
    }

    /**
     * {@link TreeSet#last()}
     **/
    @Override
    public E last() {
        var iterator = iterator();
        if (!iterator.hasNext()) {
            throw new NoSuchElementException();
        }
        return iterator.next();
    }

    /**
     * {@link TreeSet#lower(E)}
     */
    @Override
    public E lower(E e) {
        var iterator = iterator();
        int factor = 1;
        if (isInvert) {
            factor = -1;
        }
        E answer = null;
        while (iterator.hasNext()) {
            E current = iterator.next();
            if (factor * comparator.compare(e, iterator.next()) < 0) {
                answer = e;
            } else {
                break;
            }
        }
        return answer;
    }

    /**
     * {@link TreeSet#floor(E)}
     */
    @Override
    public E floor(E e) {
        var iterator = iterator();
        int factor = 1;
        if (isInvert) {
            factor = -1;
        }
        E answer = null;
        while (iterator.hasNext()) {
            E current = iterator.next();
            if (factor * comparator.compare(e, iterator.next()) <= 0) {
                answer = e;
            } else {
                break;
            }
        }
        return answer;
    }

    /**
     * {@link TreeSet#ceiling(E)}
     */
    @Override
    public E ceiling(E e) {
        int factor = 1;
        if (isInvert) {
            factor = -1;
        }
        var iterator = descendingIterator();
        E answer = null;
        while (iterator.hasNext()) {
            E current = iterator.next();
            if (factor * comparator.compare(e, iterator.next()) >= 0) {
                answer = e;
            } else {
                break;
            }
        }
        return answer;
    }

    /**
     * {@link TreeSet#higher(E)}
     */
    @Override
    public E higher(E e) {
        var iterator = descendingIterator();
        int factor = 1;
        if (isInvert) {
            factor = -1;
        }
        E answer = null;
        while (iterator.hasNext()) {
            E current = iterator.next();
            if (factor * comparator.compare(e, iterator.next()) > 0) {
                answer = e;
            } else {
                break;
            }
        }
        return answer;
    }
}
