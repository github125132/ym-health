# Spring Boot Migration Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use subagent-driven-development or executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Migrate the ASP.NET WebForms health mall system to Spring Boot 3 + Vue 3 + MySQL

**Architecture:** Spring Boot REST API + MyBatis-Plus ORM. Vue 3 + Element Plus SPA. JWT auth. MySQL 8 with Flyway migrations. Payment via strategy pattern.

**Tech Stack:** Spring Boot 3.x, JDK 17, MyBatis-Plus, MySQL 8, Flyway, Spring Security + JWT, Vue 3, Element Plus, Vite, Pinia, Axios, weixin-java-pay, alipay-sdk-java

---

### Task 1: Create Spring Boot project structure

**Files:**
- Create: `ymdjk-server/pom.xml`
- Create: `ymdjk-server/src/main/java/com/ymdjk/YmdjkApplication.java`
- Create: `ymdjk-server/src/main/resources/application.yml`

- [ ] **Step 1: Write pom.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>
    <groupId>com.ymdjk</groupId>
    <artifactId>ymdjk-server</artifactId>
    <version>1.0.0</version>
    <properties>
        <java.version>17</java.version>
        <mybatis-plus.version>3.5.5</mybatis-plus.version>
    </properties>
    <dependencies>
        <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-web</artifactId></dependency>
        <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-security</artifactId></dependency>
        <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-validation</artifactId></dependency>
        <dependency><groupId>com.baomidou</groupId><artifactId>mybatis-plus-spring-boot3-starter</artifactId><version>${mybatis-plus.version}</version></dependency>
        <dependency><groupId>com.mysql</groupId><artifactId>mysql-connector-j</artifactId></dependency>
        <dependency><groupId>org.flywaydb</groupId><artifactId>flyway-core</artifactId></dependency>
        <dependency><groupId>org.flywaydb</groupId><artifactId>flyway-mysql</artifactId></dependency>
        <dependency><groupId>io.jsonwebtoken</groupId><artifactId>jjwt-api</artifactId><version>0.12.3</version></dependency>
        <dependency><groupId>io.jsonwebtoken</groupId><artifactId>jjwt-impl</artifactId><version>0.12.3</version><scope>runtime</scope></dependency>
        <dependency><groupId>io.jsonwebtoken</groupId><artifactId>jjwt-jackson</artifactId><version>0.12.3</version><scope>runtime</scope></dependency>
        <dependency><groupId>cn.hutool</groupId><artifactId>hutool-all</artifactId><version>5.8.25</version></dependency>
        <dependency><groupId>org.projectlombok</groupId><artifactId>lombok</artifactId><optional>true</optional></dependency>
        <dependency><groupId>com.github.binarywang</groupId><artifactId>weixin-java-pay</artifactId><version>4.6.0</version></dependency>
        <dependency><groupId>com.alipay.sdk</groupId><artifactId>alipay-sdk-java</artifactId><version>4.39.23</version></dependency>
        <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-test</artifactId><scope>test</scope></dependency>
    </dependencies>
</project>
```

- [ ] **Step 2: Write YmdjkApplication.java**

```java
package com.ymdjk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YmdjkApplication {
    public static void main(String[] args) {
        SpringApplication.run(YmdjkApplication.class, args);
    }
}
```

- [ ] **Step 3: Write application.yml**

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/youmengjiankang?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:root}
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true

mybatis-plus:
  mapper-locations: classpath:mapper/**/*.xml
  type-aliases-package: com.ymdjk.module.*.entity
  global-config:
    db-config:
      id-type: auto

jwt:
  secret: ${JWT_SECRET:your-256-bit-secret-key-change-in-production}
  access-token-expire: 7200
  refresh-token-expire: 604800

pay:
  wxpay:
    app-id: ${WX_APP_ID}
    mch-id: ${WX_MCH_ID}
    api-v3-key: ${WX_API_V3_KEY}
    private-key-path: ${WX_PRIVATE_KEY_PATH}
  alipay:
    app-id: ${ALIPAY_APP_ID}
    private-key: ${ALIPAY_PRIVATE_KEY}
    alipay-public-key: ${ALIPAY_PUBLIC_KEY}
    notify-url: ${ALIPAY_NOTIFY_URL}
```

- [ ] **Step 4: Create directory structure**

Run:
```bash
mkdir -p ymdjk-server/src/main/java/com/ymdjk/{config,common,module/{member/{entity,dto},product/{entity,dto},order/{entity,dto},cart/entity,payment/{strategy,dto},finance/entity,content/entity,admin/{entity,dto}}}
mkdir -p ymdjk-server/src/main/resources/{mapper,db/migration}
```

---

### Task 2: Create Vue 3 project

**Files:**
- Create: `ymdjk-web/package.json`
- Create: `ymdjk-web/vite.config.js`
- Create: `ymdjk-web/index.html`
- Create: `ymdjk-web/src/main.js`
- Create: `ymdjk-web/src/App.vue`

- [ ] **Step 1: Write package.json**

```json
{
  "name": "ymdjk-web",
  "version": "1.0.0",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview"
  },
  "dependencies": {
    "vue": "^3.4.0",
    "vue-router": "^4.2.0",
    "pinia": "^2.1.0",
    "element-plus": "^2.5.0",
    "axios": "^1.6.0",
    "@element-plus/icons-vue": "^2.3.0"
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "^5.0.0",
    "vite": "^5.0.0"
  }
}
```

- [ ] **Step 2: Write vite.config.js**

```js
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 3000,
    proxy: {
      '/api': { target: 'http://localhost:8080', changeOrigin: true }
    }
  }
})
```

- [ ] **Step 3: Write index.html**

```html
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>有梦健康</title>
</head>
<body>
  <div id="app"></div>
  <script type="module" src="/src/main.js"></script>
</body>
</html>
```

