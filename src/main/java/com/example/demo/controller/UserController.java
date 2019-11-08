package com.example.demo.controller;


import com.example.demo.pojo.dto.Token;
import com.example.demo.pojo.dto.User;
import com.example.demo.pojo.po.Permission;
import com.example.demo.pojo.po.Role;
import com.example.demo.pojo.vo.Result;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController

public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public Result<Token> login(@RequestParam String user, @RequestParam String password) {
        UsernamePasswordToken token = new UsernamePasswordToken(user, password);
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.login(token);
        return Result.success(new Token(currentUser.getSession().getId().toString()));
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        SecurityUtils.getSubject().logout();
        return Result.success();
    }

    @GetMapping("/unauth")
    public void unLogin() {
        throw new UnauthenticatedException();
    }


    @GetMapping("/users")
    @RequiresAuthentication
    public Result<List<User>> getUserList() {
        return Result.success(userService.getAllUsers());
    }


    @PostMapping("/roles/")
    public Result<Role> addRole(@RequestParam String name, @RequestParam String desc) {
        return Result.success(userService.addRole(name, desc));
    }

    @DeleteMapping("/roles/{id}")
    public Result<Void> deleteRole(@PathVariable String id) {
        userService.deleteRole(id);
        return Result.success();
    }

}
