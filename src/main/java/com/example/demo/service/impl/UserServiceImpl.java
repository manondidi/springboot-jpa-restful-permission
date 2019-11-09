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
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        List<com.example.demo.pojo.po.User> users = userRepository.findAll(sort);
        return Optional
                .ofNullable(users)
                .orElse(new ArrayList<>())
                .stream()
                .map(userPo -> dozerMapper.map(userPo, User.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePermissions(List<String> ids) {
        List<Permission> permissions = permissionRepository
                .findAllById(ids.stream().map(id -> Long.parseLong(id)).collect(Collectors.toList()));
        if (permissions.isEmpty()) {
            throw new BusinessException(BusinessExceptionEnum.PERMISSION_NOT_FOUND);
        }
        List<Long> pids = permissions.stream().map(Permission::getId).collect(Collectors.toList());
        //找到permission的孩子结点
        List<Permission> children = permissionRepository.getPermissionsByParentId(pids);
        List<Permission> toDeletePermissionList = new ArrayList<>();
        toDeletePermissionList.addAll(permissions);
        toDeletePermissionList.addAll(children);
        Set<Long> toDeleteSet = toDeletePermissionList.stream().map(Permission::getId).collect(Collectors.toSet());
        List<Role> roleList = roleRepository
                .findAll(Example.of(Role.builder()
                        .permissions(toDeletePermissionList)
                        .build()));
        roleList.stream().forEach((role) ->
                role.getPermissions()
                        .removeIf(permission1 -> toDeleteSet.contains(permission1.getId())));
        roleRepository.saveAll(roleList);
        permissionRepository.deleteAll(toDeletePermissionList);
    }

    @Override
    public List<com.example.demo.pojo.dto.Permission> getAllPermissionsTree() {
        List<Permission> permissionPoList = permissionRepository.findAll();
        return com.example.demo.pojo.dto.Permission.convert(permissionPoList);
    }


    @Override
    public com.example.demo.pojo.dto.Permission addPermission(String name, String permission, @Nullable String parentId) {
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
    public List<com.example.demo.pojo.dto.Role> getRoles() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        List<Role> roles = roleRepository.findAll(sort);
        return roles.stream().map((rolePo) ->
                com.example.demo.pojo.dto.Role.builder()
                        .available(rolePo.getAvailable())
                        .description(rolePo.getDescription())
                        .id(rolePo.getId())
                        .name(rolePo.getName())
                        .permissions(com.example.demo.pojo.dto.Permission.convert(rolePo.getPermissions()))
                        .build()
        ).collect(Collectors.toList());
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
    public Role editRole(String id, String name, String description, Boolean available, List<String> permissionIds) {
        Role oldRole = roleRepository.findById(Long.parseLong(id))
                .orElseThrow(() ->
                        new BusinessException(BusinessExceptionEnum.ROLE_NOT_FOUND));
        if (permissionIds == null) {
            permissionIds = new ArrayList<>();
        }
        List<Permission> permissionList = permissionRepository
                .findAllById(permissionIds.stream().map(Long::parseLong).collect(Collectors.toList()));
        Role newRole = Role.builder()
                .id(Long.parseLong(id))
                .userList(oldRole.getUserList())//不可编辑
                .available(available)
                .description(description)
                .name(name)
                .permissions(permissionList)
                .build();
        roleRepository.save(newRole);
        return newRole;
    }


}
