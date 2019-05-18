package me.hwproj.gaev;

import java.util.*;

/**
 * Provides interface to add and execute LightFuture tasks in n threads.
 */
public class ThreadPoolImpl {
    private List<Thread> threadList = new ArrayList<>();
    private Queue<LightFuture<?>> taskQueue = new LinkedList<>();

    /**
     * Creates ThreadPoolImpl with n threads(workers) inside.
     */
    public ThreadPoolImpl(int n) {
        for (int i = 0; i < n; i++) {
            threadList.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    LightFuture<?> task;
                    try {
                        synchronized (taskQueue) {
                            task = taskQueue.poll();
                            while (task == null) {
                                wait();
                                task = taskQueue.poll();
                            }
                        }
                    } catch (InterruptedException e) {
                        return;
                    }
                    task.get();
                }
            }));
        }
    }

}
