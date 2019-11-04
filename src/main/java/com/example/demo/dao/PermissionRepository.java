package com.example.demo.dao;

import com.example.demo.pojo.po.Permission;
import com.example.demo.pojo.po.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface PermissionRepository extends CrudRepository<Permission, Long> {

}
