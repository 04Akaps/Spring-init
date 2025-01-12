package com.example.demo.domain.requestBinding.binding.Hex;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidator;

public class StartsWithHexValidator implements ConstraintValidator<StartWithHex, String> {

    @Override
    public void initialize(StartWithHex constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;  // null 값은 유효하지 않음
        }
        return value.startsWith("0x");
    }
}