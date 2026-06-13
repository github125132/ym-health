package com.ymdjk.module.admin;

import com.ymdjk.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/login")
    public Result<?> login(@RequestParam String username, @RequestParam String password) {
        return Result.success(adminService.login(username, password));
    }
}
