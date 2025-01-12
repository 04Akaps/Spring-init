package com.example.demo.common.strategy;

public class Result<T> {
    private final T result;
    private final Throwable error;

    public Result(T result, Throwable error) {
        this.result = result;
        this.error = error;
    }

    public T getResult() {
        return result;
    }

    public Throwable getError() {
        return error;
    }

    public boolean isError() {
        return error != null;
    }
}