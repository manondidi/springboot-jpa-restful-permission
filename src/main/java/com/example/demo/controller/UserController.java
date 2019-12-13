package com.example.demo.controller;


import com.example.demo.config.shiro.MyShiroRealm;
import com.example.demo.expection.BusinessException;
import com.example.demo.expection.BusinessExceptionEnum;
import com.example.demo.pojo.dto.PageInfo;
import com.example.demo.pojo.dto.Permission;
import com.example.demo.pojo.dto.Token;
import com.example.demo.pojo.dto.User;
import com.example.demo.pojo.po.Role;
import com.example.demo.pojo.vo.Result;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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

    @PutMapping("/users/{userId}/roles")
    @RequiresPermissions("user:role:update")
    public Result<User> editUserRole(@PathVariable String userId, @RequestParam List<String> roleIds) {
        return Result.success(userService.editUserRole(userId, roleIds));
    }


    @GetMapping("/users/{id}")
    public Result<User> getUser(@PathVariable String id) {
        String currentUserId = (String) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
        if (currentUserId.equals(id)) {
            return getCurrentUser();
        } else if (SecurityUtils.getSubject().isPermitted("user:view")) {
            return Result.success(userService.getUser(id));
        } else {
            throw new BusinessException(BusinessExceptionEnum.COMMON_HAS_NO_PERMISSION);
        }

    }

    @GetMapping("/currentUser")
    @RequiresAuthentication
    public Result<User> getCurrentUser() {
        String currentUserId = (String) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
        return Result.success(userService.getUser(currentUserId));
    }

    @PostMapping("/logout")
    @RequiresAuthentication
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.success();
    }

    @GetMapping("/unauth")
    public void unLogin() {
        throw new UnauthenticatedException();
    }


    @GetMapping("/users")
    @RequiresPermissions("user:view")
    public Result<PageInfo<User>> getUserList(@RequestParam(required = false) String search, @RequestParam int pageNum, @RequestParam int pageSize) {
        return Result.success(userService.getUsers(search, pageNum, pageSize));
    }


    @PostMapping("/roles")
    @RequiresPermissions("role:add")
    public Result<Role> addRole(@RequestParam String name, @RequestParam String desc, @RequestParam String[] permissionIds) {
        return Result.success(userService.addRole(name, desc, Arrays.asList(permissionIds)));
    }


    @DeleteMapping("/roles/{id}")
    @RequiresPermissions("role:delete")
    public Result deleteRole(@PathVariable String id) {
        userService.deleteRole(id);
        return Result.success();
    }

    @GetMapping("/roles")
    @RequiresPermissions("role:view")
    public Result<List<com.example.demo.pojo.dto.Role>> getRoles() {
        return Result.success(userService.getRoles());

    }

    @PutMapping("/roles/{id}")
    @RequiresPermissions("role:update")
    public Result<Role> editRole(@PathVariable String id,
                                 @RequestParam String name,
                                 @RequestParam String desc,
                                 @RequestParam Boolean available,
                                 @RequestParam String[] permissionIds) {

        return Result.success(userService.editRole(id, name, desc, available, Arrays.asList(permissionIds)));
    }

    @GetMapping("/permissions")
    @RequiresPermissions("permission:view")
    public Result<List<Permission>> getAllPermisstionsTree() {
        return Result.success(userService.getAllPermissionsTree());
    }

    @PostMapping("/permissions")
    @RequiresPermissions("permission:add")
    public Result<Permission> addPermisstion(@RequestParam String name,
                                             @RequestParam String permission,
                                             @RequestParam(required = false) String parentId) {

        return Result.success(userService.addPermission(name, permission, parentId));
    }

    @DeleteMapping("/permissions/{id}")
    @RequiresPermissions("permission:delete")
    public Result deletePermisstions(@PathVariable String id) {
        userService.deletePermissions(id);
        return Result.success();
    }

    @PutMapping("/users/{userId}/ban")
    @RequiresPermissions("user:ban")
    public Result<User> banUser(@PathVariable String userId, @RequestParam long banDate, @RequestParam String banReason) {
        String currentUserId = (String) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
        if (currentUserId.equals(userId)) {
            throw new BusinessException(BusinessExceptionEnum.USER_CAN_NOT_BAN_SELF);
        }
        User user = userService.ban(userId, banReason, banDate);
        MyShiroRealm.removeUser(currentUserId);
        return Result.success(user);
    }

    @PutMapping("/users/{userId}/unban")
    @RequiresPermissions("user:unban")
    public Result<User> unbanUser(@PathVariable String userId) {
        String currentUserId = (String) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
        if (currentUserId.equals(userId)) {
            throw new BusinessException(BusinessExceptionEnum.USER_CAN_NOT_UNBAN_SELF);
        }
        return Result.success(userService.unban(userId));
    }

    @PutMapping("/users/password")
    @RequiresAuthentication
    public Result changePassword( @RequestParam String oldPassword,  @RequestParam String newPassword) {
        String currentUserId = (String) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
        userService.changePassword(currentUserId, oldPassword, newPassword);
        MyShiroRealm.removeUser(currentUserId);
        return Result.success();
    }
}
