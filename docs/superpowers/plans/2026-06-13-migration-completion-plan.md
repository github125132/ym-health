# Plan A: 支付对接 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 完成微信支付和支付宝的 SDK 对接，实现完整交易闭环

**Architecture:** 在现有的 PaymentStrategy 策略模式基础上，实现 WxPayStrategy 和 AlipayStrategy，对接 weixin-java-pay 和 alipay-sdk-java SDK。统一回调入口 `/api/v1/payment/notify/{channel}`，幂等处理。

**Tech Stack:** weixin-java-pay 4.6.0, alipay-sdk-java 4.38.x, Spring Boot

---

### Task A1: WxPayStrategy 实现

**Files:**
- Modify: `server/src/main/java/com/ymdjk/module/payment/strategy/WxPayStrategy.java`
- Modify: `server/src/main/java/com/ymdjk/config/PayConfig.java`
- Create: `server/src/main/java/com/ymdjk/module/payment/dto/WxPayRequest.java`

**Context:** 旧项目微信 AppID `wx50a1c4e176ffae49`, Secret `408f1eed657f87ad925fbcb553e053a8`（可能已过期需替换）。参照 `server/src/main/java/com/ymdjk/module/payment/strategy/BalancePayStrategy.java` 的风格。

- [ ] **Step 1: 创建 PayConfig.java**

```java
package com.ymdjk.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayConfig {

    @Bean
    public WxPayService wxPayService(@Value("${pay.wxpay.app-id:}") String appId,
                                      @Value("${pay.wxpay.mch-id:}") String mchId,
                                      @Value("${pay.wxpay.api-v3-key:}") String apiV3Key) {
        WxPayConfig config = new WxPayConfig();
        config.setAppId(appId);
        config.setMchId(mchId);
        config.setApiV3Key(apiV3Key);
        WxPayService service = new WxPayServiceImpl();
        service.setConfig(config);
        return service;
    }
}
```

- [ ] **Step 2: 实现 WxPayStrategy**

```java
package com.ymdjk.module.payment.strategy;

import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component("wxpayPayStrategy")
@RequiredArgsConstructor
public class WxPayStrategy implements PaymentStrategy {
    private final WxPayService wxPayService;

    @Override
    public Map<String, String> pay(String userId, String orderNo, BigDecimal amount) {
        WxPayUnifiedOrderV3Request req = new WxPayUnifiedOrderV3Request()
                .setOutTradeNo(orderNo)
                .setAmount(new WxPayUnifiedOrderV3Request.Amount().setTotal(amount.multiply(BigDecimal.valueOf(100)).intValue()))
                .setDescription("有梦健康-商品购买");
        WxPayUnifiedOrderV3Result result = wxPayService.createOrderV3(req);
        return Map.of("prepayId", result.getPrepayId(), "status", "pending");
    }

    @Override
    public String handleNotify(String payload) {
        // 解析微信回调 XML/JSON，验签，更新订单状态
        return "success";
    }
}
```

- [ ] **Step 3: 编译验证**

```bash
cd server && mvn compile -q
```

Expected: BUILD SUCCESS

---

### Task A2: AlipayStrategy 实现

**Files:**
- Create: `server/src/main/java/com/ymdjk/module/payment/strategy/AlipayStrategy.java`
- Modify: `server/src/main/java/com/ymdjk/config/PayConfig.java`

- [ ] **Step 1: 在 PayConfig.java 添加 AlipayClient Bean**

```java
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

@Bean
public AlipayClient alipayClient(@Value("${pay.alipay.app-id:}") String appId,
                                  @Value("${pay.alipay.private-key:}") String privateKey,
                                  @Value("${pay.alipay.public-key:}") String publicKey) {
    return new DefaultAlipayClient(
        "https://openapi.alipay.com/gateway.do",
        appId, privateKey, "json", "UTF-8", publicKey, "RSA2");
}
```

- [ ] **Step 2: 实现 AlipayStrategy**

