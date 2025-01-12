package com.example.demo.domain.file.service;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.common.strategy.Result;
import com.example.demo.common.strategy.SingleFlight;
import com.example.demo.domain.exception.Response;
import com.example.demo.domain.file.model.YearAmountDTO;
import com.example.demo.domain.file.model.YearBankResponseDTO;
import com.example.demo.domain.file.model.YearBankSummaryResponseDTO;
import com.example.demo.domain.file.repository.DummyCsv;
import com.example.demo.domain.file.support.CsvFileParser;
import com.example.demo.domain.file.support.CsvFileReader;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileService {

    private final CsvFileReader csvFileReader;
    private final CsvFileParser csvFileParser;
    private final DummyCsvService dummyCsvService;

    private final SingleFlight<Response<List<YearBankSummaryResponseDTO>>> singleFlight;

    public Response<String> UploadFileToDB(String fileName) {
        StringBuilder sb = new StringBuilder(fileName);
        sb.append(".csv");
        String file = sb.toString();

        try {
            Resource resource = new ClassPathResource(file);

            InputStream stream = resource.getInputStream();

            List<String[]> result = csvFileReader.read(stream);
            List<String> header = csvFileParser.parseHeader(result);
            List<List<String>> body = csvFileParser.parseBody(result);

            resolveDummyCsv(header, body);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FailedToReadFile, e.getMessage());
        }

        return Response.success("success");
    }

    public Response<List<YearBankSummaryResponseDTO>> yearBankSummary() {

        Result<Response<List<YearBankSummaryResponseDTO>>> result = singleFlight.handleRequest("test", () -> Response.success(dummyCsvService.findYearBankSum()));

        if (result.getError() != null) {
            throw new CustomException(ErrorCode.FailedToDoSingleFlight, result.getError().toString());
        }

        return result.getResult();
    }

    public Response<List<YearBankResponseDTO>> findYearBank(Integer startYear, Integer endYear) {
        return Response.success(dummyCsvService.findYearBank(startYear, endYear));
    }

    public Response<Map<String, List<YearAmountDTO>>> minMaxBank(Integer startYear, Integer endYear, String bankName) {
        return Response.success(dummyCsvService.getMinMaxAmount(startYear, endYear, bankName));
    }

    private void resolveDummyCsv(List<String> header, List<List<String>> body) {
        // 0 -> 연도
        // 1 -> 월
        // 나머지 목차는 bank

        List<String> bankNames = header.subList(2, header.size());
        List<DummyCsv> dummyCsvList = new ArrayList<>();


        for (List<String> row : body) {
            String year = row.get(0);
            String month = row.get(1);

            for (int i = 2; i < row.size(); i++) {
                String bank = bankNames.get(i - 2).replace("(억원)", "").replace("1)", "").trim(); // bank 이름 (헤더에서 가져오기)
                String priceStr = row.get(i); // 해당 은행의 가격 (해당 열에서 가져오기)

                // 쉼표를 제거하고 숫자 변환
                Long price = Long.parseLong(priceStr.replace(",", ""));

                // DummyCsv 객체 생성 후 리스트에 추가
                dummyCsvList.add(new DummyCsv(Integer.parseInt(year), Integer.parseInt(month), bank, price));
            }
        }

        dummyCsvService.saveAllDummyCsvList(dummyCsvList);
    }




}
