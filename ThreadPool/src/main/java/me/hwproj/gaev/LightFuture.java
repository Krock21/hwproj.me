package me.hwproj.gaev;

import java.util.function.Function;

/**
 * Provides interface to create and modify tasks, check that the task is done, and get result of the task.
 * Task is just run "get" method in Supplier<T> and return result.
 */
public interface LightFuture<T> {
    /**
     * returns true, if task is done
     */
    boolean isReady();

    /**
     * waiting for the end of the task, then returns the result of the task.
     * If task throwed exception, then throws LightExecutionException
     */
    T get();

    /**
     * modifies task that it will add new task to the same ThreadPoolImpl,
     * which just run f.apply method on the result of this task
     */
    <R> LightFuture<R> thenApply(Function<T, R> f);
}
