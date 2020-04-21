package com.github.charlesvhe.core.pojo;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Date;

@Data
public class Response<T> implements Serializable {
    private Date timestamp = new Date();
    private int code = HttpStatus.OK.value();
    private String message = HttpStatus.OK.getReasonPhrase();
    private T data = null;

    public Response() {
    }

    public Response(T data) {
        this.data = data;
    }

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
