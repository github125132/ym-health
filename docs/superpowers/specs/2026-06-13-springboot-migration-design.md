# 有梦健康商城 — Spring Boot 迁移设计方案

## 1. 概述

将现有 ASP.NET WebForms 4.5 + SQL Server 的移动健康商城系统，迁移为 Spring Boot 3.x + Vue 3 前后端分离架构，数据库迁移到 MySQL 8.x。

## 2. 技术栈

| 层级 | 选型 | 说明 |
|------|------|------|
| 后端框架 | Spring Boot 3.x + JDK 17 | 标准企业级 Java 框架 |
| ORM | MyBatis-Plus | SQL 可控，适合复杂查询迁移 |
| 认证 | Spring Security + JWT | 替代 Session 认证 |
| 数据库 | MySQL 8.x | 从 SQL Server 迁移 |
| 数据库迁移 | Flyway | 版本化迁移脚本 |
| 前端框架 | Vue 3 + Element Plus | 前后端分离 SPA |
| 构建 | Vite + Maven | |
| 支付 SDK | weixin-java-pay + alipay-sdk-java | |

## 3. 架构

```
Vue 3 SPA (前台+后台)
    ↓ REST API (JWT)
Spring Boot Backend
    ├─ Controller 层
    ├─ Service 层
    └─ Repository 层 (MyBatis-Plus)
          ↓
       MySQL
```

### 包结构

```
com.ymdjk
├── config/          # Security, WebMvc, MyBatis-Plus, Pay
├── common/          # Result, PageResult, JwtUtil, GlobalExceptionHandler
├── module/
│   ├── member/      # 会员 (Controller/Service/Mapper/Entity)
│   ├── product/     # 商品
│   ├── order/       # 订单
│   ├── payment/     # 支付 (策略模式)
│   ├── finance/     # 财务/积分/提现
│   ├── content/     # 内容/广告
│   └── admin/       # 后台管理
└── YmdjkApplication.java
```

## 4. 数据模型重构

### 表清单

| 原 SQL Server 表 | 新 MySQL 表 | 说明 |
|------------------|-------------|------|
| usertable | ym_user | 拆分为用户基础表 |
| usertable (财务字段) | ym_user_balance | 钱包/积分 |
| usertable (统计字段) | ym_user_stats | 团队/业绩 |
| usertable (店铺字段) | ym_user_shop | 店铺信息 |
| usertable (VIP字段) | ym_user_vip | 等级/VIP |
| shop_news | ym_product | 商品 |
| lanmu | ym_category | 商品分类 |
| orderlist | ym_order | 订单 (合并多表) |
| Shopinglist | ym_order_item | 订单明细 |
| usercart | ym_cart | 购物车 |
| paylog | ym_pay_log | 财务流水 |
| config | ym_config | 系统配置 |
| admintable | ym_admin | 管理员 |
| roletable | ym_role | 角色 |
| adtable | ym_ad | 广告 |
| Favoritestable | ym_favorite | 收藏 |
| net_news | ym_content | 内容 |
| fantable | ym_rebate | 返利记录 |
| torder_s | ym_commission | 佣金分配 |
| message | ym_message | 留言 |
| 新增 | ym_withdraw | 提现记录 |

### 价格策略

原多档价格字段（p0/p1/p2）改为：`base_price` + 等级折扣率 `level_discount` 计算。

## 5. API 设计

- 统一前缀：`/api/v1`
- 统一返回：`{ code, message, data }`
- 认证：JWT (access_token 2h + refresh_token 7d)
- 支付使用策略模式：WxPayStrategy / AlipayStrategy / BalancePayStrategy

### 主要端点

| 模块 | 端点 |
|------|------|
| 认证 | POST /api/v1/auth/login, /register |
| 会员 | GET/PUT /api/v1/member/profile |
| 团队 | GET /api/v1/member/team |
| 商品 | GET /api/v1/products, /products/{id} |
| 购物车 | GET/POST/PUT/DELETE /api/v1/cart |
| 订单 | POST/GET /api/v1/orders |
| 支付 | POST /api/v1/payment/{channel} |
| 通知 | POST /api/v1/payment/notify/{channel} |
| 财务 | GET /api/v1/finance/paylog, POST /api/v1/finance/withdraw |
| 管理 | /api/v1/admin/** |

## 6. 前端设计

- Vue 3 + Vite + Element Plus + Pinia + Axios
- 路由懒加载，后台使用 Element Plus Table/Form/Dialog 替代 GridView
- 前台页面覆盖原 89 个 ASPX 主要功能，后台覆盖 76 个管理页面
- 静态资源（CSS/图片/字体）可直接复用

## 7. 安全

- BCrypt 密码加密
- JWT 无状态认证
- `@PreAuthorize` 注解式权限控制
- 文件上传限制扩展名 + MIME 校验
- MySQL 密码通过环境变量注入

## 8. 迁移策略

### 阶段一：基础框架搭建
1. Spring Boot 项目初始化 + MyBatis-Plus + JWT + 统一返回
2. Flyway V1 初始化脚本（根据 SQL Server schema 设计 MySQL 新表）
3. Vue 3 项目初始化 + Element Plus + Axios + Router

### 阶段二：模块开发顺序（整体迁移）
1. **会员模块** — 注册/登录/会员中心/团队
2. **商品 + 购物车** — 浏览/搜索/加入购物车
3. **订单 + 支付** — 下单/支付回调
4. **财务 + 提现** — 流水/提现/积分
5. **内容 + 广告** — 信息/广告位
6. **后台管理** — CRUD + 角色权限

### 阶段三：资源迁移
- 静态资源（upload/、erweicode/、ueditor 上传目录）拷贝到新系统
- 日志体系改为 SLF4J + Logback
- UEditor 保留或替换为轻量级编辑器

## 9. 逆向工程

由于业务逻辑编译在 web.dll 中，没有 .cs 源文件：
- 从 ASPX 模板提取 Eval()/DataBinder 字段名确定实体字段
- 从 DLL 符号名确定服务类职责
- 从 URL 参数和表单提交确定 API 入参
- 从后台管理页面逻辑确定管理接口

## 10. 风险与缓解

| 风险 | 缓解 |
|------|------|
| 业务逻辑丢失（无源码） | 逐页分析 ASPX 模板，还原核心逻辑 |
| 支付对接复杂度 | 使用成熟 SDK，保留原支付参数结构 |
| 36 表数据模型重构 | 先做表映射，再逐模块迁移逻辑 |
| 前端页面多（165+） | 路由懒加载，按模块分批开发 |
