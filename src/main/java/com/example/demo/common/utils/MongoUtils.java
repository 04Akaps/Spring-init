package com.example.demo.common.utils;

import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.Map;

public class MongoUtils {

    public static Pageable pageable(int page, int size) {
        return PageRequest.of(page, size);
    }

    /**
     * Pageable 객체를 이용해 Query 객체를 생성합니다.
     * @param pageable 페이징 정보를 담고 있는 Pageable 객체
     * @return 페이징 정보를 포함한 Query 객체
     */
    public static Query createQuery(Pageable pageable) {
        return new Query().with(pageable);
    }

    /**
     * 여러 조건을 담은 쿼리 객체를 생성합니다.
     * @param pageable 페이징 정보를 담고 있는 Pageable 객체
     * @param criteriaMap 필드와 값의 맵
     * @return 조건이 추가된 Query 객체
     */
    public static Query createQuery(Pageable pageable, Map<String, Object> criteriaMap) {
        Query query = new Query().with(pageable);
        for (Map.Entry<String, Object> entry : criteriaMap.entrySet()) {
            query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
        }
        return query;
    }

    /**
     * 여러 조건을 AND 연산자로 결합한 쿼리 객체를 생성합니다.
     * @param pageable 페이징 정보를 담고 있는 Pageable 객체
     * @param criteria1 첫 번째 조건
     * @param criteria2 두 번째 조건
     * @return 조건이 추가된 Query 객체
     */
    public static Query createQuery(Pageable pageable, Criteria criteria1, Criteria criteria2) {
        Query query = new Query().with(pageable);
        query.addCriteria(new Criteria().andOperator(criteria1, criteria2));
        return query;
    }

    /**
     * 여러 조건을 담은 쿼리 객체를 생성합니다.
     * @param criteriaMap 필드와 값의 맵
     * @return 조건이 추가된 Query 객체
     */
    public static Query createQuery(Map<String, Object> criteriaMap) {
        Query query = new Query();
        for (Map.Entry<String, Object> entry : criteriaMap.entrySet()) {
            query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
        }
        return query;
    }

    public static <T> T findOne(MongoTemplate template, Query query, Class<T> targetClass) {
        return template.findOne(query, targetClass);
    }

    public static <T> List<T> find(MongoTemplate template, Query query, Class<T> targetClass) {
        return template.find(query, targetClass);
    }


    /**
     * 여러 필드와 값을 담은 업데이트 객체를 생성합니다.
     * @param updateMap 업데이트할 필드와 값의 맵
     * @return 필드와 값이 추가된 Update 객체
     */
    public static Update createUpdate(Map<String, Object> updateMap) {
        Update update = new Update();
        for (Map.Entry<String, Object> entry : updateMap.entrySet()) {
            update.set(entry.getKey(), entry.getValue());
        }
        return update;
    }

    public static <T> T update(MongoTemplate template, Query query, Update update, Class<T> targetClass) {
        return template.findAndModify(query, update, targetClass);
    }

    public static <T> T update(MongoTemplate template, Query query, Update update, Class<T> targetClass, boolean upsert) {
        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);
        if (upsert) {
            options.upsert(true);
        }
        return template.findAndModify(query, update, options, targetClass);
    }


    public static Query addProjection(Query query, List<String> fields) {
        if (fields != null && !fields.isEmpty()) {
            Field projectionFields = query.fields();
            for (String field : fields) {
                projectionFields.include(field);
            }
        }
        return query;
    }

    public static Query addSort(Query query, Map<String, Sort.Direction> sortFields) {
        if (sortFields != null && !sortFields.isEmpty()) {
            for (Map.Entry<String, Sort.Direction> entry : sortFields.entrySet()) {
                query.with(Sort.by(entry.getValue(), entry.getKey()));
            }
        }
        return query;
    }
}
