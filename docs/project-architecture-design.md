# 有梦健康移动商城项目架构与设计说明

## 1. 文档范围

本文档基于当前站点目录 `m.ymdjk.net` 的可见文件、页面声明、配置文件、静态资源和 `bin` 目录程序集信息整理。当前仓库更接近 IIS 部署产物，而不是完整源码工程：

- 大量页面声明了 `CodeBehind="*.aspx.cs"`，但对应 `.aspx.cs` 源文件未随站点提交。
- 后端业务逻辑已编译进 `bin/web.dll`，并带有 `web.pdb` 调试符号。
- 文档中涉及后端类、工具类、支付类和部分业务流程时，来源包括页面控件绑定、URL、程序集符号和配置推断。

## 2. 项目定位

项目是一个基于 ASP.NET WebForms 的移动端会员商城/健康服务系统，品牌名来自配置 `sitetitle=有梦健康`。

核心能力包括：

- 移动端商城：首页、商品列表、商品详情、购物车、订单、支付。
- 会员系统：注册、登录、会员中心、资料维护、团队/邀请、积分、余额、财务明细、提现。
- AI 健康检测入口：前台页包含“中医AI健康检测”“成为AI健康管家”等业务入口，并集成 `aitong` 子目录页面。
- 后台管理：商品、分类、订单、会员、财务、提现、广告、系统参数、管理员与角色权限。
- 支付与通知：微信支付、支付宝支付、余额支付，以及支付回调处理。
- 内容管理：公告/课堂/信息、广告位、留言客服。

## 3. 技术栈

### 后端

- ASP.NET WebForms，页面扩展名为 `.aspx`，处理器为 `.ashx`，用户控件为 `.ascx`。
- .NET Framework 4.5，见 `Web.config` 中 `targetFramework="4.5"`。
- 业务程序集：`bin/web.dll`。
- 数据库工具：`bin/cdl.DBUtility.dll`，符号显示包含 `Maticsoft.DBUtility.DbHelperSQL`、`SqlHelper`、`DbHelperOleDb` 等。
- 数据库：SQL Server，`Web.config` 使用 `ConnectionString` 指向 `database=youmengjiankang`。

### 前端

- jQuery、AmazeUI、Bootstrap、Font Awesome、layer、lazyload。
- 后台使用 Bootstrap 风格管理框架，左侧导航 + iframe 内容区。
- 移动端采用 rem 适配和多套 CSS：`css/app.css`、`css/app2.css`、`css/style2.css`、`css/index.css` 等。

### 第三方能力

- 微信 JS SDK：`res.wx.qq.com/open/js/jweixin-*`。
- 微信支付：`wxpay.aspx`、`wxpay_notify.aspx`、`wxpay/End_orwx.aspx`、`wxpay/End_orwxout.aspx`、`wxpay/wxcx.aspx`，DLL 符号中有 `wxpayconfig/*`。
- 支付宝：`AopSdk.dll`，页面有 `alipay_Notify_url.aspx`、`alipay_Return_url.aspx`。
- 阿里云短信：`AlibabaCloud.SDK.Dysmsapi20170525.dll`、`MobileSMS.dll`，注册/找回密码页面有手机验证码流程。
- UEditor：`ueditor/`，负责富文本和文件上传。
- 二维码：`ThoughtWorks.QRCode.dll`、`qrcode.dll`，用于邀请海报/二维码。
- Excel 导出：`Excel.dll`、`COM.Excel.dll`，后台多个列表有导出按钮。

## 4. 运行与部署结构

```text
m.ymdjk.net/
├─ Web.config                 # 站点配置、数据库连接、站点标题、二维码参数
├─ *.aspx / *.ashx / *.ascx   # 前台移动端页面、API 处理器、公共控件
├─ admin_ymdjk/               # 后台管理系统
├─ aitong/                    # AI 健康相关跳转/积分查询页面
├─ wxpay/                     # 微信支付相关页面
├─ ueditor/                   # 富文本编辑器和上传处理
├─ bin/                       # 编译后的业务 DLL 与第三方依赖
├─ css/ js/ img/ images/ i/   # 前台静态资源
├─ upload/                    # 用户上传和商品/内容图片
├─ ReportFile/                # 导出报表文件
├─ logs/                      # 运行日志
└─ appdown/                   # App 下载页和安装包
```

部署模型是典型 IIS 站点部署：

1. IIS 将根目录配置为 Web 应用。
2. ASP.NET 编译 `.aspx` 页面，页面后端类型从 `bin/web.dll` 加载。
3. 页面通过 Session、Request、Server Controls 和 `DbHelperSQL` 访问业务逻辑与数据库。
4. 上传、导出、日志写入站点目录下的 `upload/`、`ReportFile/`、`logs/`。

## 5. 分层架构

