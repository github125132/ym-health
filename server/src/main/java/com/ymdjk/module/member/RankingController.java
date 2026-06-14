package com.ymdjk.module.member;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ymdjk.common.PageResult;
import com.ymdjk.common.Result;
import com.ymdjk.module.member.entity.Member;
import com.ymdjk.module.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ranking")
@RequiredArgsConstructor
public class RankingController {
    private final MemberMapper memberMapper;

    @GetMapping
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "20") int pageSize) {
        Page<Member> p = memberMapper.selectPage(new Page<>(page, pageSize),
            new LambdaQueryWrapper<Member>()
                .isNotNull(Member::getPhone)
                .orderByDesc(Member::getUserLevel)
                .orderByAsc(Member::getId));
        PageResult<Member> r = new PageResult<>();
        r.setRecords(p.getRecords());
        r.setTotal(p.getTotal());
        r.setPage(p.getCurrent());
        r.setPageSize(p.getSize());
        return Result.success(r);
    }
}
