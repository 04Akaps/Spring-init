package com.example.demo.domain.webFlux.config;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5_000)
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS))
                );
    }

    @Bean
    public ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("http-pool")
                .maxConnections(100)
                .pendingAcquireTimeout(Duration.ofMillis(0))
                .pendingAcquireMaxCount(-1)
                .maxIdleTime(Duration.ofMillis(2000L))
                .build();
    }

    @Bean
    public ExchangeStrategies defaultExchangeStrategies() {
        ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(new JavaTimeModule());
        return ExchangeStrategies.builder().codecs(config -> {
            config.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(om, MediaType.APPLICATION_JSON));
            config.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(om, MediaType.APPLICATION_JSON));
            config.defaultCodecs().maxInMemorySize(1024 * 1024);
        }).build();
    }

}