项目没有显式的现代分层目录，但从文件形态看可以归纳为以下逻辑层：

### 表现层

- 前台页面：根目录 `.aspx`，面向移动端用户。
- 后台页面：`admin_ymdjk/*.aspx`，面向管理员。
- 公共控件：`top.ascx`、`shop_foot.ascx`。
- 静态资源：`css/`、`js/`、`img/`、`images/`、字体目录。

### 页面控制层

- 每个页面的 `CodeBehind` 类处理页面生命周期、按钮事件、数据绑定和跳转。
- 典型事件：`Button1_Click`、`Button2_Click`、`GridView1_PageIndexChanging`、`uporder_Click`。
- API 型入口使用 `.ashx`：`api_miniprogram_getopenid.ashx`、`api_order_return.ashx`、`api_updateUserMoney.ashx`、`get_xianzhi.ashx`。

### 业务服务层

主要封装在 `bin/web.dll`。根据页面引用和 DLL 符号，可见的业务类/静态工具包括：

- `web.sitepublic`：会员等级、用户类型等展示转换。
- `web.ad`：广告位输出，例如 `web.ad.showad("shouye")`。
- `web.Favorites`：收藏状态判断与收藏操作。
- `shop.Web.cartclass`：购物车相关模型或工具。
- 微信支付配置/接口类：`WxPayApi`、`JsApiPay`、`NativePay`、`OrderQuery`、`Refund`、`Notify` 等。
- 通用工具：`httpclass`、`AES`、`Log` 等。

### 数据访问层

- 由 `cdl.DBUtility.dll` 提供，命名空间符号显示为 `Maticsoft.DBUtility`。
- 主要是 SQL Server 直连工具类，如 `DbHelperSQL`、`SqlHelper`。
- 连接串从 `Web.config` 的 `appSettings.ConnectionString` 读取。

### 数据存储层

- 主库：SQL Server 数据库 `youmengjiankang`。
- 文件存储：站点本地目录 `upload/`、`ueditor` 上传目录、二维码图片目录 `erweicode/`、报表目录 `ReportFile/`。
- 日志：`logs/*.log`。

## 6. 核心模块设计

### 6.1 前台会员模块

相关页面：

- `login.aspx`：会员登录。
- `adduser.aspx`：会员注册，包含手机号、验证码、密码、真实姓名、推荐人。
- `getpassword.aspx`、`getpassword2.aspx`：找回密码。
- `member.aspx`：会员中心。
- `reinfo.aspx`：资料维护。
- `repass.aspx`、`sepass.aspx`：密码/二级密码相关。
- `myadduser.aspx`：团队列表。
- `mem_haibao.aspx`：邀请海报。
- `mem_addr.aspx`：收货地址。

设计特点：

- 以 Session 保存登录态，页面中多处读取 `Session["adminusername"]`、`Session["userlevel"]`。
- 注册流程依赖短信验证码，后端由 `MobileSMS.dll` 或阿里云短信 SDK 支撑。
- 会员等级用于价格和权限展示，例如商品价格根据 `userlevel` 读取不同价格字段。
- 邀请/团队关系通过推荐人、上级/节点人字段维护，页面字段包括 `User_recommend_id`、`User_up_id`。

### 6.2 商城模块

相关页面：

- `shop_main.aspx`：商城首页。
- `shop_prolist.aspx`、`shop_prolist_jifen.aspx`、`shop_list_pro.aspx`：商品列表。
- `shop_promore.aspx`：商品详情。
- `shop_cart.aspx`：购物车和下单。
- `shop_orderlist.aspx`、`shop_ordermore.aspx`：用户订单列表和详情。
- `Favorites_add.aspx`、`Favorites_del.aspx`、`Favorites_list.aspx`：收藏。
- `shop_foot.ascx`：底部导航。

设计特点：

- 页面使用 `Repeater`、`DataList`、`GridView` 绑定商品和订单数据。
- 商品详情页支持规格选择、收藏、加入购物车、立即购买。
- 购物车页支持数量加减、删除、地址选择、备注、优惠券/积分逻辑、余额/微信/支付宝支付入口。
- 商品价格字段有多档，例如页面中出现 `p0`、`p1` 或 `price0`、`price1`、`weixin`、`jifen`。
- 首页商品分区包括“成为AI健康管家”“大健康产品甄选”等业务栏目。

### 6.3 订单与支付模块

相关页面：

- `shop_cart.aspx`：提交订单。
- `gotowxpay.aspx`、`wxpay.aspx`、`wxpay_notify.aspx`：微信支付。
- `wxpay/End_orwx.aspx`、`wxpay/End_orwxout.aspx`、`wxpay/wxcx.aspx`：微信支付扩展入口。
- `alipay_Notify_url.aspx`、`alipay_Return_url.aspx`：支付宝通知和同步返回。
- `pay_notify_url.aspx`、`pay_return_url.aspx`：另一套支付通知/返回。
- `api_order_return.ashx`：订单回调类 API。

