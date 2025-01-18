package com.example.demo.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongoTableCollector {
    TEST("TEST");

    private final String dataBaseName;
}