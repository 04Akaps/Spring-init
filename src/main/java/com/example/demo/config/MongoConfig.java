package com.example.demo.config;


import com.example.demo.common.enums.MongoTableCollector;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableMongoAuditing
public class MongoConfig {

    @Value("${spring.data.mongo.uri}")
    private String uri;


    @Bean
    public Map<MongoTableCollector, MongoTemplate> mongoTemplate() {
        Map<MongoTableCollector, MongoTemplate> hashMap = new HashMap<>();

        Arrays.stream(MongoTableCollector.values()).forEach(
                c -> {
                    MongoClient client = MongoClients.create(
                            MongoClientSettings.builder()
                                    .uuidRepresentation(UuidRepresentation.STANDARD) // BSON 변화 표준 -> 기본을 따른다.
                                    .applyConnectionString(new ConnectionString(uri)) // 접속 uri
                                    .readPreference(ReadPreference.primary()) // 모든 읽기 작업이 primary 노드에서 수행이 된다. -> 일관된 읽기를 보장
                                    .build()
                    );

                    hashMap.put(
                            c,
                            new MongoTemplate(
                                    new SimpleMongoClientDatabaseFactory(client, c.getDataBaseName())
                            )
                    );
                }
        );

        return hashMap;
    }

}
