package com.example.demo.pojo.dto;

import lombok.Data;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.Random;

@Data
public class Token {
    private String token;

    public static Token generateToken() {
        Token token = new Token();
        Random random = new Random();
        String str = new Date().getTime() + "_" + random.doubles();
        token.token = DigestUtils.md5DigestAsHex(str.getBytes());
        return token;
    }
}
