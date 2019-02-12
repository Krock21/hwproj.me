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
    private final @NotNull Comparator<E> comparator;
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
            return super.position.getPrevious() != null;
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
        Node<E> minimumNode = rootNode;
        while (minimumNode.left != null) {
            minimumNode = minimumNode.left;
        }
        var answer = new BinaryTreeSetIterator<E>();
        answer.position = minimumNode;
        return answer;
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

    @Override
    public int size() {
        return size;
    }

    /**
     * {@link TreeSet#descendingIterator()}
     **/
    @Override
    public Iterator<E> descendingIterator() {
        Node<E> maximumNode = rootNode;
        while (maximumNode.right != null) {
            maximumNode = maximumNode.right;
        }
        var answer = new BinaryTreeSetIterator<E>();
        answer.position = maximumNode;
        return answer;
    }

    /**
     * {@link TreeSet#descendingSet()}
     **/
    @Override
    public MyTreeSet<E> descendingSet() {
        return null;
    }

    /**
     * {@link TreeSet#first()}
     **/
    @Override
    public E first() {
        return null;
    }

    /**
     * {@link TreeSet#last()}
     **/
    @Override
    public E last() {
        return null;
    }

    /**
     * {@link TreeSet#lower(E)}
     *
     * @param e
     */
    @Override
    public E lower(E e) {
        return null;
    }

    /**
     * {@link TreeSet#floor(E)}
     *
     * @param e
     */
    @Override
    public E floor(E e) {
        return null;
    }

    /**
     * {@link TreeSet#ceiling(E)}
     *
     * @param e
     */
    @Override
    public E ceiling(E e) {
        return null;
    }

    /**
     * {@link TreeSet#higher(E)}
     *
     * @param e
     */
    @Override
    public E higher(E e) {
        return null;
    }
}
