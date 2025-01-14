package com.example.demo.domain.exception;

import com.example.demo.common.exception.CodeInterface;
import com.example.demo.common.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.*;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class) // 타입에러
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<Object>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String errorMessage = "Invalid value for parameter '" + ex.getName();

        Response<Object> errorResponse = BadRequest(errorMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<Object>> handleMissingParams(MissingServletRequestParameterException ex) {
        String errorMessage = "Required parameter '" + ex.getParameterName() + "' is missing";

        Response<Object> errorResponse = BadRequest(errorMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Object>> handleException(CustomException ex) {
        CodeInterface codeInterface = ex.getCodeInterface();
        String errorMessage = codeInterface.getMessage();
        Integer errorCode = codeInterface.getCode();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.error(errorCode, errorMessage));
    }

    private Response<Object> BadRequest(String msg) {
        return new Response<>(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                msg
        );
    }

}