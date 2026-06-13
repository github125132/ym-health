package com.ymdjk.module.content;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ymdjk.common.Result;
import com.ymdjk.module.content.entity.Ad;
import com.ymdjk.module.content.mapper.AdMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ads")
@RequiredArgsConstructor
public class AdController {
    private final AdMapper adMapper;

    @GetMapping
    public Result<List<Ad>> list(@RequestParam(required = false) String position) {
        LambdaQueryWrapper<Ad> q = new LambdaQueryWrapper<Ad>()
                .eq(Ad::getStatus, 1)
                .eq(position != null, Ad::getPosition, position)
                .orderByAsc(Ad::getSortOrder);
        return Result.success(adMapper.selectList(q));
    }
}
