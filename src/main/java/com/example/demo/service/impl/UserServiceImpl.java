package com.example.demo.service.impl;

import com.example.demo.dao.PermissionRepository;
import com.example.demo.dao.RoleRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.expection.BusinessException;
import com.example.demo.expection.BusinessExceptionEnum;
import com.example.demo.pojo.dto.Token;
import com.example.demo.pojo.dto.User;
import com.example.demo.pojo.po.Permission;
import com.example.demo.pojo.po.Role;
import com.example.demo.service.UserService;
import com.github.dozermapper.core.Mapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Resource
    private Mapper dozerMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public List<User> getAllUsers() {
        List<com.example.demo.pojo.po.User> users = userRepository.findAll();
        return Optional
                .ofNullable(users)
                .orElse(new ArrayList<>())
                .stream()
                .map(userPo -> dozerMapper.map(userPo, User.class))
                .collect(Collectors.toList());
    }

    public void addPermission(Permission permission) {
        permissionRepository.save(permission);
    }

    public void addRole(Role role) {
        roleRepository.save(role);
    }

    public void editRole(Role role) {
//        Role oldRole = roleRepository
//                .findOne(Example.of(Role.builder().id(role.getId()).build()))
//                .orElseThrow(() ->
//                        new BusinessException(BusinessExceptionEnum.ROLE_NOT_FOUND)
//                );
//        oldRole.setAvailable(role.getAvailable());
//        oldRole.setDescription(role.getDescription());

    }


}
