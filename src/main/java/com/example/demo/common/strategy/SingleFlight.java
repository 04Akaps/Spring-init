package com.example.demo.common.strategy;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;
import com.example.demo.common.strategy.Result;
import com.example.demo.common.strategy.Request;

import java.util.concurrent.*;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SingleFlight<T> {

    private final ConcurrentHashMap<String, Request<T>> requestMap = new ConcurrentHashMap<>();

    public Result<T> handleRequest(String key, Supplier<T> callBack)  {
        Request<T> request = requestMap.computeIfAbsent(key, k -> new Request<>());

        if (request.isNotStarted()) {
            request.start(callBack);
        }

        try {
            T result = request.getResult();
            requestMap.remove(key);
            return new Result<>(result, null);
        } catch (InterruptedException | ExecutionException e) {
            requestMap.remove(key);
            return new Result<>(null, e); 
        }
    }

}