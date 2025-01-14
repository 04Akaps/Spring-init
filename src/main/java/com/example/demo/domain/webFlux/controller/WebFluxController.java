package com.example.demo.domain.webFlux.controller;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.domain.exception.Response;
import com.example.demo.domain.webFlux.model.ProductResponse;
import com.example.demo.domain.webFlux.service.WebFluxService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/v1/webflux")
@RequiredArgsConstructor
public class WebFluxController {

    private final AtomicInteger test = new AtomicInteger(0);
    private final WebFluxService webFluxService;

    @GetMapping("/process-event")
    public Mono<Response<String>> processPayment() {

        Mono<String> one = Mono.fromCallable(() -> {
            System.out.println("one : ");
            try {
                Thread.sleep(3000); // 3초 지연
            } catch (InterruptedException e) {
                throw new CustomException(ErrorCode.FailedToMongoTest, e.getMessage());
            }
            return "Payment processed successfully one!" + test.get();
        }).subscribeOn(Schedulers.boundedElastic());

        Mono<String> two = Mono.fromCallable(() -> {
            System.out.println("two : ");
            try {
                Thread.sleep(1000); // 1초 지연
            } catch (InterruptedException e) {
                throw new CustomException(ErrorCode.FailedToMongoTest, e.getMessage());
            }
            return "Payment processed successfully two!" + test.get();
        }).subscribeOn(Schedulers.boundedElastic());

        Mono<String> three = Mono.fromCallable(() -> {
            System.out.println("three : ");
            try {
                Thread.sleep(2000); // 2초 지연
            } catch (InterruptedException e) {
                throw new CustomException(ErrorCode.FailedToMongoTest, e.getMessage());
            }
            return "Payment processed successfully three!" + test.get();
        }).subscribeOn(Schedulers.boundedElastic());

        Mono<String> four = three.doOnTerminate(() -> {
            // 이 부분에서 'three'의 결과를 사용할 수 있음
            System.out.println("four -s-d-fsd  : " + "Payment processed successfully three!");
        }).map((result) -> {
            System.out.println("four incoming: " + result);
            return result;
        });

        return Mono.zip(two, one, four)
                .map(tuple -> {
                    // 결과 조합
                    String paymentResult = tuple.getT1();
                    String fraudResult = tuple.getT2();
                    String notificationResult = tuple.getT3();

                    System.out.println(notificationResult);

                    // 모든 결과를 합쳐서 반환
                    return Response.success(paymentResult + ", " + fraudResult + ", " + notificationResult);
                });

    }


    @GetMapping("/call-http")
    public Response<ProductResponse> callHttp(@RequestParam @Valid String url) {
        return webFluxService.CallHTTP(url);
    }
}
