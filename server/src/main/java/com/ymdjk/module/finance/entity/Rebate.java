package com.ymdjk.module.finance.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@TableName("ym_rebate")
public class Rebate {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String orderNo;
    private String fromUser;
    private String toUser;
    private BigDecimal amount;
    private Integer level;
    private LocalDateTime createdAt;
}
