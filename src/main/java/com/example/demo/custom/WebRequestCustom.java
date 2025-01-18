package com.example.demo.custom;


import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.custom.annotation.PageAnnotation;
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
public class WebRequestCustom implements HandlerMethodArgumentResolver {

    private static final List<String> ALLOWED_VALUES = Arrays.asList("ASC", "DESC", "asc", "desc");

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // Request에 대한 Annotaion 추가 되면, || 조건을 통해서 검증하는 로직을 추가 한다.
        return parameter.hasParameterAnnotation(PageAnnotation.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        // 추가되는 조건을 여기서 설정한다.
        PageAnnotation customAnnotation = parameter.getParameterAnnotation(PageAnnotation.class);
        String order = webRequest.getParameter(Objects.requireNonNull(parameter.getParameterName()));

        if (order != null && !ALLOWED_VALUES.contains(order)) {
            throw new CustomException(ErrorCode.NotSupportedOrderRequest, order);
        }

        if (order == null) {
            order = customAnnotation.defaultSort();
        }

        return order.toUpperCase();
    }

}
