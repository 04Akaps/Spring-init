package com.example.demo.custom;


import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.custom.annotation.PageAnnotation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class PageCustom implements HandlerMethodArgumentResolver {

    private static final List<String> ALLOWED_VALUES = Arrays.asList("ASC", "DESC", "asc", "desc");

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(PageAnnotation.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        PageAnnotation customAnnotation = parameter.getParameterAnnotation(PageAnnotation.class);
        String order = webRequest.getParameter(Objects.requireNonNull(parameter.getParameterName()));

        if (order != null && !ALLOWED_VALUES.contains(order)) {
            throw new CustomException(ErrorCode.NotSupportedOrderRequest, order);
        }

        if (order == null) {
            order = customAnnotation.defaultSort();
        }

        return order.toUpperCase(); // Ensure the value is always in uppercase
    }

}