```java
package com.ymdjk.module.payment.strategy;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component("alipayPayStrategy")
@RequiredArgsConstructor
public class AlipayStrategy implements PaymentStrategy {
    private final AlipayClient alipayClient;

    @Override
    public Map<String, String> pay(String userId, String orderNo, BigDecimal amount) {
        AlipayTradePrecreateRequest req = new AlipayTradePrecreateRequest();
        req.setNotifyUrl("/api/v1/payment/notify/alipay");
        req.setBizContent("{" +
            "\"out_trade_no\":\"" + orderNo + "\"," +
            "\"total_amount\":\"" + amount + "\"," +
            "\"subject\":\"有梦健康-商品购买\"}");
        // AlipayResponse resp = alipayClient.execute(req);
        return Map.of("qrCode", "placeholder", "status", "pending");
    }

    @Override
    public String handleNotify(String payload) { return "success"; }
}
```

- [ ] **Step 3: 编译验证**

```bash
cd server && mvn compile -q
```

---

### Task A3: 支付回调幂等处理

**Files:**
- Modify: `server/src/main/java/com/ymdjk/module/payment/PaymentController.java`
- Modify: `server/src/main/java/com/ymdjk/module/order/OrderService.java`

**Context:** 确保支付回调按 orderNo + transactionId 做幂等，防止重复通知导致多次更新订单。

- [ ] **Step 1: 在 OrderService 添加幂等方法**

```java
public void markPaid(String orderNo, String transactionId) {
    Order order = getOrder(orderNo);
    if (order == null) throw new IllegalArgumentException("订单不存在");
    if (order.getPayStatus() == 1) return; // 幂等：已支付跳过
    order.setPayStatus(1);
    order.setOrderStatus(1);
    order.setPaidAt(LocalDateTime.now());
    orderMapper.updateById(order);
}
```

- [ ] **Step 2: 更新 PaymentController.notify() 调用 markPaid**

- [ ] **Step 3: 编译 + 测试**

---

### Task A4: 种子支付配置数据

**Files:**
- Modify: `server/src/main/java/com/ymdjk/config/DataInitializer.java`

- [ ] **Step 1: 在 DataInitializer 插入 ym_config 支付相关配置**

---

## Plan B: 用户功能完善

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development

**Goal:** 完成会员地址管理、收藏、密码修改、编辑资料

**Architecture:** 新增 Controller + Service，复用已有 entity/mapper。前端新增页面或弹窗。

---

### Task B1: 收货地址管理

**Files:**
- Create: `server/src/main/java/com/ymdjk/module/member/AddressController.java`
- Create: `server/src/main/java/com/ymdjk/module/member/AddressService.java`
- Modify: `web/src/router/index.js` 添加地址路由
- Create: `web/src/views/member/Address.vue`

**UserAddress 实体字段:** id, userId, receiver, phone, province, city, district, address, zipCode, isDefault

- [ ] **Step 1: 创建 AddressService (CRUD)**

```
GET    /api/v1/address          — 地址列表
POST   /api/v1/address          — 新增地址
PUT    /api/v1/address/{id}     — 编辑地址
DELETE /api/v1/address/{id}     — 删除地址
PUT    /api/v1/address/{id}/default — 设为默认
```

- [ ] **Step 2: 创建 AddressController**

- [ ] **Step 3: 创建 Address.vue** (Element Plus Form 表单)

- [ ] **Step 4: Center.vue 添加地址入口按钮**

- [ ] **Step 5: 编译 + 运行验证**

---

### Task B2: 收藏功能

**Files:**
- Create: `server/src/main/java/com/ymdjk/module/member/FavoriteController.java`
- Create: `server/src/main/java/com/ymdjk/module/member/FavoriteService.java`
- Create: `web/src/views/member/Favorites.vue`

**ym_favorite 字段:** id, userId, targetType, targetId, createdAt

- [ ] **Step 1: 创建 FavoriteService**

```
GET    /api/v1/favorites        — 收藏列表
POST   /api/v1/favorites        — 添加收藏 (productId)
DELETE /api/v1/favorites/{id}   — 取消收藏
```

- [ ] **Step 2: 创建 FavoriteController**

- [ ] **Step 3: ProductDetail.vue 添加收藏按钮**

- [ ] **Step 4: 创建 Favorites.vue 页面**

- [ ] **Step 5: 编译验证**

---

### Task B3: 修改密码 + 编辑资料

