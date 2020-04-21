package com.github.charlesvhe.core.exception;

import com.github.charlesvhe.core.pojo.Response;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BusinessException extends RuntimeException {
    private Response<Object> response;

    public BusinessException(Response<Object> response) {
        this.response = response;
    }

    public BusinessException(String message) {
        super(message);
        this.response = new Response<>(HttpStatus.BAD_REQUEST.value(), message);
    }

    public BusinessException(Throwable throwable) {
        super(throwable);
        this.response = new Response<>(HttpStatus.BAD_REQUEST.value(), throwable.getMessage());
    }

    public BusinessException(String message, Throwable throwable) {
        super(message, throwable);
        this.response = new Response<>(HttpStatus.BAD_REQUEST.value(), message);
    }
}