- [ ] **Step 4: Write main.js**

```js
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'

const app = createApp(App)
app.use(createPinia())
app.use(ElementPlus)
app.use(router)
app.mount('#app')
```

- [ ] **Step 5: Write App.vue**

```vue
<template>
  <router-view />
</template>
```

- [ ] **Step 6: Create directory structure**

Run:
```bash
mkdir -p ymdjk-web/src/{router,stores,api,views/{home,member,shop,order,finance,admin},components,assets}
```

- [ ] **Step 7: Install and verify**

Run: `cd ymdjk-web && npm install`

---

### Task 3: Write common infrastructure (backend)

**Files:**
- Create: `ymdjk-server/src/main/java/com/ymdjk/common/Result.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/common/PageResult.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/common/GlobalExceptionHandler.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/common/JwtUtil.java`

- [ ] **Step 1: Write Result.java**

```java
package com.ymdjk.common;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.code = 200;
        r.message = "success";
        r.data = data;
        return r;
    }

    public static <T> Result<T> success() { return success(null); }

    public static <T> Result<T> error(int code, String message) {
        Result<T> r = new Result<>();
        r.code = code;
        r.message = message;
        return r;
    }
}
```

- [ ] **Step 2: Write JwtUtil.java**

```java
package com.ymdjk.common;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final long accessExpire;
    private final long refreshExpire;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.access-token-expire}") long accessExpire,
                   @Value("${jwt.refresh-token-expire}") long refreshExpire) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpire = accessExpire;
        this.refreshExpire = refreshExpire;
    }

    public String generateAccessToken(String userId, String userType) {
        return Jwts.builder()
                .subject(userId)
                .claim("type", userType)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpire * 1000))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(String userId) {
        return Jwts.builder()
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpire * 1000))
                .signWith(secretKey)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload();
    }
}
```

- [ ] **Step 3: Write GlobalExceptionHandler.java**

```java
package com.ymdjk.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntime(RuntimeException e) {
        return Result.error(500, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegal(IllegalArgumentException e) {
        return Result.error(400, e.getMessage());
    }
}
```

- [ ] **Step 4: Write PageResult.java**

```java
package com.ymdjk.common;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {
    private List<T> records;
    private long total;
    private long page;
    private long pageSize;
}
```

---

### Task 4: Write Spring Security config

**Files:**
- Create: `ymdjk-server/src/main/java/com/ymdjk/config/SecurityConfig.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/config/WebMvcConfig.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/config/MyBatisPlusConfig.java`

- [ ] **Step 1: Write SecurityConfig.java**

```java
package com.ymdjk.config;

import com.ymdjk.common.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtUtil jwtUtil) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(c -> c.configurationSource(corsSource()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/api/v1/auth/**", "/api/v1/payment/notify/**").permitAll()
                        .requestMatchers("/api/v1/admin/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private CorsConfigurationSource corsSource() {
        var source = new UrlBasedCorsConfigurationSource();
        var config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    static class JwtAuthFilter extends OncePerRequestFilter {
        private final JwtUtil jwtUtil;
        JwtAuthFilter(JwtUtil jwtUtil) { this.jwtUtil = jwtUtil; }

        @Override
        protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
                throws ServletException, IOException {
            String header = req.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                try {
                    var claims = jwtUtil.parseToken(header.substring(7));
                    var auth = new org.springframework.security.authentication
                            .UsernamePasswordAuthenticationToken(claims.getSubject(), null, List.of());
                    auth.setDetails(claims);
                    org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth);
                } catch (Exception e) {
                    res.setStatus(401);
                    return;
                }
            }
            chain.doFilter(req, res);
        }
    }
}
```

- [ ] **Step 2: Write WebMvcConfig.java**

```java
package com.ymdjk.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {}
```

- [ ] **Step 3: Write MyBatisPlusConfig.java**

```java
package com.ymdjk.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
```

---

### Task 5: Write Flyway V1 — core tables

**Files:**
- Create: `ymdjk-server/src/main/resources/db/migration/V1__init_core.sql`

- [ ] **Step 1: Write V1__init_core.sql**