支付方式：

- 余额支付：前端 `paytype=0`。
- 微信支付：前端 `paytype=1`。
- 支付宝支付：前端 `paytype=2`。

设计特点：

- 订单创建后根据支付方式跳转到对应支付页或直接扣余额。
- 支付回调页面负责验签、更新订单支付状态、记财务流水。
- 微信支付类在 DLL 符号中较完整，包含统一下单、查询订单、关闭订单、退款、通知等。
- 小程序环境下 `wxpay.aspx` 将 JSAPI 支付参数转成路由参数，跳转到小程序支付页。

### 6.4 财务、积分和提现模块

相关页面：

- `paylog.aspx`：用户财务明细。
- `tixian.aspx`、`tixianlog.aspx`：申请提现和提现记录。
- `duijifen.aspx`、`duijifenlog.aspx`：积分兑换。
- `jifentoaitong.aspx`、`jifentoquan.aspx`、`zhuanjifen.aspx`、`zhuanmoney.aspx`、`okmoneytonowmoney.aspx`：积分/余额/券转换。
- `usercz.aspx`：充值。

后台相关页面：

- `admin_ymdjk/paylog.aspx`：财务明细管理。
- `admin_ymdjk/tixianlist.aspx`：提现审核、导出、一键审核。
- `admin_ymdjk/tixian_sh.aspx`、`admin_ymdjk/tixian_esc.aspx`：提现通过/驳回。
- `admin_ymdjk/sysset.aspx`：提现起提金额、手续费、积分比例、奖励比例等参数。

设计特点：

- 财务流水统一通过 `paylog` 类页面和后台明细查看。
- 系统参数页维护奖励比例、积分比例、合伙人分红、代理奖励、提现规则。
- 后台对提现支持筛选、导出和批量处理。

### 6.5 内容与广告模块

相关页面：

- 前台：`infolist.aspx`、`info.aspx`。
- 后台：`admin_ymdjk/infolist.aspx`、`infoadd.aspx`、`infoadm.aspx`、`infodel.aspx`。
- 广告/图片：`admin_ymdjk/imglist.aspx`、`imgadd.aspx`、`imgadm.aspx`、`imgdel.aspx`。
- 留言客服：`gbook.aspx`、`gbookmore.aspx`、`admin_ymdjk/gbook.aspx`、`gbookre.aspx`。

设计特点：

- 首页广告通过 `web.ad.showad("shouye")` 输出。
- 内容编辑依赖 UEditor。
- 上传文件默认保存在 `/upload/image/{yyyy}{mm}{dd}/...`。

### 6.6 后台管理模块

入口与框架：

- `admin_ymdjk/login.aspx`：后台登录。
- `admin_ymdjk/default.aspx`：后台主框架。
- `admin_ymdjk/tongji.aspx`：后台首页统计。
- `admin_ymdjk/logout.aspx`：退出。

功能菜单：

- 信息管理：新闻/公告/课堂内容。
- 商城管理：商品、分类。
- 商城订单管理：订单、待付款、待发货、已完成。
- 会员管理：会员、留言。
- 财务管理：财务明细、提现申请。
- 系统设置：系统参数、管理员、角色、广告。

权限设计：

- `admin_role_add.aspx` / `admin_role_edit.aspx` 使用 `CheckBoxList` 维护权限点。
- 权限点的值直接对应后台主菜单 DOM ID，例如 `b11`、`a11`、`b12`、`a21`。
- `admin_ymdjk/default.aspx` 中菜单项默认 `visible="false"`，后端根据角色权限控制显示。

### 6.7 AI 健康/爱瞳模块

相关页面：

- `aitonglogin.aspx`
- `aitong/aitonglogin.aspx`
- `aitong/searchremainingPoint.aspx`
- `aitong/jifentoaitongremainingPoint.aspx`
- `jifentoaitong.aspx`

设计特点：

- 前台 `member.aspx` 和 `shop_main.aspx` 提供健康报告/AI 检测入口。
- 页面命名显示存在积分兑换到爱瞳系统、剩余点数查询等能力。
- 这部分大概率是外部健康检测系统的单点登录/积分接口适配层。

## 7. 关键业务流程

### 7.1 用户注册登录

1. 用户访问 `adduser.aspx`。
2. 输入手机号、验证码、密码、真实姓名、推荐人。
3. 点击获取验证码，后端触发短信发送。
4. 点击注册，后端校验验证码、推荐关系、手机号唯一性并写入会员表。
5. 用户通过 `login.aspx` 登录，登录态写入 Session。
6. 登录后进入 `member.aspx` 或商城首页。

### 7.2 商品购买

