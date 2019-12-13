package com.example.demo.config.shiro;

import com.example.demo.dao.UserRepository;
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
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Data
public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private UserRepository userRepository;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String userIdStr = (String) principalCollection.getPrimaryPrincipal();
        User userPo = userRepository.findById(Long.parseLong(userIdStr)).get();
        for (Role role : userPo.getRoleList()) {
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
        if (userPo == null || !userPo.getPassword().equals(password)) {
            throw new AccountException("用户名或密码错误");
        }
        if (Optional.ofNullable(userPo.getBanTime()).orElse(new Date(0)).after(new Date())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            throw new DisabledAccountException(String.format("该用户因{%s}被封停至{%s}", userPo.getBanReason(), sdf.format(userPo.getBanTime())));
        }
        return new SimpleAuthenticationInfo(userPo.getId().toString(), userPo.getPassword(), getName());
    }

    /**
     * 删除所有人的授权cache 使其再走一次doGetAuthorizationInfo
     */

    public void clearAuthorization() {
        this.getAuthenticationCache().clear();
    }


    /**
     * @param userId 删除某个人的信息 使其需要重新输入用户名密码登陆
     */
    public void clearAuthentication(String userId) {
        this.clearCache(new SimplePrincipalCollection(userId, getName()));
    }

    /**
     * shiro刷新所有人权限
     */
    public static void reloadAuthorizing() {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        MyShiroRealm realm = (MyShiroRealm) rsm.getRealms().iterator().next();
        realm.clearAuthorization();
    }

    /**
     * shiro清除该用户所有token 用于修改密码后过期该用户所有token
     */
    public static void removeUser(String userId) {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        MyShiroRealm realm = (MyShiroRealm) rsm.getRealms().iterator().next();
        realm.clearAuthentication(userId);
    }
}
