package com.ymdjk.module.order.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class CreateOrderRequest {
    private String receiverName;
    private String receiverPhone;
    @NotBlank(message = "收货地址不能为空")
    private String receiverAddr;
    private String receiverZip;
    private String remark;
}
