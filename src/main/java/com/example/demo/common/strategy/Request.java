package com.example.demo.common.strategy;

import java.util.concurrent.*;
import java.util.function.Supplier;

public class Request<T> {
    private final CountDownLatch latch = new CountDownLatch(1);
    private T result;
    private Throwable error;
    private boolean started = false;

    public boolean isNotStarted() {
        return !started;
    }

    public synchronized void start(Supplier<T> callBack) {
        started = true;


        try {
            this.result = callBack.get();
        } catch (Exception e) {
            this.error = e;
        } finally {
            this.latch.countDown();
        }
    
    }

    public T getResult() throws InterruptedException, ExecutionException {
        latch.await();            

        if (error != null) {
            throw new ExecutionException(error);
        }

        return result;
    }


}