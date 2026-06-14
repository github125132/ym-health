-- 生成更多演示用户
INSERT IGNORE INTO ym_user (user_id, phone, password, real_name, user_level, up_id, add_date) VALUES
('M001', '13800001001', '$2a$10$dummyhash', '张三', 1, NULL, NOW()),
('M002', '13800001002', '$2a$10$dummyhash', '李四', 0, 'M001', NOW()),
('M003', '13800001003', '$2a$10$dummyhash', '王五', 0, 'M001', NOW()),
('M004', '13800001004', '$2a$10$dummyhash', '赵六', 2, NULL, NOW()),
('M005', '13800001005', '$2a$10$dummyhash', '孙七', 0, 'M004', NOW());

INSERT IGNORE INTO ym_user_balance (user_id, balance, points) VALUES
('M001', 1000.00, 500),
('M002', 500.00, 200),
('M003', 300.00, 100),
('M004', 2000.00, 1000),
('M005', 100.00, 50);

INSERT IGNORE INTO ym_user_stats (user_id, team_count) VALUES
('M001', 2), ('M004', 1);

-- 更多商品
INSERT IGNORE INTO ym_product (title, category_id, purl, base_price, stock, status, sort_order, created_at) VALUES
('智能体脂秤', 4, '/img/default.png', 129.00, 80, 1, 5, NOW()),
('颈椎按摩仪', 4, '/img/default.png', 199.00, 60, 1, 6, NOW()),
('有机燕麦片', 1, '/img/default.png', 38.00, 200, 1, 7, NOW()),
('便携榨汁杯', 4, '/img/default.png', 79.00, 120, 1, 8, NOW());

-- 演示订单
INSERT IGNORE INTO ym_order (order_no, user_id, total_amount, pay_amount, pay_type, order_status, pay_status, delivery_status, receiver_name, receiver_phone, receiver_addr, created_at) VALUES
('DEMO20260613001', 'M001', 129.00, 129.00, 1, 3, 1, 2, '张三', '13800001001', '北京市朝阳区', DATE_SUB(NOW(), INTERVAL 5 DAY)),
('DEMO20260613002', 'M001', 89.00, 89.00, 0, 1, 1, 0, '张三', '13800001001', '北京市朝阳区', DATE_SUB(NOW(), INTERVAL 1 DAY)),
('DEMO20260613003', 'M002', 199.00, 199.00, 1, 0, 0, 0, '李四', '13800001002', '上海市浦东新区', NOW());

INSERT IGNORE INTO ym_order_item (order_id, product_id, product_name, price, quantity, subtotal) VALUES
(1, 1, '有机红枣礼盒', 59.90, 1, 59.90),
(1, 2, '五谷杂粮营养餐', 69.10, 1, 69.10),
(2, 1, '有机红枣礼盒', 59.90, 1, 59.90),
(2, 5, '天然蜂蜜500g', 29.10, 1, 29.10),
(3, 7, '颈椎按摩仪', 199.00, 1, 199.00);

-- 演示评价
INSERT IGNORE INTO ym_review (user_id, product_id, order_no, rating, content, created_at) VALUES
('M001', 1, 'DEMO20260613001', 5, '品质很好，值得购买', DATE_SUB(NOW(), INTERVAL 3 DAY)),
('M001', 2, 'DEMO20260613001', 4, '味道不错', DATE_SUB(NOW(), INTERVAL 3 DAY));

-- 演示财务流水
INSERT IGNORE INTO ym_pay_log (user_id, order_no, amount, pay_type, description, created_at) VALUES
('M001', 'DEMO20260613001', 129.00, 2, '订单支付', DATE_SUB(NOW(), INTERVAL 5 DAY)),
('M001', 'DEMO20260613002', 89.00, 2, '订单支付', DATE_SUB(NOW(), INTERVAL 1 DAY));

-- 演示提现
INSERT IGNORE INTO ym_withdraw (user_id, amount, bank_name, bank_card, status, created_at) VALUES
('M001', 200.00, '中国银行', '6222****1234', 0, DATE_SUB(NOW(), INTERVAL 2 DAY));

-- 演示广告（使用已有图片资源）
INSERT IGNORE INTO ym_ad (position, title, image_url, link_url, sort_order, status, created_at) VALUES
('shouye', '大健康产品甄选', '/img/banner.jpg', '/products', 1, 1, NOW()),
('shouye', '限时特惠', '/img/ban01.jpg', '/products', 2, 1, NOW()),
('shouye', '新品上市', '/img/yuchi1.jpg', '/products', 3, 1, NOW());

-- 配置数据
INSERT IGNORE INTO ym_config (`key`, value, remark) VALUES
('withdraw_min_amount', '100', '提现最低金额'),
('withdraw_fee_rate', '0.05', '提现手续费率'),
('rebate_level1', '0.10', '一级返利比例'),
('rebate_level2', '0.05', '二级返利比例')
ON DUPLICATE KEY UPDATE value = VALUES(value);
