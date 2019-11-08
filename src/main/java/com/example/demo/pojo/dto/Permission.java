package com.example.demo.pojo.dto;

import com.example.demo.pojo.po.Role;
import com.github.dozermapper.core.DozerBeanMapper;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Resource;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission implements Serializable {
    private long id;//主键.
    private String name;//名称.
    private String permission; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    private Boolean available;
    private List<Permission> children;


    public static List<Permission> convert(List<com.example.demo.pojo.po.Permission> permissionPoList) {
        Mapper dozerMapper = DozerBeanMapperBuilder.buildDefault();
        List<Permission> permissionDtoList = new ArrayList<>();
        for (com.example.demo.pojo.po.Permission po : permissionPoList) {
            if (po.getParentId() == null) {
                com.example.demo.pojo.dto.Permission dto = dozerMapper.map(po, com.example.demo.pojo.dto.Permission.class);
                dto.setChildren(convertChild(po.getId(), permissionPoList));
                permissionDtoList.add(dto);
            }
        }
        return permissionDtoList;
    }

    private static List<Permission> convertChild(Long parentId, List<com.example.demo.pojo.po.Permission> permissionPoList) {
        Mapper dozerMapper = DozerBeanMapperBuilder.buildDefault();
        List<Permission> permissionDtoList = new ArrayList<>();
        for (com.example.demo.pojo.po.Permission po : permissionPoList) {
            if (po.getParentId() == parentId) {
                com.example.demo.pojo.dto.Permission dto = dozerMapper.map(po, com.example.demo.pojo.dto.Permission.class);
                dto.setChildren(convertChild(po.getId(), permissionPoList));
                permissionDtoList.add(dto);
            }
        }
        return permissionDtoList;
    }

}
