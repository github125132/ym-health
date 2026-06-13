package com.ymdjk.config;

import com.ymdjk.module.admin.entity.Admin;
import com.ymdjk.module.admin.entity.Role;
import com.ymdjk.module.admin.mapper.AdminMapper;
import com.ymdjk.module.admin.mapper.RoleMapper;
import com.ymdjk.module.content.entity.Ad;
import com.ymdjk.module.content.mapper.AdMapper;
import com.ymdjk.module.product.entity.Category;
import com.ymdjk.module.product.entity.Product;
import com.ymdjk.module.product.mapper.CategoryMapper;
import com.ymdjk.module.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AdminMapper adminMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;
    private final AdMapper adMapper;

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

        if (categoryMapper.selectCount(null) == 0) {
            long[] catIds = new long[4];
            String[] catNames = {"健康吃", "时尚穿", "舒心住", "好易用"};
            for (int i = 0; i < catNames.length; i++) {
                Category c = new Category();
                c.setName(catNames[i]);
                c.setSortOrder(i);
                categoryMapper.insert(c);
                catIds[i] = c.getId();
                log.info("初始化分类: {}", catNames[i]);
            }

            String[][] demoProducts = {
                {"有机红枣礼盒", catIds[0] + "", "59.90", "100"},
                {"五谷杂粮营养餐", catIds[0] + "", "89.00", "200"},
                {"纯棉家居服", catIds[1] + "", "129.00", "50"},
                {"真丝眼罩礼盒", catIds[2] + "", "39.90", "150"},
                {"智能足浴盆", catIds[3] + "", "299.00", "30"},
                {"天然蜂蜜500g", catIds[0] + "", "45.00", "300"},
            };

            for (String[] p : demoProducts) {
                Product product = new Product();
                product.setTitle(p[0]);
                product.setCategoryId(Integer.parseInt(p[1]));
                product.setPurl("/img/default.png");
                product.setBasePrice(new BigDecimal(p[2]));
                product.setStock(Integer.parseInt(p[3]));
                product.setStatus(1);
                product.setSortOrder(0);
                product.setCreatedAt(LocalDateTime.now());
                productMapper.insert(product);
                log.info("初始化商品: {}", p[0]);
            }
        }

        if (adMapper.selectCount(null) == 0) {
            String[][] ads = {
                {"shouye", "大健康产品甄选", "/img/ad_1.png"},
                {"shouye", "中医AI健康检测", "/img/ad_2.png"},
                {"shouye", "成为AI健康管家", "/img/ad_3.png"},
            };
            for (String[] a : ads) {
                Ad ad = new Ad();
                ad.setPosition(a[0]);
                ad.setTitle(a[1]);
                ad.setImageUrl(a[2]);
                ad.setSortOrder(0);
                ad.setStatus(1);
                ad.setCreatedAt(LocalDateTime.now());
                adMapper.insert(ad);
                log.info("初始化广告: {}", a[1]);
            }
        }

        log.info("数据初始化完成");
    }
}
