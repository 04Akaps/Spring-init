package com.example.demo.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;

@Slf4j
@Aspect
@Component
public class LoggingAspect {


    @Pointcut("execution(* com.example.demo..*(..))")
    public void all() {
    }

    @Pointcut("execution(* com.example.demo..*Controller.*(..))")
    public void controller() {
    }

    @Pointcut("execution(* com.example.demo..*Service.*(..))")
    public void service() {
    }


    @Around("all()") // (7)
    public Object logging(ProceedingJoinPoint joinPoint)  { // (7-1)
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            log.info("log = {}", joinPoint.getSignature());
            log.info("timeMs = {}", timeMs);
        }
    }

    @Before("controller() || service()") // (8)
    public void beforeLogic(JoinPoint joinPoint) { // (8-1)
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        log.info("method = {}", method.getName());


        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null) {
                log.info("type = {}", arg.getClass().getSimpleName());
                log.info("value = {}", arg);
            }

        }
    }

    @After("controller() || service()") // (9)
    public void afterLogic(JoinPoint joinPoint)  { // (9-1)
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        log.info("method = {}", method.getName());

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null) {
                log.info("type = {}", arg.getClass().getSimpleName());
                log.info("value = {}", arg);
            }


        }
    }

}