package com.github.charlesvhe.core.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.charlesvhe.core.pojo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Slf4j
@ControllerAdvice("com.github.charlesvhe")
public class GlobalResponseEntityExceptionHandler {
    @Value("${core.exception.printMessage:false}")
    private boolean printMessage;
    @Value("${core.exception.printStackTrace:false}")
    private boolean printStackTrace;
    @Value("${core.exception.printValidation:true}")
    private boolean printValidation;

    @Autowired
    private ObjectMapper objectMapper;

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity<Response> handleControllerException(Exception ex, WebRequest request) {
        log.error("GlobalResponseEntityExceptionHandler", ex);

        ResponseEntity<Response> result;
        if (ex instanceof HttpStatusCodeBusinessException) {
            HttpStatusCodeBusinessException e = (HttpStatusCodeBusinessException) ex;
            result = new ResponseEntity<>(e.getResponse(), HttpStatus.resolve(e.getHttpStatusCode()));
        } else if (ex instanceof BusinessException) {
            BusinessException e = (BusinessException) ex;
            result = new ResponseEntity<>(e.getResponse(), HttpStatus.BAD_REQUEST);
        } else if (ex instanceof FatalException) {
            // TODO 发送告警
            FatalException e = (FatalException) ex;
            result = new ResponseEntity<>(e.getResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (ex instanceof HttpStatusCodeException) { // rest client 异常
            HttpStatusCodeException e = (HttpStatusCodeException) ex;
            // 调用链异常上抛 A->B->C  这里可以抛出C的异常
            String body = e.getResponseBodyAsString();
            if (StringUtils.hasText(body)) {
                try {
                    Response responseBody = objectMapper.readValue(body, Response.class);
                    result = new ResponseEntity<>(responseBody, e.getStatusCode());
                } catch (JsonProcessingException jsonProcessingException) {
                    result = new ResponseEntity<>(new Response(e.getStatusCode().value(), e.getStatusCode().getReasonPhrase(), body), e.getStatusCode());
                }
            } else {
                result = new ResponseEntity<>(new Response(e.getStatusCode().value(), e.getStatusCode().getReasonPhrase(), body), e.getStatusCode());
            }
        } else if (ex instanceof MethodArgumentNotValidException) { // bean valid 异常
            MethodArgumentNotValidException e = (MethodArgumentNotValidException) ex;
            result = new ResponseEntity<>(new Response(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null), HttpStatus.BAD_REQUEST);
            transferBindingResult(result.getBody(), e.getBindingResult());
        } else if (ex instanceof BindException) { // bean valid 异常
            BindException e = (BindException) ex;
            result = new ResponseEntity<>(new Response(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null), HttpStatus.BAD_REQUEST);
            transferBindingResult(result.getBody(), e.getBindingResult());
        } else if (ex instanceof ConstraintViolationException) { // bean valid 异常
            ConstraintViolationException e = (ConstraintViolationException) ex;
            result = new ResponseEntity<>(new Response(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null), HttpStatus.BAD_REQUEST);
            transferConstraintViolations(result.getBody(), e.getConstraintViolations());

        } else {
            result = new ResponseEntity<>(new Response(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (!printMessage) {
            result.getBody().setMessage("系统开小差了 请稍后再试");
        } else {
            transferStackTrace(result.getBody(), ex);
        }
        return result;
    }

    public void transferBindingResult(Response<Object> response, BindingResult bindingResult) {
        if (printValidation) {
            Map<String, String> message = new HashMap<>();
            bindingResult.getAllErrors().stream().forEach(oe -> message.put(((FieldError) oe).getField(), oe.getDefaultMessage()));
            try {
                response.setMessage(objectMapper.writeValueAsString(message));
            } catch (JsonProcessingException e) {
                log.error("transferBindingResult失败", e);
            }
        }
    }

    public void transferConstraintViolations(Response<Object> response, Set<ConstraintViolation<?>> constraintViolationSet) {
        if (printValidation) {
            Map<String, String> message = new HashMap<>();
            constraintViolationSet.stream().forEach(cv -> {
                Iterator<Path.Node> it = cv.getPropertyPath().iterator();
                Path.Node last = it.next();
                while (it.hasNext()) {
                    last = it.next();
                }
                message.put(last.getName(), cv.getMessage());
            });
            try {
                response.setMessage(objectMapper.writeValueAsString(message));
            } catch (JsonProcessingException e) {
                log.error("transferBindingResult失败", e);
            }
        }
    }

    public void transferStackTrace(Response<Object> response, Exception e) {
        if (printStackTrace && response.getData() == null) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            response.setData(sw.toString());
        }
    }

}
