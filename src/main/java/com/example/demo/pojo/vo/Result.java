package com.example.demo.pojo.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class Result<T> {
    private int code;
    private String message;
    private T data;


    public static <T> Result success(T data) {
        Result<T> result = new Result<>(0, "success", data);
        return result;
    }

    public static Result success() {
        Result result = new Result(0, "success", null);
        return result;
    }
}
