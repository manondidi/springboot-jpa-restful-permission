package com.example.demo.expection;

import com.example.demo.expection.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHanlder {
    @ExceptionHandler(value = BusinessException.class)
    public BusinessException.ExceptionResult onErrorHandler(BusinessException ex) {
        ex.printStackTrace();
        log.error(ex.getMessage());
        return ex.getResult();
    }

    @ExceptionHandler(value = Exception.class)
    public BusinessException.ExceptionResult onErrorHandler(Exception ex) {
        ex.printStackTrace();
        log.error(ex.getMessage());
        if (ex instanceof UnauthenticatedException) {
            return BusinessException.ExceptionResult.builder().code(401).message("未登录").build();
        }
        if (ex instanceof UnauthorizedException) {
            return BusinessException.ExceptionResult.builder().code(403).message("用户无权限").build();
        }
        return BusinessException.ExceptionResult.builder().code(500).message(ex.getLocalizedMessage()).build();
    }
}
