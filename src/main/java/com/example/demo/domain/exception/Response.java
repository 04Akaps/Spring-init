package com.example.demo.domain.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Response<T> {
    private int code;
    private String message;
    private T result;

    public static <T> Response<T> success(T result) {
        return new Response<>(200, "", result);
    }

    public static <T> Response<T> error(Integer code, String message) {
        return new Response<>(code, message, null);
    }
}