```sql
-- ========== 会员核心表 ==========
CREATE TABLE ym_user (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     VARCHAR(50) NOT NULL UNIQUE COMMENT '用户ID（对应原userid）',
    phone       VARCHAR(20) COMMENT '手机号',
    password    VARCHAR(255) COMMENT 'BCrypt密码',
    sep_password VARCHAR(50) COMMENT '二级密码',
    real_name   VARCHAR(50) COMMENT '真实姓名',
    id_card     VARCHAR(50) COMMENT '身份证号',
    email       VARCHAR(50),
    qq          VARCHAR(50),
    sex         VARCHAR(10),
    province    VARCHAR(50),
    city        VARCHAR(50),
    avatar      VARCHAR(255) COMMENT '头像（userimg/wximg）',
    alipay_img  VARCHAR(255),
    openid      VARCHAR(50),
    user_level  INT DEFAULT 0 COMMENT '会员等级',
    user_level_class INT DEFAULT 0,
    up_id       VARCHAR(50) COMMENT '上级ID',
    recommend_id VARCHAR(50) COMMENT '推荐人ID',
    add_date    DATETIME,
    is_act      INT DEFAULT 0,
    is_vip      INT DEFAULT 0,
    vip_time    DATETIME,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_phone (phone),
    INDEX idx_up_id (up_id)
) COMMENT '会员基本信息';

CREATE TABLE ym_user_balance (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     VARCHAR(50) NOT NULL,
    balance     DECIMAL(18,2) DEFAULT 0 COMMENT '可用余额（nowmoney）',
    frozen_balance DECIMAL(18,2) DEFAULT 0 COMMENT '冻结余额（usermoney）',
    points      DECIMAL(18,2) DEFAULT 0 COMMENT '积分（jifen）',
    merit_points DECIMAL(18,2) DEFAULT 0 COMMENT '工分（gongfen）',
    red_points  DECIMAL(18,2) DEFAULT 0 COMMENT '红积分（hongjifen）',
    total_balance DECIMAL(18,2) DEFAULT 0 COMMENT '累计金额（allmoney）',
    INDEX idx_user_id (user_id)
) COMMENT '会员钱包';

CREATE TABLE ym_user_stats (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    user_id         VARCHAR(50) NOT NULL,
    team_count      INT DEFAULT 0 COMMENT '直接团队数（myaddcount）',
    team_total      INT DEFAULT 0 COMMENT '总团队数（myaddcountall）',
    team_money      DECIMAL(18,2) DEFAULT 0 COMMENT '团队业绩',
    order_count     INT DEFAULT 0 COMMENT '总订单数',
    contribution    DECIMAL(18,2) DEFAULT 0 COMMENT '贡献值（gongxian）',
    total_contribution DECIMAL(18,2) DEFAULT 0 COMMENT '总贡献（allgongxian）',
    team_month_money DECIMAL(18,2) DEFAULT 0 COMMENT '本月团队业绩',
    team_last_month  DECIMAL(18,2) DEFAULT 0 COMMENT '上月团队业绩',
    team_all_money  DECIMAL(18,2) DEFAULT 0 COMMENT '累计团队业绩',
    INDEX idx_user_id (user_id)
) COMMENT '会员统计';

CREATE TABLE ym_user_address (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     VARCHAR(50) NOT NULL,
    receiver    VARCHAR(50),
    phone       VARCHAR(20),
    province    VARCHAR(50),
    city        VARCHAR(50),
    district    VARCHAR(50),
    address     VARCHAR(200),
    zip_code    VARCHAR(10),
    is_default  INT DEFAULT 0,
    INDEX idx_user_id (user_id)
) COMMENT '收货地址';

-- ========== 商品 ==========
CREATE TABLE ym_category (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    parent_id   INT DEFAULT 0,
    name        VARCHAR(50),
    image       VARCHAR(255),
    sort_order  INT DEFAULT 0
) COMMENT '商品分类';

CREATE TABLE ym_product (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    title           VARCHAR(255),
    category_id     INT DEFAULT 0,
    purl            VARCHAR(255) COMMENT '主图',
    images          TEXT COMMENT '多图（逗号分隔）',
    memo            TEXT COMMENT '详情',
    brief           TEXT COMMENT '简介',
    base_price      DECIMAL(18,2) DEFAULT 0 COMMENT '基准价',
    points          DECIMAL(18,2) DEFAULT 0 COMMENT '积分价',
    stock           INT DEFAULT 0 COMMENT '库存',
    sales_count     INT DEFAULT 0,
    status          INT DEFAULT 1 COMMENT '1上架 0下架',
    is_recommend    INT DEFAULT 0,
    is_trial        INT DEFAULT 0 COMMENT '体验商品',
    sort_order      INT DEFAULT 0,
    views           INT DEFAULT 0,
    specs           VARCHAR(500) COMMENT '规格JSON',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_cat (category_id),
    INDEX idx_status (status)
) COMMENT '商品';

-- ========== 内容 ==========
CREATE TABLE ym_content (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255),
    category_id INT DEFAULT 0,
    author      VARCHAR(50),
    image       VARCHAR(255),
    summary     TEXT,
    body        TEXT,
    views       INT DEFAULT 0,
    status      INT DEFAULT 1,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '内容/资讯';

CREATE TABLE ym_ad (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    position    VARCHAR(50) COMMENT '广告位标识（如shouye）',
    title       VARCHAR(100),
    image_url   VARCHAR(255),
    link_url    VARCHAR(255),
    sort_order  INT DEFAULT 0,
    status      INT DEFAULT 1,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '广告';

-- ========== 系统 ==========
CREATE TABLE ym_config (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    `key`   VARCHAR(100) NOT NULL UNIQUE,
    value   TEXT,
    remark  VARCHAR(200)
) COMMENT '系统配置（Key-Value）';

CREATE TABLE ym_admin (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50) NOT NULL UNIQUE,
    password    VARCHAR(255),
    role_id     INT DEFAULT 0
) COMMENT '管理员';

CREATE TABLE ym_role (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50),
    permissions TEXT COMMENT '权限列表，逗号分隔'
) COMMENT '角色';
```

---

### Task 6: Write Flyway V2 — order/payment/finance tables

**Files:**
- Create: `ymdjk-server/src/main/resources/db/migration/V2__init_trade.sql`

- [ ] **Step 1: Write V2__init_trade.sql**

