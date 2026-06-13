package com.ymdjk.module.payment.strategy;

import java.math.BigDecimal;
import java.util.Map;

public interface PaymentStrategy {
    Map<String, String> pay(String userId, String orderNo, BigDecimal amount);
    String handleNotify(String payload);
}
