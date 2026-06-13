package com.ymdjk.module.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank private String phone;
    @NotBlank private String password;
    private String realName;
    private String recommendId;
}
