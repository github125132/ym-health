package com.ymdjk.module.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("ym_review")
public class Review {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userId;
    private Integer productId;
    private String orderNo;
    private Integer rating;
    private String content;
    private String images;
    private LocalDateTime createdAt;
}
