package com.example.demo.domain.webFlux.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Board {
    private List<Product> products;
}