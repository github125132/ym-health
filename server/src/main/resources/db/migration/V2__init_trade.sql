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
