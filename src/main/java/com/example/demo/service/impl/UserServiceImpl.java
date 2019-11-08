package com.example.demo.service.impl;

import com.example.demo.dao.PermissionRepository;
import com.example.demo.dao.RoleRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.expection.BusinessException;
import com.example.demo.expection.BusinessExceptionEnum;
import com.example.demo.pojo.dto.User;
import com.example.demo.pojo.po.Permission;
import com.example.demo.pojo.po.Role;
import com.example.demo.service.UserService;
import com.github.dozermapper.core.Mapper;
import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.Column;
import java.util.*;
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

    @Override
    @Transactional
    public void deletePermission(String id) {

        Permission permission = permissionRepository
                .findById(Long.parseLong(id))
                .orElseThrow(() ->
                        new BusinessException(BusinessExceptionEnum.PERMISSION_NOT_FOUND));
        List<Permission> permissionList = new ArrayList<>();
        permissionList.add(permission);
        List<Role> roleList = roleRepository
                .findAll(Example.of(Role.builder()
                        .permissions(permissionList)
                        .build()));
        roleList.stream().forEach((role) ->
                role.getPermissions()
                        .removeIf(permission1 ->
                                permission.getId() == permission1.getId()));
        roleRepository.saveAll(roleList);
        permissionRepository.deleteById(permission.getId());
    }

    public List<com.example.demo.pojo.dto.Permission> getPermissions(List<String> ids) {
        List<Long> idList = ids.stream().map((id) -> Long.parseLong(id)).collect(Collectors.toList());
        List<Permission> permissionPoList = permissionRepository.getPermissionsById(idList);
        return com.example.demo.pojo.dto.Permission.convert(permissionPoList);
    }


    @Override
    public com.example.demo.pojo.dto.Permission addPermission(String name, String resourceType, String permission, @Nullable String parentId) {
        if (parentId != null) {
            Permission parent = permissionRepository.findById(Long.parseLong(parentId))
                    .orElseThrow(() -> new BusinessException(BusinessExceptionEnum.PERMISSION_PARENT_NOT_FOUND));
        }
        Permission permissionPo = Permission.builder()
                .name(name)
                .permission(permission)
                .parentId(Long.parseLong(parentId))
                .build();
        permissionRepository.saveAndFlush(permissionPo);
        com.example.demo.pojo.dto.Permission permissionDto = dozerMapper.map(permissionPo, com.example.demo.pojo.dto.Permission.class);
        permissionDto.setChildren(new ArrayList<>());
        return permissionDto;
    }

    @Override
    public Role addRole(String name, String description) {
        Role role = Role.builder().name(name).description(description).available(true).build();
        roleRepository.saveAndFlush(role);
        return role;
    }

    @Override
    @Transactional
    public void deleteRole(String id) {
        Role role = roleRepository
                .findById(Long.parseLong(id))
                .orElseThrow(() -> new BusinessException(BusinessExceptionEnum.ROLE_NOT_FOUND));
        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        List<com.example.demo.pojo.po.User> userList = userRepository
                .findAll(Example.of(com.example.demo.pojo.po.User.builder().roleList(roleList).build()));
        userList.stream().forEach((user) ->
                user.getRoleList()
                        .removeIf(role1 ->
                                role1.getId() == role.getId()));
        userRepository.saveAll(userList);
        roleRepository.deleteById(role.getId());
    }

    @Override
    public Role editRole(com.example.demo.pojo.dto.Role role, List<String> permissionIds) {
        Role oldRole = roleRepository.findById(role.getId())
                .orElseThrow(() ->
                        new BusinessException(BusinessExceptionEnum.ROLE_NOT_FOUND));
        List<Permission> permissionList = permissionRepository
                .findAllById(permissionIds.stream().map(Long::parseLong).collect(Collectors.toList()));
        Role newRole = Role.builder()
                .id(role.getId())
                .userList(oldRole.getUserList())//不可编辑
                .available(role.getAvailable())
                .description(role.getDescription())
                .name(role.getName())
                .permissions(permissionList)
                .build();
        roleRepository.save(newRole);
        return newRole;
    }


}
