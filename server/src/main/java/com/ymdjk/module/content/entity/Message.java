package com.ymdjk.module.content.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data @TableName("ym_message")
public class Message {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userId;
    private String title;
    private String content;
    private Integer isRead;
    private LocalDateTime createdAt;
}
