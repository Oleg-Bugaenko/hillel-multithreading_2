import java.util.Arrays;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeList<E> {
    private static final int START_CAPACITY = 10;
    private int currentRecord = 0;
    private Object[] array;
    private final ReentrantReadWriteLock lock;
    public ThreadSafeList() {
        array = new Object[START_CAPACITY];
        lock = new ReentrantReadWriteLock();
    }

    public int getSize() {
        return currentRecord;
    }

    public void add(E element) {
        lock.writeLock().lock();
        try {
            if (array.length == currentRecord ) {
                array = copyArray(array, array.length);
            }
            array[currentRecord] = element;
            currentRecord++;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void remove(E element) {
        lock.writeLock().lock();
        try {
            if (element.equals(array[currentRecord-1])) {
                currentRecord--;
            } else {
                for (int i = 0; i < currentRecord; i++) {
                    if (element.equals(array[i])) {
                        for (int j = i; j < currentRecord-1; j++) {
                            array[j] = array[j+1];
                        }
                        currentRecord--;
                        return;
                    }
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public E get(int index) {
        lock.readLock().lock();
        try {
            if (index >= currentRecord) {
                throw new IndexOutOfBoundsException("The specified index is out of the size of the array");
            }
            return (E) (array[index]);
        } finally {
            lock.readLock().unlock();
        }
    }

    public E[] get(){
        lock.readLock().lock();
        try {
            return (E[]) Arrays.copyOf(array, currentRecord);
        } finally {
            lock.readLock().unlock();
        }
    }

    private Object[] copyArray(Object[] src, int askSize) {
        Object[] des = new Object[(int) (askSize*1.5)];
        System.arraycopy(src, 0, des, 0, src.length);
        return des;
    }

}
