package com.ymdjk.module.finance;

import com.ymdjk.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/finance")
@RequiredArgsConstructor
public class RebateController {
    private final RebateService rebateService;

    @GetMapping("/rebate")
    public Result<?> rebateList(Authentication auth) {
        return Result.success(rebateService.listByUser(auth.getName()));
    }
}
