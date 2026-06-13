package com.ymdjk.module.content.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data @TableName("ym_ad")
public class Ad {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String position;
    private String title;
    private String imageUrl;
    private String linkUrl;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createdAt;
}
