package com.example.demo.domain.file.controller;


import com.example.demo.domain.exception.Response;
import com.example.demo.domain.file.model.YearAmountDTO;
import com.example.demo.domain.file.model.YearBankResponseDTO;
import com.example.demo.domain.file.model.YearBankSummaryResponseDTO;
import com.example.demo.domain.file.service.FileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/file")
@Tag(name = "file controller")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/upload")
    public Response<String> upload(
            @RequestParam @Valid String fileName
    ) {
        return fileService.UploadFileToDB(fileName);
    }


    @GetMapping("/year-month-summary")
    public Response<List<YearBankSummaryResponseDTO>> yearBankSummary() {
        return fileService.yearBankSummary();
    }


    @GetMapping("/year-max-bank")
    public  Response<List<YearBankResponseDTO>> findYearBank(
            @RequestParam @Valid @NotNull Integer startYear,
            @RequestParam  @Valid @NotNull Integer endYear
    ) {
        return fileService.findYearBank(startYear, endYear);
    }


    @GetMapping("/min-max-bank")
    public Response<Map<String, List<YearAmountDTO>>> findMinMaxBank(
            @RequestParam @Valid @NotNull Integer startYear,
            @RequestParam  @Valid @NotNull Integer endYear,
            @RequestParam  @Valid @NotNull String  bankName
    ) {
        return fileService.minMaxBank(startYear, endYear, bankName);
    }

}
