package com.example.demo.dao;

import com.example.demo.pojo.po.Role;
import com.example.demo.pojo.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
