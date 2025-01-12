package com.example.demo.domain.file.service;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.domain.file.model.YearAmountDTO;
import com.example.demo.domain.file.model.YearBankResponseDTO;
import com.example.demo.domain.file.model.YearBankSummaryResponseDTO;
import com.example.demo.domain.file.repository.DummyCsv;
import com.example.demo.domain.file.repository.DummyCsvRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DummyCsvService {
    private final DummyCsvRepository dummyCsvRepository;

    private AtomicInteger test = new AtomicInteger(0);

    @Transactional()
    public void saveAllDummyCsvList(List<DummyCsv> dummyCsvList) {
        dummyCsvRepository.saveAll(dummyCsvList);
    }

    @Transactional(readOnly = true)
    public List<YearBankSummaryResponseDTO> findYearBankSum() {
        int newValue = test.getAndIncrement();
        System.out.println("test = " + newValue);

        List<Object[]> results = dummyCsvRepository.findYearBankSum();
        Map<String, YearBankSummaryResponseDTO> summaryMap = new LinkedHashMap<>();

        for (Object[] result : results) {
            Integer year = (Integer) result[0];
            String bank = (String) result[1];
            Long price = (Long) result[2];

            String yearKey = year + "년"; // '년' 추가
            YearBankSummaryResponseDTO yearSummary = summaryMap.getOrDefault(yearKey,
                    new YearBankSummaryResponseDTO(yearKey, 0L, new HashMap<>()));

            // 3. 연도별 총합 및 세부 금액 처리
            yearSummary.setTotalAmount(yearSummary.getTotalAmount() + price);
            yearSummary.getDetailAmount().put(bank, price);

            // 4. 맵에 업데이트
            summaryMap.put(yearKey, yearSummary);
        }

        return new ArrayList<>(summaryMap.values());
    }


    @Transactional(readOnly = true)
    public List<YearBankResponseDTO> findYearBank(Integer startYear, Integer endYear) {
        // 쿼리에서 가져온 데이터 (각 년도별 각 기관의 금액 합계)
        List<Object[]> results = dummyCsvRepository.findMaxAmountPerYear(startYear, endYear);

        // 연도별로 가장 큰 금액을 기록한 기관을 저장할 리스트
        List<YearBankResponseDTO> response = new ArrayList<>();

        // 이전 년도를 추적할 변수
        String previousYear = null;
        String maxBank = null;
        Long maxAmount = 0L;

        for (Object[] result : results) {
            String year = String.valueOf(result[0]);
            String bank = String.valueOf(result[1]);
            Long totalAmount = (Long) result[2];

            // 만약 현재 년도가 이전 년도와 같다면, 가장 큰 금액 기관만 업데이트
            if (previousYear != null && previousYear.equals(year)) {
                if (totalAmount > maxAmount) {
                    maxAmount = totalAmount;
                    maxBank = bank;
                }
            } else {
                // 새 년도가 나오면 이전 년도를 리스트에 추가
                if (previousYear != null) {
                    response.add(new YearBankResponseDTO(previousYear, maxBank, maxAmount));
                }

                // 현재 년도에 대해 초기화
                previousYear = year;
                maxBank = bank;
                maxAmount = totalAmount;
            }
        }

        // 마지막 년도에 대한 추가
        if (previousYear != null) {
            response.add(new YearBankResponseDTO(previousYear, maxBank, maxAmount));
        }

        return response;
    }


    public Map<String, List<YearAmountDTO>> getMinMaxAmount(Integer startYear, Integer endYear, String bankName) {
        List<Object[]> results = dummyCsvRepository.findAverageSupportAmountByBank(bankName, startYear, endYear);

        if (results.isEmpty()) {
            throw new CustomException(ErrorCode.NoResultData);
        }

        // 2. 각 년도의 평균 금액을 계산하여 List에 저장
        List<YearAmountDTO> yearAmountList = new ArrayList<>();

        for (Object[] result : results) {
            Integer year = (Integer) result[0];
            Double averageAmount = (Double) result[1];

            // 소수점 이하 반올림 (반올림을 해야 하므로 Math.round 사용)
            Integer roundedAmount = (int) Math.round(averageAmount);

            yearAmountList.add(new YearAmountDTO(year, roundedAmount));
        }

        // 3. 가장 작은 값과 큰 값을 찾아서 반환
        YearAmountDTO minAmount = Collections.min(yearAmountList, Comparator.comparingInt(YearAmountDTO::getAmount));
        YearAmountDTO maxAmount = Collections.max(yearAmountList, Comparator.comparingInt(YearAmountDTO::getAmount));

        // 결과 맵에 최소, 최대 값 저장
        Map<String, List<YearAmountDTO>> response = new HashMap<>();
        response.put("bank", List.of(minAmount, maxAmount));

        return response;
    }

}
