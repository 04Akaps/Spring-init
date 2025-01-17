package com.example.demo.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(ExchangeStrategies exchangeStrategies, HttpClient httpClient) {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchangeStrategies(exchangeStrategies)
                .build();
    }

    @Bean
    public HttpClient defaultHttpClient(ConnectionProvider provider) {
        return HttpClient
                .create(provider)
                .wiretap(this.getClass().getCanonicalName(), LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5_000) // call time out
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS)).
                                addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS))
                );
    }

    @Bean
    public ConnectionProvider connectionProvider() {
        // HTTP의 커넥션 풀을 관리
        // pendingAcquireTimeout : 커넥션 대기 시간
        // pendingAcquireMaxCount : 커넥션 대기 요청 수 -> 무제한
        // maxIdleTime : 커넥션 최대 유효 시간
        return ConnectionProvider.builder("http-pool")
                .maxConnections(100)
                .pendingAcquireTimeout(Duration.ofMillis(0))
                .pendingAcquireMaxCount(-1)
                .maxIdleTime(Duration.ofMillis(2000L))
                .build();
    }

    @Bean
    public ExchangeStrategies defaultExchangeStrategies(ObjectMapper om) {
        // FAIL_ON_UNKNOWN_PROPERTIES : 응답값에서 예상하지 못한 필드가 있어도 에러가 아니다.
        // new JavaTimeModule : LocalDate, Instant등의 날짜 데이터를 지원하겠다.
        // maxInMemorySize : 요청 및 응답의 최대 메모리 크기를 1MB로 설정한다.
        // Jackson을 통한 인코딩 및 디코딩 진행
        return ExchangeStrategies.builder().
                codecs(config -> {
            config.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(om, MediaType.APPLICATION_JSON));
            config.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(om, MediaType.APPLICATION_JSON));
            config.defaultCodecs().maxInMemorySize(1024 * 1024);
        }).build();
    }


    @Bean
    public ObjectMapper objectMapper() {
        // ObjectMapper를 Bean으로 등록함으로써 재사용
        // AfterburnerModule : 직렬화 및 역직렬화 성능을 위한 모듈
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Java 8 날짜/시간 지원
        objectMapper.registerModule(new AfterburnerModule()); // 성능 향상 모듈
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

}
