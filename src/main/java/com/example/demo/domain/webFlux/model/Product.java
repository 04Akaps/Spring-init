package com.example.demo.domain.webFlux.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private String adType;
    private String imageUrl;
    private String productTitle;
    private int minPrice;
    private String adCntsSeq;
    private String bizCd;
}
