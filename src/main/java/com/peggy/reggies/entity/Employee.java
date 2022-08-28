package com.peggy.reggies.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;

    //表示插入时填充字段
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime; //创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime; //修改时间

    @TableField(fill = FieldFill.INSERT)
    private Long createUser; //创建者

    //表示插入和更新时填充字段
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser; //修改者

}
