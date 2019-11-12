package com.example.demo.service.impl;

import com.example.demo.config.shiro.MyShiroRealm;
import com.example.demo.dao.PermissionRepository;
import com.example.demo.dao.RoleRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.expection.BusinessException;
import com.example.demo.expection.BusinessExceptionEnum;
import com.example.demo.pojo.dto.PageInfo;
import com.example.demo.pojo.dto.User;
import com.example.demo.pojo.po.Permission;
import com.example.demo.pojo.po.Role;
import com.example.demo.service.UserService;
import com.github.dozermapper.core.Mapper;
import com.google.common.collect.Lists;
import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    public PageInfo<User> getUsers(String search, int pageNum, int pageSize) {
        if (search == null) {
            search = "";
        }
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<com.example.demo.pojo.po.User> page = userRepository
                .findAll(Example.of(com.example.demo.pojo.po.User.builder()
                                .userName(search)
                                .tel(search)
                                .mail(search)
                                .build(),
                        ExampleMatcher.matchingAny()
                                .withMatcher("userName", ExampleMatcher.GenericPropertyMatchers.contains())
                                .withMatcher("tel", ExampleMatcher.GenericPropertyMatchers.contains())
                                .withMatcher("mail", ExampleMatcher.GenericPropertyMatchers.contains())
                ), pageable);
        PageInfo<User> userPageInfo = new PageInfo<User>(page.getTotalElements(), page.getSize(), page.getNumber(),
                page.getContent().stream().map(userPo -> dozerMapper.map(userPo, User.class)).collect(Collectors.toList()));
        return userPageInfo;
    }


    @Override
    @Transactional
    public void deletePermissions(String id) {
        Permission permission = permissionRepository
                .findById(Long.parseLong(id))
                .orElseThrow(() -> new BusinessException(BusinessExceptionEnum.PERMISSION_NOT_FOUND));
        if (permissionRepository.count(Example.of(Permission.builder().parentId(Long.parseLong(id)).build())) > 0) {
            throw new BusinessException(BusinessExceptionEnum.PERMISSION_DELETE_HAS_CHIRLDREN);
        }
        List<Role> roleList = roleRepository
                .findAll(Example.of(Role.builder()
                        .permissions(Lists.newArrayList(permission))
                        .build()));
        roleList.stream().forEach((role) ->
                role.getPermissions()
                        .removeIf(permission1 -> permission1.getId().equals(permission.getId())));
        roleRepository.saveAll(roleList);
        permissionRepository.delete(permission);
        MyShiroRealm.reloadAuthorizing();
    }


    @Override
    public List<com.example.demo.pojo.dto.Permission> getAllPermissionsTree() {
        List<Permission> permissionPoList = permissionRepository.findAll();
        return com.example.demo.pojo.dto.Permission.convert(permissionPoList);
    }


    @Override
    public com.example.demo.pojo.dto.Permission addPermission(String name, String permission, @Nullable String parentId) {
        Permission permissionPo = Permission.builder()
                .name(name)
                .permission(permission)
                .build();
        if (parentId != null) {
            Permission parent = permissionRepository.findById(Long.parseLong(parentId))
                    .orElseThrow(() -> new BusinessException(BusinessExceptionEnum.PERMISSION_PARENT_NOT_FOUND));
            permissionPo.setParentId(Long.parseLong(parentId));
        }
        permissionRepository.saveAndFlush(permissionPo);
        com.example.demo.pojo.dto.Permission permissionDto = dozerMapper.map(permissionPo, com.example.demo.pojo.dto.Permission.class);
        permissionDto.setChildren(new ArrayList<>());
        return permissionDto;
    }

    @Override
    public Role addRole(String name, String description, List<String> permissionIds) {
        List<Permission> permissionList = permissionRepository
                .findAllById(permissionIds.stream().map(Long::parseLong).collect(Collectors.toList()));
        Role role = Role.builder().name(name).description(description).available(true).permissions(permissionList).build();
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
                                role1.getId().equals(role.getId())));
        userRepository.saveAll(userList);
        roleRepository.deleteById(role.getId());
        MyShiroRealm.reloadAuthorizing();
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
                .available(available)
                .description(description)
                .name(name)
                .permissions(permissionList)
                .build();
        roleRepository.save(newRole);
        MyShiroRealm.reloadAuthorizing();
        return newRole;
    }

    @Override
    public User editUserRole(String userId, List<String> roleIds) {
        com.example.demo.pojo.po.User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new BusinessException(BusinessExceptionEnum.USER_NOT_FOUND));
        List<Role> roles = roleRepository.findAllById(roleIds.stream().map(Long::parseLong).collect(Collectors.toList()));
        user.setRoleList(roles);
        userRepository.saveAndFlush(user);
        MyShiroRealm.reloadAuthorizing();
        return dozerMapper.map(user, User.class);
    }

    @Override
    public User getUser(String id) {
        com.example.demo.pojo.po.User userPo = userRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new BusinessException(BusinessExceptionEnum.USER_NOT_FOUND));

        return dozerMapper.map(userPo, User.class);
    }


}
