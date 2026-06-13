package com.ymdjk.module.finance.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data @TableName("ym_withdraw")
public class Withdraw {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userId;
    private BigDecimal amount;
    private BigDecimal fee;
    private String bankName;
    private String bankCard;
    private String alipayAccount;
    private Integer status;
    private LocalDateTime auditTime;
    private String remark;
    private LocalDateTime createdAt;
}
