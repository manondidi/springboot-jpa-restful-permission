package com.example.demo.service;

import com.example.demo.pojo.dto.Token;
import com.example.demo.pojo.dto.User;

public interface UserService {
    Token login(String user, String password);

    User findUserByToken(String token);
}
