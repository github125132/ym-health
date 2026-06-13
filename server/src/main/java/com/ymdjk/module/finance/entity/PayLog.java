package com.ymdjk.module.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("ym_pay_log")
public class PayLog {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userId;
    private String orderNo;
    private BigDecimal amount;
    private Integer payType;
    private String description;
    private Integer status;
    private LocalDateTime createdAt;
}
