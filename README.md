# 有梦健康商城 (ym-health)

基于 Spring Boot 3 + Vue 3 的移动健康管理系统。

## 模块

| 模块 | 功能 |
|------|------|
| 会员 | 注册/登录/会员中心/团队管理 |
| 商城 | 商品列表/详情/分类/搜索 |
| 购物车 | 增删改查/数量调整 |
| 订单 | 下单/订单列表/详情 |
| 支付 | 余额支付/微信支付/支付宝（策略模式） |
| 财务 | 流水明细/提现申请 |
| 内容 | 资讯/广告位 |
| 后台 | 商品/订单/会员/财务/提现/内容/设置 |

## 项目结构

```
ym-health/
├── legacy/        # 旧 ASP.NET 项目（参考）
├── server/        # Spring Boot 后端
│   ├── pom.xml
│   └── src/main/java/com/ymdjk/
│       ├── config/     # 安全/配置
│       ├── common/     # 公共工具
│       └── module/     # 业务模块
├── web/           # Vue 3 前端
│   ├── package.json
│   └── src/views/     # 页面组件
└── docs/          # 文档
```

## 快速开始

### 数据库
```bash
mysql -u root -p123456 -e "CREATE DATABASE IF NOT EXISTS youmengjiankang DEFAULT CHARSET utf8mb4;"
mysql -u root -p123456 youmengjiankang < server/src/main/resources/db/migration/V1__init_core.sql
mysql -u root -p123456 youmengjiankang < server/src/main/resources/db/migration/V2__init_trade.sql
```

### 后端
```bash
cd server
mvn spring-boot:run
# 启动在 http://localhost:8080
```

### 前端
```bash
cd web
npm install
npm run dev
# 启动在 http://localhost:3000
```

## API 示例

```bash
# 注册
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"phone":"13800138000","password":"123456","realName":"张三"}'

# 登录
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"phone":"13800138000","password":"123456"}'

# 商品列表
curl http://localhost:8080/api/v1/products?page=1&pageSize=20
```

## 技术栈

- Spring Boot 3.2 / JDK 17
- MyBatis-Plus 3.5.5
- MySQL 8 / Flyway
- Spring Security + JWT
- Vue 3 + Element Plus + Pinia
- 微信支付 (weixin-java-pay)
