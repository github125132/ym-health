# 有梦健康商城 — 新旧功能清单对比

> 旧项目: ASP.NET WebForms (56 前台页面 + 69 后台页面)
> 新项目: Spring Boot REST API + Vue 3 SPA
> 更新时间: 2026-06-13

---

## 1. 会员模块

| 功能 | 旧项目 (ASPX) | 新项目 | 状态 |
|------|--------------|--------|------|
| 登录 | login.aspx | POST /api/v1/auth/login + Login.vue | ✅ |
| 注册 | adduser.aspx | POST /api/v1/auth/register + Register.vue | ✅ |
| 忘记密码 | getpassword.aspx, getpassword2.aspx | POST /api/v1/auth/forgot-password + ForgotPassword.vue | ✅ |
| 退出登录 | logout.aspx | 前端清除 token | ✅ |
| 会员中心 | member.aspx | GET /api/v1/member/profile + Center.vue | ✅ |
| 修改密码 | repass.aspx | — | ❌ |
| 二级密码 | sepass.aspx | 字段 sepPassword 已存在 | ❌ 无 API |
| 编辑资料 | reinfo.aspx | — | ❌ |
| 收货地址 | mem_addr.aspx | ym_user_address 表已建 | ❌ 无 API/页面 |
| 邀请海报 | mem_haibao.aspx | — | ❌ |
| 我的团队 | myadduser.aspx | Team.vue | ⚠️ 页面骨架，无 API |

## 2. 商城模块

| 功能 | 旧项目 (ASPX) | 新项目 | 状态 |
|------|--------------|--------|------|
| 首页 | shop_main.aspx, default.aspx | Index.vue (商品+广告) | ✅ |
| 商品列表 | shop_prolist.aspx, shop_list.aspx | GET /api/v1/products + ProductList.vue | ✅ |
| 商品详情 | shop_promore.aspx | GET /api/v1/products/{id} + ProductDetail.vue | ✅ |
| 商品分类 | shopclass.aspx, shop_list_pro.aspx | GET /api/v1/products?categoryId= | ✅ |
| 积分商品 | shop_prolist_jifen.aspx | — | ❌ |
| 更多商品 | shop_listmore.aspx | 分页已实现 | ✅ |
| 收藏 | Favorites_add/del/list.aspx | ym_favorite 表已建 | ❌ 无 API/页面 |

## 3. 购物车 & 订单

| 功能 | 旧项目 (ASPX) | 新项目 | 状态 |
|------|--------------|--------|------|
| 购物车 | shop_cart.aspx | CRUD /api/v1/cart + Cart.vue | ✅ |
| 下单结算 | shop_cart.aspx (下单部分) | POST /api/v1/orders + 结算弹窗 | ✅ |
| 订单列表 | shop_orderlist.aspx | GET /api/v1/orders + OrderList.vue | ✅ |
| 订单详情 | shop_ordermore.aspx | GET /api/v1/orders/{no} + OrderDetail.vue | ✅ |
| 取消订单 | — | PUT /api/v1/orders/{no}/cancel | ✅ |
| 确认收货 | — | PUT /api/v1/orders/{no}/confirm | ✅ |
| 后台发货 | orderin.aspx | PUT /api/v1/orders/{no}/ship | ✅ |
| 后台改价 | orderalter.aspx | — | ❌ |
| 订单出库 | orderout.aspx | — | ❌ |

## 4. 支付模块

| 功能 | 旧项目 (ASPX) | 新项目 | 状态 |
|------|--------------|--------|------|
| 余额支付 | shop_cart.aspx (余额) | BalancePayStrategy | ✅ |
| 微信支付 | wxpay.aspx, wxpay_notify.aspx, gotowxpay.aspx | PaymentStrategy 接口 | ⚠️ 接口定义，SDK 未对接 |
| 支付宝 | alipay_Notify/Return_url.aspx | PaymentStrategy 接口 | ⚠️ 接口定义，SDK 未对接 |
| 支付回调 | pay_notify/Return_url.aspx | POST /api/v1/payment/notify/{channel} | ⚠️ 路由就绪，逻辑待实现 |

## 5. 财务 & 积分

| 功能 | 旧项目 (ASPX) | 新项目 | 状态 |
|------|--------------|--------|------|
| 财务流水 | paylog.aspx | GET /api/v1/finance/paylog + PayLog.vue | ✅ |
| 申请提现 | tixian.aspx | POST /api/v1/finance/withdraw + Withdraw.vue | ✅ |
| 提现记录 | tixianlog.aspx | GET /api/v1/finance/withdraw/list | ❌ |
| 充值 | usercz.aspx | — | ❌ |
| 积分兑换 | duijifen.aspx, duijifenlog.aspx | — | ❌ |
| 积分转换 | zhuanjifen.aspx, zhuanmoney.aspx | — | ❌ |
| 余额转换 | okmoneytonowmoney.aspx, fumoney.aspx | — | ❌ |
| 积分转爱瞳 | jifentoaitong.aspx, jifentoquan.aspx | — | ❌ |