**Files:**
- Modify: `server/src/main/java/com/ymdjk/module/member/MemberController.java`
- Modify: `server/src/main/java/com/ymdjk/module/member/MemberService.java`
- Create: `server/src/main/java/com/ymdjk/module/member/dto/UpdateProfileRequest.java`
- Create: `server/src/main/java/com/ymdjk/module/member/dto/ChangePasswordRequest.java`
- Create: `web/src/views/member/EditProfile.vue`

- [ ] **Step 1: 后端 API**

```
PUT  /api/v1/member/profile        — 更新资料（姓名/性别/头像）
PUT  /api/v1/member/password       — 修改密码（旧密码+新密码）
```

- [ ] **Step 2: 前端 EditProfile.vue + Center.vue 入口**

- [ ] **Step 3: 编译验证**

---

## Plan C: 后台管理 CRUD 完善

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development

**Goal:** 完成分类/广告/管理员/角色/系统设置/留言管理等后台功能

**Architecture:** 在 AdminCrudController 基础上扩展，或创建独立 Controller。前端复用 Element Plus Table Form 模式。

---

### Task C1: 分类管理（后台）

**Files:**
- Create: `server/src/main/java/com/ymdjk/module/product/CategoryController.java`
- Create: `web/src/views/admin/CategoryList.vue`
- Modify: `web/src/router/admin.js` 添加路由

- [ ] **Step 1: CategoryController (CRUD)**

```
GET    /api/v1/admin/categories        — 分类列表
POST   /api/v1/admin/categories        — 新增分类
PUT    /api/v1/admin/categories/{id}   — 编辑分类
DELETE /api/v1/admin/categories/{id}   — 删除分类
```

- [ ] **Step 2: CategoryService 添加业务逻辑（检查分类下是否有商品再删除）**

- [ ] **Step 3: 前端 CategoryList.vue**（参照 ProductList.vue 模式）

- [ ] **Step 4: 左侧菜单添加分类入口**

- [ ] **Step 5: 编译验证**

---

### Task C2: 广告管理（后台）

**Files:**
- Create: `server/src/main/java/com/ymdjk/module/content/AdAdminController.java`
- Create: `web/src/views/admin/AdList.vue`
- Modify: `web/src/router/admin.js` 添加路由

- [ ] **Step 1: AdAdminController (CRUD)**

```
GET    /api/v1/admin/ads           — 广告列表
POST   /api/v1/admin/ads           — 新增
PUT    /api/v1/admin/ads/{id}      — 编辑
DELETE /api/v1/admin/ads/{id}      — 删除
```

- [ ] **Step 2: Admin Layout.vue 左侧菜单添加**

- [ ] **Step 3: 编译验证**

---

### Task C3: 管理员 + 角色管理（后台）

**Files:**
- Create: `server/src/main/java/com/ymdjk/module/admin/AdminUserController.java`
- Create: `web/src/views/admin/AdminList.vue`
- Create: `web/src/views/admin/RoleList.vue`
- Modify: `web/src/router/admin.js`

- [ ] **Step 1: 管理员 CRUD**

```
GET    /api/v1/admin/admins         — 管理员列表
POST   /api/v1/admin/admins         — 新增管理员
PUT    /api/v1/admin/admins/{id}    — 编辑
DELETE /api/v1/admin/admins/{id}    — 删除
GET    /api/v1/admin/roles          — 角色列表
POST   /api/v1/admin/roles          — 新增角色 (name + permissions)
PUT    /api/v1/admin/roles/{id}     — 编辑
DELETE /api/v1/admin/roles/{id}     — 删除
```

- [ ] **Step 2: 前端 AdminList.vue + RoleList.vue**

- [ ] **Step 3: 左侧菜单添加**

- [ ] **Step 4: 编译验证**

---

### Task C4: 系统设置 API

**Files:**
- Modify: `server/src/main/java/com/ymdjk/module/admin/AdminCrudController.java`
- Modify: `web/src/views/admin/Settings.vue`

- [ ] **Step 1: 配置 CRUD (ym_config 表)**

```
GET  /api/v1/admin/config          — 获取所有配置
PUT  /api/v1/admin/config/{key}    — 更新某个配置
```

