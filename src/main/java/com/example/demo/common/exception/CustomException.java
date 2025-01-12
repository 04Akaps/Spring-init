package com.example.demo.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final CodeInterface codeInterface;

    public CustomException(CodeInterface codeInterface) {
        super(codeInterface.getMessage());
        this.codeInterface = codeInterface;
    }

    public CustomException(CodeInterface codeInterface, String additionalMessage) {
        super(codeInterface.getMessage());
        codeInterface.setMessage(" : "+ additionalMessage);
        this.codeInterface = codeInterface;
    }
}