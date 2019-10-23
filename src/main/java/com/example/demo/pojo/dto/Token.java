package com.example.demo.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.Random;

@Data
@AllArgsConstructor
public class Token {
    private String token;
}
