package com.ymdjk.module.member;

import com.ymdjk.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberProfileController {
    private final MemberService memberService;

    @GetMapping("/profile")
    public Result<?> profile(Authentication auth) {
        return Result.success(memberService.findByUserId(auth.getName()));
    }
}
