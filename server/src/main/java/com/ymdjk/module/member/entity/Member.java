package com.ymdjk.module.member.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("ym_user")
public class Member {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userId;
    private String phone;
    private String password;
    private String sepPassword;
    private String realName;
    private String idCard;
    private String avatar;
    private String openid;
    private Integer userLevel;
    private String upId;
    private String recommendId;
    private LocalDateTime addDate;
    private Integer isAct;
    private LocalDateTime vipTime;
}
