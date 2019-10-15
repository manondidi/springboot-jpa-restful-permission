package com.example.demo.controller;


import com.example.demo.pojo.dto.Token;
import com.example.demo.pojo.dto.User;
import com.example.demo.pojo.vo.Result;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController

public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/user/login")
    public Result<Token> login(@RequestParam String user, @RequestParam String password) {
        Token token = userService.login(user, password);
        return Result.success(token);
    }

    @PostMapping("/user/unLogin")
    public Result<Void> unLogin(@RequestParam String userId, @RequestParam String token) {
        userService.unLogin(userId, token);
        return Result.success();
    }

    @GetMapping("/users/")
    public Result<User> findUserByToken(@RequestParam String token) {
        User userDto = userService.findUserByToken(token);
        return Result.success(userDto);

    }


}
