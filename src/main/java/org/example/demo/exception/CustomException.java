package org.example.demo.exception;


import lombok.Getter;
import org.example.demo.domain.emuns.ResponseEnum;

@Getter
public class CustomException extends RuntimeException {

    private final ResponseEnum responseEnum;

    public CustomException(ResponseEnum responseEnum, String message) {
        super(message);
        this.responseEnum = responseEnum;
    }

}
