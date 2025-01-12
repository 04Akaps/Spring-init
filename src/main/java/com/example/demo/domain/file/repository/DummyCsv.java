package com.example.demo.domain.file.repository;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dummy_csv")
@NoArgsConstructor
public class DummyCsv {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year")
    private Integer year;

    @Column(name = "month")
    private Integer month;

    @Column(name = "bank")
    private String bank;

    @Column(name = "price")
    private Long price;


    public DummyCsv(Integer year, Integer month, String bank, Long price) {
        this.year = year;
        this.month = month;
        this.bank = bank;
        this.price = price;
    }
}