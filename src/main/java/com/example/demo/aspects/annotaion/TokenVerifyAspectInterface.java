package com.example.demo.aspects.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
// 적용되는 어노테이션 기준 -> Method만 적용
@Retention(RetentionPolicy.RUNTIME)
// 런타임에 사용, SOURCE : 컴파일에만 사용, RetentionPolicy : 클래스에만 사용
public @interface TokenVerifyAspectInterface {
}