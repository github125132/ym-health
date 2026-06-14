package com.ymdjk.module.content;

import com.ymdjk.common.Result;
import com.ymdjk.module.content.entity.Message;
import com.ymdjk.module.content.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageMapper messageMapper;

    @PostMapping
    public Result<Void> submit(@RequestBody Message message) {
        message.setIsRead(0);
        messageMapper.insert(message);
        return Result.success();
    }
}
