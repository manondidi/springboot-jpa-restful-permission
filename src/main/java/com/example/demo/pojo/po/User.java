package com.example.demo.pojo.po;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, length = 32, unique = true)
    private String userName;
    @Column(nullable = false, length = 32)
    private String password;
    @Column(nullable = false, length = 32, unique = true)
    private String mail;
    @Column(nullable = false, length = 32, unique = true)
    private String tel;
    @Column()
    private String avatar;
}