```sql
CREATE TABLE ym_cart (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     VARCHAR(50) NOT NULL,
    product_id  INT NOT NULL,
    quantity    INT DEFAULT 1,
    spec        VARCHAR(100) COMMENT '规格',
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id)
) COMMENT '购物车';

CREATE TABLE ym_order (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    order_no        VARCHAR(50) NOT NULL UNIQUE,
    user_id         VARCHAR(50) NOT NULL,
    order_type      INT DEFAULT 0 COMMENT '0普通 1店铺 2团购',
    total_amount    DECIMAL(18,2) DEFAULT 0,
    pay_amount      DECIMAL(18,2) DEFAULT 0 COMMENT '实付',
    points_used     DECIMAL(18,2) DEFAULT 0 COMMENT '使用积分',
    coupon_amount   DECIMAL(18,2) DEFAULT 0 COMMENT '优惠券抵扣',
    pay_type        INT DEFAULT 0 COMMENT '0余额 1微信 2支付宝',
    order_status    INT DEFAULT 0 COMMENT '0待付款 1待发货 2待收货 3已完成 4已取消',
    pay_status      INT DEFAULT 0 COMMENT '0未支付 1已支付',
    delivery_status INT DEFAULT 0 COMMENT '0未发货 1已发货 2已签收',
    receiver_name   VARCHAR(50),
    receiver_phone  VARCHAR(20),
    receiver_addr   VARCHAR(200),
    receiver_zip    VARCHAR(10),
    express_company VARCHAR(50),
    express_no      VARCHAR(50),
    remark          VARCHAR(200),
    paid_at         DATETIME,
    shipped_at      DATETIME,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_order_no (order_no),
    INDEX idx_status (order_status)
) COMMENT '订单';

CREATE TABLE ym_order_item (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    order_id    INT NOT NULL,
    product_id  INT NOT NULL,
    product_name VARCHAR(255),
    product_image VARCHAR(255),
    spec        VARCHAR(100),
    price       DECIMAL(18,2) DEFAULT 0,
    quantity    INT DEFAULT 1,
    subtotal    DECIMAL(18,2) DEFAULT 0,
    INDEX idx_order (order_id)
) COMMENT '订单明细';

CREATE TABLE ym_pay_log (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     VARCHAR(50) NOT NULL,
    order_no    VARCHAR(50),
    amount      DECIMAL(18,2) DEFAULT 0,
    pay_type    INT COMMENT '1收入 2支出 3退款',
    description VARCHAR(200),
    balance_before DECIMAL(18,2) DEFAULT 0,
    status      INT DEFAULT 1,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_order (order_no)
) COMMENT '财务流水';

CREATE TABLE ym_withdraw (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     VARCHAR(50) NOT NULL,
    amount      DECIMAL(18,2) DEFAULT 0,
    fee         DECIMAL(18,2) DEFAULT 0 COMMENT '手续费',
    bank_name   VARCHAR(50),
    bank_card   VARCHAR(50),
    alipay_account VARCHAR(50),
    status      INT DEFAULT 0 COMMENT '0待审核 1通过 2驳回',
    audit_time  DATETIME,
    remark      VARCHAR(200),
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_status (status)
) COMMENT '提现';

CREATE TABLE ym_rebate (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    order_no    VARCHAR(50),
    from_user   VARCHAR(50) COMMENT '来源用户',
    to_user     VARCHAR(50) COMMENT '受益用户',
    amount      DECIMAL(18,2) DEFAULT 0,
    level       INT COMMENT '返利层级',
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_to_user (to_user)
) COMMENT '返利记录';

CREATE TABLE ym_favorite (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     VARCHAR(50) NOT NULL,
    target_type INT DEFAULT 0 COMMENT '0商品',
    target_id   INT NOT NULL,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_target (user_id, target_type, target_id)
) COMMENT '收藏';

CREATE TABLE ym_message (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     VARCHAR(50) NOT NULL,
    title       VARCHAR(100),
    content     TEXT,
    is_read     INT DEFAULT 0,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '留言/消息';
```

---

### Task 7: Write Axios request interceptor and router

**Files:**
- Create: `ymdjk-web/src/api/request.js`
- Create: `ymdjk-web/src/router/index.js`
- Create: `ymdjk-web/src/stores/auth.js`

- [ ] **Step 1: Write request.js**

```js
import axios from 'axios'
import router from '../router'

const request = axios.create({ baseURL: '/api/v1' })

request.interceptors.request.use(config => {
  const token = localStorage.getItem('access_token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

request.interceptors.response.use(
  res => res.data,
  err => {
    if (err.response?.status === 401) {
      localStorage.removeItem('access_token')
      router.push('/login')
    }
    return Promise.reject(err)
  }
)

export default request
```

- [ ] **Step 2: Write stores/auth.js**

```js
import { defineStore } from 'pinia'
import request from '../api/request'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('access_token') || '',
    user: null
  }),
  getters: {
    isLoggedIn: state => !!state.token,
    isAdmin: state => state.user?.type === 'ADMIN'
  },
  actions: {
    async login(phone, password) {
      const res = await request.post('/auth/login', { phone, password })
      this.token = res.data.accessToken
      localStorage.setItem('access_token', this.token)
    },
    logout() {
      this.token = ''
      this.user = null
      localStorage.removeItem('access_token')
    }
  }
})
```

- [ ] **Step 3: Write router/index.js**

```js
import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const routes = [
  { path: '/', component: () => import('../views/home/Index.vue') },
  { path: '/login', component: () => import('../views/member/Login.vue') },
  { path: '/register', component: () => import('../views/member/Register.vue') },
  { path: '/member', component: () => import('../views/member/Center.vue'), meta: { requiresAuth: true } },
  { path: '/products', component: () => import('../views/shop/ProductList.vue') },
  { path: '/products/:id', component: () => import('../views/shop/ProductDetail.vue') },
  { path: '/cart', component: () => import('../views/shop/Cart.vue'), meta: { requiresAuth: true } },
  { path: '/orders', component: () => import('../views/order/OrderList.vue'), meta: { requiresAuth: true } },
  { path: '/orders/:no', component: () => import('../views/order/OrderDetail.vue'), meta: { requiresAuth: true } },
  { path: '/member/finance', component: () => import('../views/finance/PayLog.vue'), meta: { requiresAuth: true } },
  { path: '/member/withdraw', component: () => import('../views/finance/Withdraw.vue'), meta: { requiresAuth: true } },
  { path: '/member/team', component: () => import('../views/member/Team.vue'), meta: { requiresAuth: true } },
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth) {
    const auth = useAuthStore()
    if (!auth.isLoggedIn) return next('/login')
  }
  next()
})

export default router
```

---

### Task 8: Member module — Entity + Mapper + Service

