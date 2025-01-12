package com.example.demo.domain.file.support;


import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvFileReader {

     public List<String[]> read(InputStream stream) {
        List<String[]> results = new ArrayList<>();

         try (CSVReader reader = new CSVReader(new InputStreamReader(stream))) {
             String[] nextLine;
             while ((nextLine = reader.readNext()) != null) {
                 results.add(nextLine);
             }
         } catch (IOException | CsvValidationException e) {
             throw new CustomException(ErrorCode.FailedToReadStream, e.getMessage());
         }

         return results;
     }

}
