package com.example.demo.expection;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class BusinessException extends RuntimeException {
    private ExceptionResult result;

    private BusinessException(int code, String message) {
        result = ExceptionResult.builder().code(code).message(message).build();
    }

    private BusinessException(int code, String message, Object data) {
        result = ExceptionResult.builder().code(code).message(message).data(data).build();
    }

    public BusinessException(BusinessExceptionEnum exceptionEnum) {
        this(exceptionEnum.code, exceptionEnum.message);
    }

    public BusinessException(BusinessExceptionEnum exceptionEnum, Object data) {
        this(exceptionEnum.code, exceptionEnum.message);
        result.data = data;
    }

    @Data
    @Builder
    public static class ExceptionResult {
        private int code;
        private String message;
        private Object data;
    }
}