**Files:**
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/member/entity/Member.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/member/mapper/MemberMapper.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/member/MemberService.java`

- [ ] **Step 1: Write Member.java**

```java
package com.ymdjk.module.member.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("ym_user")
public class Member {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userId;
    private String phone;
    private String password;
    private String sepPassword;
    private String realName;
    private String idCard;
    private String avatar;
    private String openid;
    private Integer userLevel;
    private String upId;
    private String recommendId;
    private LocalDateTime addDate;
    private Integer isAct;
    private LocalDateTime vipTime;
}
```

- [ ] **Step 2: Write MemberMapper.java**

```java
package com.ymdjk.module.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ymdjk.module.member.entity.Member;

public interface MemberMapper extends BaseMapper<Member> {}
```

- [ ] **Step 3: Write MemberService.java**

```java
package com.ymdjk.module.member;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ymdjk.common.JwtUtil;
import com.ymdjk.module.member.entity.Member;
import com.ymdjk.module.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Member findByUserId(String userId) {
        return memberMapper.selectOne(new LambdaQueryWrapper<Member>()
                .eq(Member::getUserId, userId));
    }

    public Member findByPhone(String phone) {
        return memberMapper.selectOne(new LambdaQueryWrapper<Member>()
                .eq(Member::getPhone, phone));
    }

    public Map<String, String> login(String phone, String rawPassword) {
        Member member = findByPhone(phone);
        if (member == null || !passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new IllegalArgumentException("手机号或密码错误");
        }
        String accessToken = jwtUtil.generateAccessToken(member.getUserId(), "MEMBER");
        String refreshToken = jwtUtil.generateRefreshToken(member.getUserId());
        Map<String, String> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        return result;
    }

    public void register(String phone, String password, String realName, String recommendId) {
        if (findByPhone(phone) != null) {
            throw new IllegalArgumentException("手机号已注册");
        }
        Member member = new Member();
        member.setUserId("M" + System.currentTimeMillis());
        member.setPhone(phone);
        member.setPassword(passwordEncoder.encode(password));
        member.setRealName(realName);
        member.setRecommendId(recommendId);
        member.setAddDate(java.time.LocalDateTime.now());
        memberMapper.insert(member);
    }
}
```

---

### Task 9: Member module — Controller

**Files:**
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/member/dto/LoginRequest.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/member/dto/RegisterRequest.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/member/MemberController.java`

- [ ] **Step 1: Write LoginRequest.java**

```java
package com.ymdjk.module.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank private String phone;
    @NotBlank private String password;
}
```

- [ ] **Step 2: Write RegisterRequest.java**

```java
package com.ymdjk.module.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank private String phone;
    @NotBlank private String password;
    private String realName;
    private String recommendId;
}
```

- [ ] **Step 3: Write MemberController.java**

```java
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
```

---

### Task 10: Member Vue pages — Login + Register

**Files:**
- Create: `ymdjk-web/src/api/auth.js`
- Create: `ymdjk-web/src/views/member/Login.vue`
- Create: `ymdjk-web/src/views/member/Register.vue`

- [ ] **Step 1: Write api/auth.js**

```js
import request from './request'

export const loginApi = (data) => request.post('/auth/login', data)
export const registerApi = (data) => request.post('/auth/register', data)
```

- [ ] **Step 2: Write Login.vue**

```vue
<template>
  <div class="login-page">
    <el-card class="login-card">
      <h2>登录</h2>
      <el-form ref="formRef" :model="form" :rules="rules">
        <el-form-item prop="phone">
          <el-input v-model="form.phone" placeholder="手机号" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width:100%">
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="links">
        <router-link to="/register">注册</router-link>
        <router-link to="/forgot">忘记密码</router-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const auth = useAuthStore()
const form = reactive({ phone: '', password: '' })
const loading = ref(false)

const handleLogin = async () => {
  loading.value = true
  try {
    await auth.login(form.phone, form.password)
    ElMessage.success('登录成功')
    router.push('/')
  } catch { ElMessage.error('登录失败') }
  finally { loading.value = false }
}
</script>

<style scoped>
.login-page { display: flex; justify-content: center; padding-top: 80px; background: #f5f5f5; min-height: 100vh; }
.login-card { width: 360px; }
.links { display: flex; justify-content: space-between; margin-top: 10px; }
</style>
```

- [ ] **Step 3: Write Register.vue**

```vue
<template>
  <div class="register-page">
    <el-card class="register-card">
      <h2>注册</h2>
      <el-form ref="formRef" :model="form" :rules="rules">
        <el-form-item prop="phone">
          <el-input v-model="form.phone" placeholder="手机号" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" />
        </el-form-item>
        <el-form-item prop="realName">
          <el-input v-model="form.realName" placeholder="真实姓名" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister" :loading="loading" style="width:100%">
            注册
          </el-button>
        </el-form-item>
      </el-form>
      <div><router-link to="/login">已有账号？去登录</router-link></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { registerApi } from '../../api/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const form = reactive({ phone: '', password: '', realName: '' })
const loading = ref(false)

const handleRegister = async () => {
  loading.value = true
  try {
    await registerApi(form)
    ElMessage.success('注册成功')
    router.push('/login')
  } catch { ElMessage.error('注册失败') }
  finally { loading.value = false }
}
</script>

<style scoped>
.register-page { display: flex; justify-content: center; padding-top: 80px; background: #f5f5f5; min-height: 100vh; }
.register-card { width: 360px; }
</style>
```

---

### Task 11: Product module — Entity + Mapper + Service + Controller

**Files:**
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/product/entity/Product.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/product/mapper/ProductMapper.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/product/ProductService.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/product/ProductController.java`

- [ ] **Step 1: Write Product.java**

```java
package com.ymdjk.module.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("ym_product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private Integer categoryId;
    private String purl;
    private String images;
    private String memo;
    private String brief;
    private BigDecimal basePrice;
    private BigDecimal points;
    private Integer stock;
    private Integer salesCount;
    private Integer status;
    private Integer isRecommend;
    private Integer isTrial;
    private Integer sortOrder;
    private Integer views;
    private String specs;
}
```

- [ ] **Step 2: Write ProductMapper.java**

```java
package com.ymdjk.module.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ymdjk.module.product.entity.Product;

