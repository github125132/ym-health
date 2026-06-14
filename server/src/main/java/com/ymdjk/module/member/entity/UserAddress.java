package com.ymdjk.module.member.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
@Data @TableName("ym_user_address")
public class UserAddress {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userId;
    private String receiver;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String address;
    private String zipCode;
    private Integer isDefault;
}