## 6. 内容 & 广告

| 功能 | 旧项目 (ASPX) | 新项目 | 状态 |
|------|--------------|--------|------|
| 资讯列表 | infolist.aspx | GET /api/v1/contents | ✅ |
| 资讯详情 | info.aspx | — | ❌ |
| 广告位 | (shop_main.aspx 内联 ad.showad) | GET /api/v1/ads + AdController | ✅ |
| 留言反馈 | gbook.aspx, gbookmore.aspx, addmessage.aspx | ym_message 表已建 | ❌ 无 API/页面 |
| 排行榜 | paiming.aspx | — | ❌ |

## 7. 后台管理 (Admin)

| 功能 | 旧项目 (ASPX) | 新项目 | 状态 |
|------|--------------|--------|------|
| 后台登录 | admin/login.aspx | AdminLogin.vue + POST /api/v1/admin/login | ✅ |
| 仪表盘 | admin/tongji.aspx | Dashboard.vue (统计卡片) | ⚠️ 骨架，无真实数据 |
| 商品管理 | shop_prolist/add/adm/del/sh.aspx | AdminCrudController + ProductList.vue | ✅ |
| 分类管理 | site_class/add/alter/del.aspx | — | ❌ |
| 订单管理 | orderlist/alter/in/out.aspx | AdminCrudController + OrderList.vue | ✅ |
| 会员管理 | useradm/add/alter/del/act.aspx | AdminCrudController + MemberList.vue | ✅ |
| 财务管理 | finance.aspx, paylog.aspx | AdminCrudController + FinanceList.vue | ✅ |
| 提现管理 | tixianlist/sh/esc.aspx | AdminCrudController + WithdrawList.vue | ✅ |
| 积分管理 | jifenlist.aspx, jifen_sh.aspx | — | ❌ |
| 内容管理 | infolist/add/adm/del.aspx | AdminCrudController + ContentList.vue | ✅ |
| 广告管理 | imglist/add/adm/del.aspx | — | ❌ |
| 留言管理 | gbook.aspx, gbookre.aspx | — | ❌ |
| 管理员管理 | admin_add/edit/del/list.aspx | — | ❌ |
| 角色权限 | admin_role_add/edit/del/list.aspx | ym_role 表已建 | ❌ 无管理页面 |
| 系统设置 | sysset.aspx | Settings.vue | ⚠️ 骨架，无 API |
| 代理等级 | addlevel.aspx, addagent.aspx | — | ❌ |
| 返利分红 | fanlist.aspx, fanlilog.aspx, fenhong.aspx | ym_rebate 表已建 | ❌ |
| 佣金分配 | torder_s_more/state/adm.aspx | — | ❌ |
| 会员财务操作 | user_addmoney/jifen/usermoney/baodan.aspx | — | ❌ |
| 修改密码 | repass.aspx | — | ❌ |

## 8. 数据种子 & 测试

| 功能 | 旧项目 | 新项目 | 状态 |
|------|--------|--------|------|
| 管理员账号 | 数据库已有 | admin / admin123 (自动初始化) | ✅ |
| 商品数据 | 数据库已有 | 6 个演示商品 (自动初始化) | ✅ |
| 商品分类 | 数据库已有 | 4 个分类 (自动初始化) | ✅ |
| 广告数据 | 数据库已有 | 3 个广告位 (自动初始化) | ✅ |
| 系统配置 | config 表已有 | — | ❌ 配置数据 |
| 单元测试 | — | 5 个测试 (Result + MemberService) | ✅ |

## 覆盖率统计

| 分类 | 旧功能数 | 已实现 | 覆盖率 |
|------|---------|--------|--------|
| 前台功能 | ~56 | ~25 | ~45% |
| 后台功能 | ~69 | ~15 | ~22% |
| **合计** | **~125** | **~40** | **~32%** |

## 仍未实现的模块（按优先级）

**P1 高优先级缺失：**
- 微信支付 SDK 对接（核心交易闭环）
- 支付宝 SDK 对接
- 后台提现审核（通过/驳回）
- 后台发货功能（填写物流信息）

**P2 中优先级：**
- 收货地址管理（API + 页面）
- 收藏功能（API + 页面）
- 分类管理（后台 CRUD）
- 广告管理（后台 CRUD）
- 系统配置 API（提现门槛/手续费比例等）

**P3 低优先级：**
- 积分系统（兑换/转换/积分商品）
- 返利/分红系统
- 佣金分配
- 爱瞳 AI 对接
- 邀请海报生成
- 排行榜
- 留言反馈
- 二级密码
- 会员财务操作（后台加款/扣款）
