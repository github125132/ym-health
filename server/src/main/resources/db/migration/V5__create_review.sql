CREATE TABLE IF NOT EXISTS ym_review (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     VARCHAR(50) NOT NULL,
    product_id  INT NOT NULL,
    order_no    VARCHAR(50),
    rating      INT DEFAULT 5 COMMENT '1-5分',
    content     TEXT COMMENT '评价内容',
    images      VARCHAR(500) COMMENT '评价图片',
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_product (product_id),
    INDEX idx_user (user_id),
    INDEX idx_order (order_no)
) COMMENT '商品评价';
