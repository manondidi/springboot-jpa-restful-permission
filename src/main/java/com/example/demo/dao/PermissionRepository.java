package com.example.demo.dao;

import com.example.demo.pojo.po.Permission;
import com.example.demo.pojo.po.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query("select distinct p1 from com.example.demo.pojo.po.Permission p1 left join  com.example.demo.pojo.po.Permission p2 on p1.id=p2.parentId where p1.id in (:ids)")
    List<Permission> getPermissionsById(@Param("ids") List<Long> ids);

}
