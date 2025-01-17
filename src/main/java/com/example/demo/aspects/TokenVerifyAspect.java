package com.example.demo.aspects;

import com.example.demo.aspects.annotaion.TokenVerifyAspectInterface;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class TokenVerifyAspect {

    private final HttpServletRequest request;

    @Before("@annotation(com.example.demo.aspects.annotaion.TokenVerifyAspectInterface)")
    public void checkHeaderToken() {
        String token = request.getHeader("Authorization");
        System.out.println(token);
    }
}