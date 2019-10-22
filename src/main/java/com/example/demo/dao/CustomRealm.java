package com.example.demo.dao;

import com.example.demo.pojo.po.Permission;
import com.example.demo.pojo.po.Role;
import com.example.demo.pojo.po.User;
import com.example.demo.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Data
public class CustomRealm extends AuthorizingRealm {
    @Autowired
    private UserRepository userRepository;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principalCollection.getPrimaryPrincipal();
        for (Role role : user.getRoleList()) {
            authorizationInfo.addRole(role.getName());
            for (Permission p : role.getPermissions()) {
                authorizationInfo.addStringPermission(p.getPermission());
            }
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String user = (String) authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());
        User userPo = userRepository.findUserByUserNameOrMailOrTel(user, user, user);
        log.info("doGetAuthenticationInfo =======" + user);
        if (userPo == null || !userPo.getPassword().equals(password)) {
            throw new AccountException("用户名或密码错误");
        }
        return new SimpleAuthenticationInfo(userPo, userPo.getPassword(), getName());
    }
}
