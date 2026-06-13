package com.ymdjk.module.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("ym_product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private Integer categoryId;
    private String purl;
    private String images;
    private String memo;
    private String brief;
    private BigDecimal basePrice;
    private BigDecimal points;
    private Integer stock;
    private Integer salesCount;
    private Integer status;
    private Integer isRecommend;
    private Integer isTrial;
    private Integer sortOrder;
    private Integer views;
    private String specs;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
