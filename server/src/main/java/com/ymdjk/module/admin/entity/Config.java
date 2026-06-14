package com.ymdjk.module.admin.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
@Data @TableName("ym_config")
public class Config {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("`key`")
    private String configKey;
    private String value;
    private String remark;
}
