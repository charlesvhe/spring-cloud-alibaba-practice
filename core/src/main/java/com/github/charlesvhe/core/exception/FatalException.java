package com.github.charlesvhe.core.exception;

import com.github.charlesvhe.core.pojo.Response;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class FatalException extends RuntimeException {
    private Response<Object> response;

    public FatalException(Response<Object> response) {
        this.response = response;
    }

    public FatalException(String message) {
        super(message);
        this.response = new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    public FatalException(Throwable throwable) {
        super(throwable);
        this.response = new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), throwable.getMessage());
    }

    public FatalException(String message, Throwable throwable) {
        super(message, throwable);
        this.response = new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }
}
