package com.ymdjk.module.payment.strategy;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ymdjk.module.finance.entity.PayLog;
import com.ymdjk.module.finance.mapper.PayLogMapper;
import com.ymdjk.module.order.entity.Order;
import com.ymdjk.module.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Component("balancePayStrategy")
@RequiredArgsConstructor
public class BalancePayStrategy implements PaymentStrategy {
    private final OrderMapper orderMapper;
    private final PayLogMapper payLogMapper;

    @Override
    public Map<String, String> pay(String userId, String orderNo, BigDecimal amount) {
        Order order = orderMapper.selectOne(
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getOrderNo, orderNo));
        if (order == null) throw new IllegalArgumentException("订单不存在");

        order.setPayStatus(1);
        order.setOrderStatus(1);
        order.setPaidAt(LocalDateTime.now());
        orderMapper.updateById(order);

        PayLog log = new PayLog();
        log.setUserId(userId);
        log.setOrderNo(orderNo);
        log.setAmount(amount);
        log.setPayType(2);
        log.setDescription("余额支付");
        log.setStatus(1);
        payLogMapper.insert(log);

        return Map.of("status", "success");
    }

    @Override
    public String handleNotify(String payload) { return "success"; }
}
