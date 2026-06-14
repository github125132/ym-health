package com.ymdjk.module.member.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class ChangePasswordRequest {
    @NotBlank private String oldPassword;
    @NotBlank private String newPassword;
}
