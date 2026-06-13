package com.ymdjk.module.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("ym_order_item")
public class OrderItem {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private String productName;
    private String productImage;
    private String spec;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}
