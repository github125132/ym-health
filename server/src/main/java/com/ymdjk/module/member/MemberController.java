package com.ymdjk.module.member;

import com.ymdjk.common.Result;
import com.ymdjk.module.member.dto.LoginRequest;
import com.ymdjk.module.member.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody LoginRequest req) {
        return Result.success(memberService.login(req.getPhone(), req.getPassword()));
    }

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest req) {
        memberService.register(req.getPhone(), req.getPassword(), req.getRealName(), req.getRecommendId());
        return Result.success();
    }
}
