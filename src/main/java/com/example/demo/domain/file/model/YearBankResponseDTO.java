package com.example.demo.domain.file.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class YearBankResponseDTO {
    private String year;
    private String bank;
    private Long totalAmount;
}
