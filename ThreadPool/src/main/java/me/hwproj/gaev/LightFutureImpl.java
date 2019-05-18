package me.hwproj.gaev;

import java.util.function.Function;

public class LightFutureImpl<T> implements LightFuture<T> {
    private T result;

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public T get() {
        return null;
    }

    @Override
    public <R> LightFuture<R> thenApply(Function<T, R> f) {
        return null;
    }
}
