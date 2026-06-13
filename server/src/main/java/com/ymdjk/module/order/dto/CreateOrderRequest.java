package com.ymdjk.module.order.dto;
import lombok.Data;
@Data
public class CreateOrderRequest {
    private String receiverName;
    private String receiverPhone;
    private String receiverAddr;
    private String receiverZip;
    private String remark;
}
