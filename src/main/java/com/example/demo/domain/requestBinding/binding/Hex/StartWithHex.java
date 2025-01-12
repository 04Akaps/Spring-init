package com.example.demo.domain.requestBinding.binding.Hex;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StartsWithHexValidator.class)  // 실제 검증 로직을 처리할 Validator 지정
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface StartWithHex {
    String message() default "The key must start with '0x'";  // 기본 메시지
    Class<?>[] groups() default {};  // 그룹 설정 (디폴트는 없음)
    Class<? extends Payload>[] payload() default {};  // 페이로드 설정 (디폴트는 없음)
}