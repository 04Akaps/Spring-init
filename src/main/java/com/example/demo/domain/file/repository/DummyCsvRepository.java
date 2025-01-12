package com.example.demo.domain.file.repository;

import com.example.demo.domain.file.model.YearBankSummaryResponseDTO;
import jakarta.validation.constraints.Max;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DummyCsvRepository extends JpaRepository<DummyCsv, Long> {

    @Query("SELECT d.year, d.bank, SUM(d.price) " +
            "FROM DummyCsv d " +
            "GROUP BY d.year, d.bank " +
            "ORDER BY d.year DESC")
    List<Object[]> findYearBankSum();


    @Query("SELECT d.year AS year, d.bank AS bank, SUM(d.price) AS totalAmount " +
            "FROM DummyCsv d " +
            "WHERE d.year BETWEEN :startYear AND :endYear " +
            "GROUP BY d.year, d.bank " +
            "ORDER BY d.year DESC, totalAmount DESC")
    List<Object[]> findMaxAmountPerYear(@Param("startYear") Integer startYear, @Param("endYear") Integer endYear);


    @Query("SELECT d.year, AVG(d.price) " +
            "FROM DummyCsv d " +
            "WHERE d.bank = :bankName AND d.year BETWEEN :startYear AND :endYear " +
            "GROUP BY d.year")
    List<Object[]> findAverageSupportAmountByBank(
            @Param("bankName") String bankName,
            @Param("startYear") Integer startYear,
            @Param("endYear") Integer endYear
    );
}