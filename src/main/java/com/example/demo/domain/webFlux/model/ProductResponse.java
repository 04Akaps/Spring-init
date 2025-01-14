package com.example.demo.domain.webFlux.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductResponse {
    private List<String> titleList;
    private List<Board> boards;
    private boolean isFallback;
}
