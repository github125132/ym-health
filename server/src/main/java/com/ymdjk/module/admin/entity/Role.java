package com.ymdjk.module.admin.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
@Data @TableName("ym_role")
public class Role {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String permissions;
}
