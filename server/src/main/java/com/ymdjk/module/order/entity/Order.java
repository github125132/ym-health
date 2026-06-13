package com.ymdjk.module.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("ym_order")
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String orderNo;
    private String userId;
    private Integer orderType;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private Integer payType;
    private Integer orderStatus;
    private Integer payStatus;
    private Integer deliveryStatus;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddr;
    private String expressCompany;
    private String expressNo;
    private String remark;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
}