1. 用户进入 `shop_main.aspx` 或 `shop_prolist.aspx` 浏览商品。
2. 在 `shop_promore.aspx` 选择商品/规格。
3. 点击加入购物车或立即购买，后端加入购物车或创建临时购买记录。
4. 在 `shop_cart.aspx` 确认商品、数量、地址、备注和支付方式。
5. 余额支付直接扣款并更新订单；微信/支付宝跳转支付。
6. 支付回调更新订单状态和财务流水。
7. 后台在 `admin_ymdjk/orderlist.aspx` 发货/处理订单。
8. 用户在 `shop_orderlist.aspx` 查看订单状态。

### 7.3 提现审核

1. 用户在 `tixian.aspx` 提交提现申请。
2. 系统根据 `sysset.aspx` 配置的起提金额、手续费等规则计算。
3. 申请记录进入后台 `tixianlist.aspx`。
4. 管理员审核通过或驳回。
5. 系统更新提现状态，必要时更新余额/财务流水。

### 7.4 后台角色权限

1. 超级管理员在 `admin_role_add.aspx` 新增角色。
2. 勾选权限点，例如商品管理、订单管理、会员管理。
3. 管理员账号绑定角色。
4. 登录后台后，`default.aspx` 根据权限点决定菜单是否显示。

## 8. 主要数据对象推断

当前没有数据库脚本。根据页面字段和控件绑定，可推断核心数据对象如下：

- 用户/会员：`userid`、手机号、真实姓名、密码、推荐人、上级、会员等级、合伙人等级、余额、积分、银行卡/支付宝/微信收款信息、地址。
- 商品：`id`、标题、图片、详情、分类、库存、上下架状态、排序、价格档位、积分价。
- 分类：商品分类或内容分类，支持排序。
- 购物车：用户、商品、数量、规格、价格、积分。
- 订单：订单号、用户、商品明细、收货信息、金额、积分、支付方式、支付状态、发货/收货状态。
- 财务流水：用户、金额、类型、说明、时间。
- 提现申请：用户、金额、手续费、状态、收款方式、审核时间。
- 内容信息：标题、摘要、图片、正文、分类、发布时间。
- 广告：广告位、图片、链接、排序。
- 收藏：收藏类型、目标 ID、用户、时间。
- 管理员/角色：账号、密码、角色、权限点。

## 9. 配置说明

`Web.config` 关键配置：

- `ConnectionString`：SQL Server 连接串。
- `sitetitle`：站点标题。
- `payid`、`paykey`：支付相关配置。
- `qr_weburl`、`qr_wh`、`qr_x`、`qr_y`：邀请二维码/海报生成参数。
- `headphoto_x`、`headphoto_y`、`name_x`、`name_y` 等：海报头像和昵称绘制参数。
- `httpRuntime maxRequestLength="1048576"`：请求体上限较大，用于上传。
- `pages validateRequest="false"`：关闭请求验证，便于富文本提交，但需要后端自行做 XSS 和危险输入防护。

`ueditor/net/config.json` 关键配置：

- 图片上传目录：`/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}`。
- 视频上传目录：`/upload/video/...`。
- 文件上传目录：`/upload/file/...`。
- 图片限制约 2MB，视频约 100MB，普通文件约 50MB。

## 10. 安全与维护风险

以下是从配置和页面形态可直接观察到的风险：

- `Web.config` 明文保存数据库账号、密码和支付密钥。
- `compilation debug="true"`，生产环境不应开启。
- `validateRequest="false"`，富文本场景可以理解，但所有输出点必须做 HTML 编码或白名单过滤。
- 多处前端 JS 只做客户端校验，关键校验必须由后端重复执行。
- 当前站点目录包含上传文件、日志、导出报表和 DLL，权限边界需要由 IIS 严格控制。
- 业务源码缺失，问题定位、审计和重构成本较高。
- 页面中可见硬编码微信 AppID/Secret 片段和旧域名文案，应统一迁移到配置。
- `.aspx` 页面存在大量内联样式和旧版脚本，长期维护成本高。

## 11. 建议的后续整理方向

1. 补齐完整源码：至少恢复 `*.aspx.cs`、`class/`、`wxpayconfig/` 等源文件，避免只依赖 DLL。
2. 建立数据库脚本：导出表结构、索引、存储过程和基础配置数据。
3. 分离配置和密钥：使用环境变量、IIS 配置变换或密钥管理，不在仓库保留生产密钥。
4. 关闭生产调试：`debug=false`，并配置统一错误页。
5. 加固上传：限制可执行扩展、设置上传目录无脚本执行权限、校验 MIME 和文件头。
6. 梳理支付幂等：支付回调必须按订单号/交易号做幂等更新。
7. 增加日志规范：支付、提现、余额、积分变更应有可追踪审计日志。
8. 逐步模块化：先把支付、订单、会员、财务这类高风险逻辑从页面事件中抽成服务类。

