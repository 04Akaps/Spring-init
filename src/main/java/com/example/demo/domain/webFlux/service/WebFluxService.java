package com.example.demo.domain.webFlux.service;


import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.domain.exception.Response;
import com.example.demo.domain.webFlux.model.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class WebFluxService {

    private final WebClient webClient;

    // "https://recoshopping.naver.com/api/v1/recoshopping?pageSize=3"

    public Response<ProductResponse> CallHTTP(String url) {
        Function<ClientResponse, ? extends Mono<Response<ProductResponse>>> responseHandler = clientResponse -> clientResponse.bodyToMono(ProductResponse.class).handle(
                (data, sink) -> {

                    if (!clientResponse.statusCode().is2xxSuccessful()) {
                        sink.error(new CustomException(ErrorCode.FailedToCallHTTP , clientResponse.statusCode().toString()));
                        return;
                    }


                    sink.next(Response.success(data));
                }
        );


        // 만약 비동기적 호출을 원한다면, subscribe
        return webClient.get().uri(url).exchangeToMono(responseHandler).block();
    }
}
