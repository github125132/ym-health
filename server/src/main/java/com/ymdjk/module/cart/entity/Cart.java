package com.ymdjk.module.cart.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("ym_cart")
public class Cart {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userId;
    private Integer productId;
    private Integer quantity;
    private String spec;
    private LocalDateTime createdAt;
}
