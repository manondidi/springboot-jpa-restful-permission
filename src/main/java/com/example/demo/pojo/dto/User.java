package com.example.demo.pojo.dto;

import lombok.Data;

import javax.persistence.*;

@Data
public class User {
    private Long id;
    private String userName;
    private String mail;
    private String tel;
    private String avatar;
}
