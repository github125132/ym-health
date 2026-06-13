package com.ymdjk.module.content.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data @TableName("ym_content")
public class Content {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private Integer categoryId;
    private String author;
    private String image;
    private String summary;
    private String body;
    private Integer views;
    private Integer status;
    private LocalDateTime createdAt;
}
