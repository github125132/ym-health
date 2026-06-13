package com.ymdjk.module.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ymdjk.common.JwtUtil;
import com.ymdjk.module.admin.entity.Admin;
import com.ymdjk.module.admin.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Map<String, String> login(String username, String password) {
        Admin admin = adminMapper.selectOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getUsername, username));
        if (admin == null || !passwordEncoder.matches(password, admin.getPassword()))
            throw new IllegalArgumentException("用户名或密码错误");
        String token = jwtUtil.generateAccessToken(String.valueOf(admin.getId()), "ADMIN");
        return Map.of("accessToken", token);
    }
}
