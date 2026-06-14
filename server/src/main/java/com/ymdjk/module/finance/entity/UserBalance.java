package com.ymdjk.module.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("ym_user_balance")
public class UserBalance {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userId;
    private BigDecimal balance;
    private BigDecimal frozenBalance;
    private BigDecimal points;
}