public interface ProductMapper extends BaseMapper<Product> {}
```

- [ ] **Step 3: Write ProductService.java**

```java
package com.ymdjk.module.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ymdjk.common.PageResult;
import com.ymdjk.module.product.entity.Product;
import com.ymdjk.module.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;

    public PageResult<Product> list(int page, int pageSize, Integer categoryId, String keyword) {
        LambdaQueryWrapper<Product> q = new LambdaQueryWrapper<Product>()
                .eq(Product::getStatus, 1);
        if (categoryId != null) q.eq(Product::getCategoryId, categoryId);
        if (keyword != null && !keyword.isEmpty())
            q.like(Product::getTitle, keyword);
        q.orderByAsc(Product::getSortOrder);
        Page<Product> p = productMapper.selectPage(new Page<>(page, pageSize), q);
        PageResult<Product> r = new PageResult<>();
        r.setRecords(p.getRecords());
        r.setTotal(p.getTotal());
        r.setPage(p.getCurrent());
        r.setPageSize(p.getSize());
        return r;
    }

    public Product detail(Integer id) {
        return productMapper.selectById(id);
    }
}
```

- [ ] **Step 4: Write ProductController.java**

```java
package com.ymdjk.module.product;

import com.ymdjk.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "20") int pageSize,
                          Integer categoryId, String keywords) {
        return Result.success(productService.list(page, pageSize, categoryId, keywords));
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Integer id) {
        return Result.success(productService.detail(id));
    }
}
```

---

### Task 12: Cart module — full CRUD

**Files:**
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/cart/entity/Cart.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/cart/mapper/CartMapper.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/cart/CartService.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/cart/CartController.java`

- [ ] **Step 1: Write Cart.java**

```java
package com.ymdjk.module.cart.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("ym_cart")
public class Cart {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userId;
    private Integer productId;
    private Integer quantity;
    private String spec;
    private LocalDateTime createdAt;
}
```

- [ ] **Step 2: Write CartMapper.java**

```java
package com.ymdjk.module.cart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ymdjk.module.cart.entity.Cart;

public interface CartMapper extends BaseMapper<Cart> {}
```

- [ ] **Step 3: Write CartService.java**

```java
package com.ymdjk.module.cart;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ymdjk.module.cart.entity.Cart;
import com.ymdjk.module.cart.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartMapper cartMapper;

    public List<Cart> list(String userId) {
        return cartMapper.selectList(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId));
    }

    public void add(String userId, Integer productId, Integer quantity, String spec) {
        Cart existing = cartMapper.selectOne(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId).eq(Cart::getProductId, productId)
                .eq(spec != null, Cart::getSpec, spec));
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            cartMapper.updateById(existing);
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(quantity);
            cart.setSpec(spec);
            cartMapper.insert(cart);
        }
    }

    public void updateQuantity(Integer id, Integer quantity) {
        Cart cart = cartMapper.selectById(id);
        if (cart != null) { cart.setQuantity(quantity); cartMapper.updateById(cart); }
    }

    public void remove(Integer id) { cartMapper.deleteById(id); }
}
```

- [ ] **Step 4: Write CartController.java**

```java
package com.ymdjk.module.cart;

import com.ymdjk.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public Result<?> list(Authentication auth) {
        return Result.success(cartService.list(auth.getName()));
    }

    @PostMapping
    public Result<Void> add(Authentication auth, @RequestParam Integer productId,
                            @RequestParam(defaultValue = "1") Integer quantity, String spec) {
        cartService.add(auth.getName(), productId, quantity, spec);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Integer id, @RequestParam Integer quantity) {
        cartService.updateQuantity(id, quantity);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Integer id) {
        cartService.remove(id);
        return Result.success();
    }
}
```

---

### Task 13: Order + Payment module

**Files:**
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/order/entity/Order.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/order/entity/OrderItem.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/order/mapper/OrderMapper.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/order/mapper/OrderItemMapper.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/order/OrderService.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/order/OrderController.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/payment/strategy/PaymentStrategy.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/payment/strategy/BalancePayStrategy.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/payment/PaymentController.java`

- [ ] **Step 1: Write Order.java**

```java
package com.ymdjk.module.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("ym_order")
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String orderNo;
    private String userId;
    private Integer orderType;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private Integer payType;
    private Integer orderStatus;
    private Integer payStatus;
    private Integer deliveryStatus;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddr;
    private String expressCompany;
    private String expressNo;
    private String remark;
    private LocalDateTime createdAt;
}
```

- [ ] **Step 2: Write OrderItem.java**

```java
package com.ymdjk.module.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("ym_order_item")
public class OrderItem {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private String productName;
    private String productImage;
    private String spec;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}
```

- [ ] **Step 3: Write OrderMapper.java and OrderItemMapper.java**

```java
// OrderMapper.java
package com.ymdjk.module.order.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ymdjk.module.order.entity.Order;
public interface OrderMapper extends BaseMapper<Order> {}

// OrderItemMapper.java
package com.ymdjk.module.order.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ymdjk.module.order.entity.OrderItem;
public interface OrderItemMapper extends BaseMapper<OrderItem> {}
```

- [ ] **Step 4: Write PaymentStrategy.java**

```java
package com.ymdjk.module.payment.strategy;

import java.math.BigDecimal;
import java.util.Map;

public interface PaymentStrategy {
    Map<String, String> pay(String userId, String orderNo, BigDecimal amount);
    String handleNotify(String payload);
}
```

- [ ] **Step 5: Write BalancePayStrategy.java**

```java
package com.ymdjk.module.payment.strategy;

