package me.hwproj.gaev;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * List with костыли on size <= 5
 *
 * @param <E> type of elements in list
 */
public class SmartList<E> extends AbstractList<E> {
    private int size;
    private Object data;

    public SmartList() {
        size = 0;// redundant
        data = null; // redundant
    }

    public SmartList(Collection<E> values) {
        SmartList<E> list = getSmartList(values);
        size = list.size;
        data = list.data;
    }

    public static <E> SmartList<E> getSmartList(Collection<E> values) {
        SmartList<E> list = new SmartList<>();
        for (E i : values) {
            list.add(list.size(), i);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void add(int index, E value) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 0) {
            data = value;
            size = 1;
        } else if (size == 1) {
            Object[] arrayData = new Object[5];
            if (index == 0) {
                arrayData[1] = data;
                arrayData[0] = value;
            } else {
                arrayData[0] = data;
                arrayData[1] = value;
            }
            data = arrayData;
            size = 2;
        } else if (size < 5) {
            Object[] objectData = (Object[]) data;
            System.arraycopy(objectData, index, objectData, index + 1, 4 - index);
            /*for (int i = 4; i > index; i--) {
                objectData[i] = objectData[i - 1];
            }*/
            objectData[index] = value;
            size++;
        } else if (size == 5) {
            List<E> arrayListData = new ArrayList<E>();
            for (int i = 0; i < 5; i++) {
                arrayListData.add(i, (E) ((Object[]) data)[i]);
            }
            arrayListData.add(index, value);
            data = arrayListData;
            size = 6;
        } else if (size > 5) {
            ((List<E>) data).add(index, value);
            size++;
        } else {
            throw new NullPointerException(); //it means that i am a bad programmer
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        E answer = null;
        if (size == 1) {
            answer = (E) data;
            data = null;
            size = 0;
        } else if (size <= 5) {
            Object[] objectData = (Object[]) data;
            answer = (E) objectData[index];
            if (index == 4) {
                objectData[4] = null;
            } else {
                System.arraycopy(objectData, index + 1, objectData, index, 4 - index);
                objectData[index] = null;
            }
            size--;
        } else if (size == 6) {
            List<E> arrayListData = (List<E>) data;
            answer = arrayListData.remove(index);
            Object[] objectData = new Object[5];
            for (int i = 0; i < 5; i++) {
                objectData[i] = arrayListData.get(i);
            }
            data = objectData;
            size = 5;
        } else if (size > 6) {
            size--;
            List<E> arrayListData = (List<E>) data;
            answer = arrayListData.remove(index);
        } else {
            throw new NullPointerException();//it means that i am a bad programmer
        }
        return answer;
    }

    @Override
    public E set(int index, E value) {
        E answer = remove(index);
        add(index, value);
        return answer;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException();
        }
        E answer = null;
        if (size == 1) {
            answer = (E) data;
        } else if (size <= 5) {
            answer = (E) ((Object[]) data)[index];
        } else {
            answer = ((ArrayList<E>) data).get(index);
        }
        return answer;
    }

    @Override
    public int size() {
        return size;
    }
}