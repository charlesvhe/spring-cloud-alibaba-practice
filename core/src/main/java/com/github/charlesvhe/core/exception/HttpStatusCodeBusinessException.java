package com.github.charlesvhe.core.exception;

import com.github.charlesvhe.core.pojo.Response;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class HttpStatusCodeBusinessException extends BusinessException {
    private int httpStatusCode;

    public HttpStatusCodeBusinessException(int httpStatusCode, Response<Object> response) {
        super(response);
        this.httpStatusCode = httpStatusCode;
    }

    public HttpStatusCodeBusinessException(int httpStatusCode, String message) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public HttpStatusCodeBusinessException(int httpStatusCode, Throwable throwable) {
        super(throwable);
        this.httpStatusCode = httpStatusCode;
    }

    public HttpStatusCodeBusinessException(int httpStatusCode, String message, Throwable throwable) {
        super(message, throwable);
        this.httpStatusCode = httpStatusCode;
    }
}
