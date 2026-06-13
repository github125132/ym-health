package com.ymdjk.module.admin.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
@Data @TableName("ym_admin")
public class Admin {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private Integer roleId;
}
