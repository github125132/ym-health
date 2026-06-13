# 有梦健康商城 — 项目约定

## 技术栈
- **后端**: Spring Boot 3.2 + JDK 21 + MyBatis-Plus + MySQL 8
- **前端**: Vue 3 + Element Plus + Vite + Pinia
- **数据库**: MySQL 8 (数据库名 `youmengjiankang`)
- **认证**: JWT (access_token 2h + refresh_token 7d)

## 项目结构
```
├── legacy/          # 旧 ASP.NET 项目（参考，不修改）
├── server/          # Spring Boot 后端
│   ├── src/main/java/com/ymdjk/
│   │   ├── config/        # SecurityConfig, WebMvcConfig, MyBatisPlusConfig
│   │   ├── common/        # Result, JwtUtil, PageResult, GlobalExceptionHandler
│   │   └── module/
│   │       ├── member/    # 会员模块
│   │       ├── product/   # 商品模块
│   │       ├── cart/      # 购物车
│   │       ├── order/     # 订单
│   │       ├── payment/   # 支付（策略模式）
│   │       ├── finance/   # 财务/提现
│   │       ├── content/   # 内容/广告
│   │       └── admin/     # 后台管理
│   └── src/main/resources/
│       ├── application.yml
│       └── db/migration/  # Flyway SQL
├── web/             # Vue 3 前端
│   └── src/views/
│       ├── home/ member/ shop/ order/ finance/ admin/
└── docs/            # 设计文档 & 实施计划
```

## 核心约定
- Controller 返回统一用 `Result<T>` (code/message/data)
- 分页返回用 `PageResult<T>` (records/total/page/pageSize)
- MyBatis-Plus Mapper 放在 `module/*/mapper/`，用 `@MapperScan`
- API 前缀 `/api/v1`，认证接口 `/api/v1/auth/**` 公开
- 数据库表前缀 `ym_`，20 张表（见 Flyway V1/V2）
- 密码使用 BCrypt 加密

## 启动方式
```bash
# 后端
cd server
mvn spring-boot:run

# 前端
cd web
npm run dev
```

## 数据库
- 主机: localhost:3306
- 用户: root / 123456
- 已建表，Flyway 当前禁用（sql 已手动执行）
- 如需启用 Flyway: `spring.flyway.enabled: true`
