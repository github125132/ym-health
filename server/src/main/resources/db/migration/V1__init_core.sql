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