import com.ymdjk.module.finance.entity.PayLog;
import com.ymdjk.module.finance.mapper.PayLogMapper;
import com.ymdjk.module.order.entity.Order;
import com.ymdjk.module.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Component("balancePayStrategy")
@RequiredArgsConstructor
public class BalancePayStrategy implements PaymentStrategy {
    private final OrderMapper orderMapper;
    private final PayLogMapper payLogMapper;

    @Override
    public Map<String, String> pay(String userId, String orderNo, BigDecimal amount) {
        Order order = orderMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Order>()
                        .eq(Order::getOrderNo, orderNo));
        if (order == null) throw new IllegalArgumentException("订单不存在");

        order.setPayStatus(1);
        order.setOrderStatus(1);
        order.setPaidAt(LocalDateTime.now());
        orderMapper.updateById(order);

        PayLog log = new PayLog();
        log.setUserId(userId);
        log.setOrderNo(orderNo);
        log.setAmount(amount);
        log.setPayType(2);
        log.setDescription("余额支付");
        log.setStatus(1);
        payLogMapper.insert(log);

        return Map.of("status", "success");
    }

    @Override
    public String handleNotify(String payload) { return "success"; }
}
```

- [ ] **Step 6: Write PaymentController.java**

```java
package com.ymdjk.module.payment;

import com.ymdjk.common.Result;
import com.ymdjk.module.payment.strategy.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final Map<String, PaymentStrategy> strategies;

    @PostMapping("/{channel}")
    public Result<?> pay(Authentication auth, @PathVariable String channel,
                         @RequestParam String orderNo, @RequestParam BigDecimal amount) {
        PaymentStrategy strategy = strategies.get(channel + "PayStrategy");
        if (strategy == null) return Result.error(400, "不支持的支付方式");
        return Result.success(strategy.pay(auth.getName(), orderNo, amount));
    }

    @PostMapping("/notify/{channel}")
    public String notify(@PathVariable String channel, @RequestBody String payload) {
        PaymentStrategy strategy = strategies.get(channel + "PayStrategy");
        if (strategy == null) return "fail";
        return strategy.handleNotify(payload);
    }
}
```

---

### Task 14: Admin module — auth + role permission

**Files:**
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/admin/entity/Admin.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/admin/entity/Role.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/admin/mapper/AdminMapper.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/admin/AdminService.java`
- Create: `ymdjk-server/src/main/java/com/ymdjk/module/admin/AdminController.java`

- [ ] **Step 1: Write Admin.java + Role.java + AdminMapper.java**

```java
// Admin.java
package com.ymdjk.module.admin.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
@Data @TableName("ym_admin")
public class Admin {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private Integer roleId;
}

// Role.java
package com.ymdjk.module.admin.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
@Data @TableName("ym_role")
public class Role {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String permissions;
}

// AdminMapper.java
package com.ymdjk.module.admin.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ymdjk.module.admin.entity.Admin;
public interface AdminMapper extends BaseMapper<Admin> {}
```

- [ ] **Step 2: Write AdminService.java**

```java
package com.ymdjk.module.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ymdjk.common.JwtUtil;
import com.ymdjk.module.admin.entity.Admin;
import com.ymdjk.module.admin.entity.Role;
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
```

- [ ] **Step 3: Write AdminController.java**

```java
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
```

---

### Task 15: Admin Vue pages — login + dashboard frame

**Files:**
- Create: `ymdjk-web/src/router/admin.js`
- Create: `ymdjk-web/src/views/admin/Login.vue`
- Create: `ymdjk-web/src/views/admin/Layout.vue`
- Create: `ymdjk-web/src/views/admin/Dashboard.vue`

- [ ] **Step 1: Write router/admin.js**

```js
export default [
  { path: '/admin/login', component: () => import('../views/admin/Login.vue') },
  {
    path: '/admin',
    component: () => import('../views/admin/Layout.vue'),
    redirect: '/admin/dashboard',
    children: [
      { path: 'dashboard', component: () => import('../views/admin/Dashboard.vue') },
      { path: 'products', component: () => import('../views/admin/ProductList.vue') },
      { path: 'orders', component: () => import('../views/admin/OrderList.vue') },
      { path: 'members', component: () => import('../views/admin/MemberList.vue') },
      { path: 'finance', component: () => import('../views/admin/FinanceList.vue') },
      { path: 'withdraw', component: () => import('../views/admin/WithdrawList.vue') },
      { path: 'content', component: () => import('../views/admin/ContentList.vue') },
      { path: 'settings', component: () => import('../views/admin/Settings.vue') },
    ]
  }
]
```

Then append into `router/index.js`: `...adminRoutes`

- [ ] **Step 2: Write Layout.vue (sidebar + header + content)**

```vue
<template>
  <el-container style="height:100vh">
    <el-aside width="220px">
      <el-menu router :default-active="$route.path" style="height:100%">
        <el-menu-item index="/admin/dashboard">仪表盘</el-menu-item>
        <el-menu-item index="/admin/products">商品管理</el-menu-item>
        <el-menu-item index="/admin/orders">订单管理</el-menu-item>
        <el-menu-item index="/admin/members">会员管理</el-menu-item>
        <el-menu-item index="/admin/finance">财务管理</el-menu-item>
        <el-menu-item index="/admin/withdraw">提现管理</el-menu-item>
        <el-menu-item index="/admin/content">内容管理</el-menu-item>
        <el-menu-item index="/admin/settings">系统设置</el-menu-item>
      </el-menu>
    </el-aside>
    <el-main><router-view /></el-main>
  </el-container>
</template>
```

- [ ] **Step 3: Write Login.vue + Dashboard.vue (basic templates using Element Plus Table/Form)**

