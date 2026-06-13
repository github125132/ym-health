package com.ymdjk.module.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ShipRequest {
    @NotBlank private String expressCompany;
    @NotBlank private String expressNo;
}
