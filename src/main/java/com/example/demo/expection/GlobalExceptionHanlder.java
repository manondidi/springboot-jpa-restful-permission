package com.example.demo.expection;

import com.example.demo.expection.BusinessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHanlder {
    @ExceptionHandler(value = BusinessException.class)
    public BusinessException.ExceptionResult onErrorHandler(BusinessException ex) {
        return ex.getResult();
    }

    @ExceptionHandler(value = Exception.class)
    public BusinessException.ExceptionResult onErrorHandler(Exception ex) {
        return BusinessException.ExceptionResult.builder().code(500).message(ex.getLocalizedMessage()).build();
    }
}
