package com.example.demo.pojo.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "t_permission")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission implements Serializable {
    @Id
    @GeneratedValue
    private Long id;//主键.
    private String name;//名称.
    private String permission; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    private Long parentId; //父编号
    private Boolean available;

}