- [ ] **Step 2: Settings.vue 对接真实 API，保存按钮写入数据库**

- [ ] **Step 3: 编译验证**

---

### Task C5: 留言管理（后台）

**Files:**
- Create: `server/src/main/java/com/ymdjk/module/content/MessageController.java`
- Create: `web/src/views/admin/MessageList.vue`

- [ ] **Step 1: 留言 API**

```
GET  /api/v1/messages              — 留言列表（公开，可匿名提交）
POST /api/v1/messages              — 提交留言
GET  /api/v1/admin/messages        — 后台查看（管理员）
```

- [ ] **Step 2: MessageList.vue + gbook.vue（前台留言页面）**

- [ ] **Step 3: 编译验证**

---

## Plan D: 财务与积分系统

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development

**Goal:** 完成提现审核、积分系统、返利分红

**Architecture:** 在 FinanceService 基础上扩展，新增积分 Service。前端扩展现有页面。

---

### Task D1: 提现审核（后台）

**Files:**
- Modify: `server/src/main/java/com/ymdjk/module/finance/FinanceService.java`
- Modify: `server/src/main/java/com/ymdjk/module/finance/FinanceController.java`
- Modify: `web/src/views/admin/WithdrawList.vue`

**当前状态:** approveWithdraw 已有（设置 status=1），但缺少驳回功能

- [ ] **Step 1: 添加驳回接口**

```
POST /api/v1/admin/withdraw/{id}/reject  — 驳回（status=2, remark）
```

- [ ] **Step 2: WithdrawList.vue 添加驳回按钮**

- [ ] **Step 3: 提现审核后更新 ym_user_balance 余额（通过时冻结→扣除，驳回时冻结→退回）**

```java
public void approveWithdraw(Integer id) {
    Withdraw w = withdrawMapper.selectById(id);
    if (w == null || w.getStatus() != 0) return;
    w.setStatus(1);
    withdrawMapper.updateById(w);
    // 扣除用户余额
    // UPDATE ym_user_balance SET balance = balance - amount WHERE user_id = ?
}
```

- [ ] **Step 4: 用户端提现记录列表**

```
GET /api/v1/finance/withdraw/list  — 用户自己的提现记录
```

- [ ] **Step 5: 编译验证**

---

### Task D2: 积分系统

**Files:**
- Create: `server/src/main/java/com/ymdjk/module/finance/PointsService.java`
- Create: `server/src/main/java/com/ymdjk/module/finance/PointsController.java`
- Create: `web/src/views/finance/PointsExchange.vue`

**ym_user_balance 字段:** points(积分), meritPoints(工分), redPoints(红积分)

- [ ] **Step 1: 积分 API**

```
GET  /api/v1/finance/points         — 积分余额
POST /api/v1/finance/points/convert — 积分转换（如红积分→可用积分）
GET  /api/v1/products/points        — 积分商品列表（base_price=0 的商品）
```

- [ ] **Step 2: 下单时积分抵扣逻辑**（修改 OrderService.createOrder）

- [ ] **Step 3: 前端 PointsExchange.vue + 入口**

- [ ] **Step 4: 编译验证**

---

### Task D3: 返利/分红

**Files:**
- Create: `server/src/main/java/com/ymdjk/module/finance/RebateService.java`
- Create: `web/src/views/finance/RebateLog.vue`

- [ ] **Step 1: 支付完成后触发返利**

```java
// OrderService.markPaid 完成后调用
public void distributeRebate(String orderNo, BigDecimal amount) {
    // 1. 查找用户上级
    // 2. 按 config 表返利比例计算
    // 3. 写入 ym_rebate 记录
    // 4. 更新用户余额
}
```

- [ ] **Step 2: 返利记录查询**

```
GET /api/v1/finance/rebate — 我的返利记录
```

- [ ] **Step 3: 编译验证**

---

## 执行顺序建议

```
Phase 1: Plan A (支付) — 最核心，完成交易闭环
Phase 2: Plan C Task C4 (系统设置) — 其他模块依赖配置
Phase 3: Plan B (用户功能) — 前台体验
Phase 4: Plan C Task C1-C3, C5 (后台 CRUD)
Phase 5: Plan D (财务积分) — 最复杂，最后做
```
