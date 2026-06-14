INSERT INTO ym_config (`key`, value, remark) VALUES
('withdraw_min_amount', '100', '提现最低金额'),
('withdraw_fee_rate', '0.05', '提现手续费率'),
('point_exchange_rate', '1', '积分兑换比例(1积分=1元)'),
('rebate_level1', '0.10', '一级返利比例'),
('rebate_level2', '0.05', '二级返利比例'),
('site_title', '有梦健康', '站点标题')
ON DUPLICATE KEY UPDATE value = VALUES(value);
