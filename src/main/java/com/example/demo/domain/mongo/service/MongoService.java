package com.example.demo.domain.mongo.service;


import com.example.demo.common.enums.MongoTableCollector;
import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.common.utils.MongoUtils;
import com.example.demo.domain.exception.Response;
import com.example.demo.domain.mongo.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Pageable;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MongoService {

    private final Map<MongoTableCollector, MongoTemplate> mongoTemplate;
    private final int maxLimit = 50_000;


    private Response<User> users(String table, int page, int size) {
        if (size > maxLimit) {
            throw new CustomException(ErrorCode.OverLimit ,size);
        }

        MongoTemplate template = getTemplate(table);
        Pageable pageable = MongoUtils.pageable(page, size);

        Query query =  MongoUtils.createQuery(pageable);


    }

    private MongoTemplate getTemplate(String name) {
        try {
            return mongoTemplate.get(MongoTableCollector.valueOf(name.toUpperCase()));
        }catch (Exception e){
            throw new CustomException(ErrorCode.FailedToFindMongoTemplate, name);
        }
    }
}
