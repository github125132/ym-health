package com.ymdjk.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ymdjk.module.admin.entity.Admin;
import com.ymdjk.module.admin.entity.Role;
import com.ymdjk.module.admin.mapper.AdminMapper;
import com.ymdjk.module.admin.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AdminMapper adminMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (roleMapper.selectCount(null) == 0) {
            Role role = new Role();
            role.setName("超级管理员");
            role.setPermissions("products,orders,members,finance,withdraw,content,settings");
            roleMapper.insert(role);
            log.info("初始化角色: 超级管理员");
        }

        if (adminMapper.selectCount(null) == 0) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoleId(1);
            adminMapper.insert(admin);
            log.info("初始化管理员: admin / admin123");
        }

        log.info("数据初始化完成");
    }
}
