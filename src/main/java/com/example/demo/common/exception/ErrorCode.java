package com.example.demo.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DialectOverride;

@AllArgsConstructor
@Getter
public enum ErrorCode implements  CodeInterface {
    NoResultData(1, "no result data"),
    FailedToDoSingleFlight(2, "server internal error"),
    FailedToMongoTest(3, "mono test failed"),
    FailedToCallHTTP(4, "failed to call http"),

    FailedToReadFile(101, "Failed to read file"),
    FailedToReadStream(100, "Failed to read stream");

    private final Integer code;
    private String message;


    @Override
    public void setMessage(String message) {
        this.message = this.message + message;
    }
}