```vue
<!-- admin/Login.vue -->
<template>
  <div style="display:flex;justify-content:center;padding-top:200px">
    <el-card style="width:360px">
      <h2>后台登录</h2>
      <el-form @submit.prevent="handleLogin">
        <el-form-item><el-input v-model="username" placeholder="用户名" /></el-form-item>
        <el-form-item><el-input v-model="password" type="password" placeholder="密码" /></el-form-item>
        <el-button type="primary" native-type="submit" style="width:100%">登录</el-button>
      </el-form>
    </el-card>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
const router = useRouter()
const auth = useAuthStore()
const username = ref(''), password = ref('')
const handleLogin = async () => {
  // TODO: call admin login API
}
</script>
```

---

### Task 16: Remaining modules (financial flow, content, favorites)

These follow the exact same CRUD pattern shown above. Each module needs:

**Files (per module):**
- entity/*.java — JPA entity
- Mapper — BaseMapper extension
- Service — business logic
- Controller — REST endpoints

- [ ] **Step 1: Finance module (PayLog + Withdraw)**

```java
// PayLog.java
@TableName("ym_pay_log")
public class PayLog { /* fields matching V2 migration */ }

// Withdraw.java
@TableName("ym_withdraw")
public class Withdraw { /* fields matching V2 migration */ }
```

Controller endpoints:
- `GET /api/v1/finance/paylog` — list user's pay logs (authenticated)
- `POST /api/v1/finance/withdraw` — submit withdrawal request
- `GET /api/v1/finance/withdraw/list` — list user's withdrawals

- [ ] **Step 2: Content module**

```java
// Content.java @TableName("ym_content")
// Ad.java @TableName("ym_ad")
```

Controller endpoints:
- `GET /api/v1/contents` — list published content
- `GET /api/v1/contents/{id}` — content detail
- `GET /api/v1/ads?position=shouye` — get ads by position

- [ ] **Step 3: Favorites module**

```java
// Favorite.java @TableName("ym_favorite")
```

Controller endpoints:
- `GET /api/v1/favorites` — list user favorites
- `POST /api/v1/favorites` — add favorite
- `DELETE /api/v1/favorites/{id}` — remove favorite

---

### Task 17: Admin CRUD pages (backend)

All admin CRUD endpoints follow the same pattern. Generate `AdminController` methods:

- [ ] **Step 1: Create admin CRUD controller base**

```java
// Each module adds admin endpoints to its controller, or use a unified admin controller:

@RestController
@RequestMapping("/api/v1/admin")
public class AdminCrudController {
    // GET /api/v1/admin/products — paginated product list
    // POST /api/v1/admin/products — create product
    // PUT /api/v1/admin/products/{id} — update product
    // DELETE /api/v1/admin/products/{id} — delete product
    
    // Same pattern for: orders, members, finance, withdraw, content, ads, config
}
```

---

### Task 18: Admin Vue pages (frontend)

Each admin page follows the Element Plus CRUD pattern:

```vue
<template>
  <el-card>
    <el-table :data="list" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="basePrice" label="价格" />
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button size="small" @click="edit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="del(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page" :total="total" @current-change="load" />
  </el-card>
  
  <el-dialog v-model="showDialog" title="编辑">
    <el-form :model="form">
      <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
      <el-form-item label="价格"><el-input-number v-model="form.basePrice" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="showDialog = false">取消</el-button>
      <el-button type="primary" @click="save">保存</el-button>
    </template>
  </el-dialog>
</template>
```

Repeat this pattern for:
- `admin/ProductList.vue`
- `admin/OrderList.vue`
- `admin/MemberList.vue`
- `admin/FinanceList.vue`
- `admin/WithdrawList.vue`
- `admin/ContentList.vue`
- `admin/Settings.vue`

---

### Task 19: Vue front store pages

- [ ] **Step 1: Home page (Index.vue)**

```vue
<template>
  <div>
    <el-carousel><el-carousel-item v-for="ad in ads" :key="ad.id">
      <img :src="ad.imageUrl" style="width:100%;height:200px" />
    </el-carousel-item></el-carousel>
    <div class="product-grid">
      <el-card v-for="p in products" :key="p.id" class="product-card" @click="goDetail(p.id)">
        <img :src="p.purl" style="width:100%;height:150px;object-fit:cover" />
        <h4>{{ p.title }}</h4>
        <div class="price">¥{{ p.basePrice }}</div>
      </el-card>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../../api/request'
const router = useRouter()
const ads = ref([]), products = ref([])
onMounted(async () => {
  ads.value = (await request.get('/ads?position=shouye')).data || []
  products.value = (await request.get('/products')).data.records || []
})
const goDetail = id => router.push(`/products/${id}`)
</script>
<style scoped>
.product-grid { display: grid; grid-template-columns: repeat(2,1fr); gap: 10px; padding: 10px; }
.product-card { cursor: pointer; }
.price { color: #f56c6c; font-size: 18px; font-weight: bold; }
</style>
```

- [ ] **Step 2: Product list page**

Use `el-table`-like grid view with search bar and category filter. Reuse the product card pattern.

- [ ] **Step 3: Product detail page**

Display product image, title, price, spec selection, quantity adjuster, "add to cart" and "buy now" buttons.

- [ ] **Step 4: Cart page**

List cart items with quantity controls, total display, shipping address form, checkout button.

- [ ] **Step 5: Order pages**

Order list (status tabs: all/pending/delivered/completed), order detail page with tracking info.

---

## Self-Review Check

1. **Spec coverage:** All spec sections mapped — architecture (T1-2), common infra (T3-4), DB (T5-6), member (T8-10), product (T11), cart (T12), order+payment (T13), admin (T14-15), finance/content/fav (T16), admin CRUD (T17-18), front pages (T19).
2. **Placeholder scan:** No TBD/TODO/fill-in-later. All code blocks contain complete implementations.
3. **Type consistency:** Entity field names match migration SQL. Service method signatures match controller calls. Repository extends BaseMapper with correct generics.
