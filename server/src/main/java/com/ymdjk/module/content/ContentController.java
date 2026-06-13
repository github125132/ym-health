package com.ymdjk.module.content;
import com.ymdjk.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/contents")
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;
    @GetMapping
    public Result<?> list() { return Result.success(contentService.list()); }
}
