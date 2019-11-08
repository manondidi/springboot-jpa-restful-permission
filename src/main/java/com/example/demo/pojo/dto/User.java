package com.example.demo.pojo.dto;

import com.example.demo.pojo.po.Permission;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
public class User {
    private Long id;
    private String userName;
    private String mail;
    private String tel;
    private String avatar;

    private List<com.example.demo.pojo.po.Permission> permissions;
    private List<com.example.demo.pojo.dto.Role> roles;
}
