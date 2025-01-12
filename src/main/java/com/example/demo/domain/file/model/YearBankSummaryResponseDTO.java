package com.example.demo.domain.file.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
public class YearBankSummaryResponseDTO {
    private String year;
    private Long totalAmount;
    private Map<String, Long> detailAmount;
}