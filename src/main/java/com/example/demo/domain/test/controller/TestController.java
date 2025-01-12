package com.example.demo.domain.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.strategy.Result;
import com.example.demo.common.strategy.SingleFlight;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TestController {

    final private SingleFlight<String> singleFlight;

    @GetMapping("/test")
    public String getData(@RequestParam String key) {
        Result<String> result = singleFlight.handleRequest(key, () -> {
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            return "Data for key: " + key;
        });

        return result.getResult();
    }

    
}