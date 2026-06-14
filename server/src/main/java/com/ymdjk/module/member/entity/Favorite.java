package com.ymdjk.module.member.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data @TableName("ym_favorite")
public class Favorite {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userId;
    private Integer targetType;
    private Integer targetId;
    private LocalDateTime createdAt;
}
