package com.ymdjk.module.payment;

import com.ymdjk.common.Result;
import com.ymdjk.module.payment.strategy.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final Map<String, PaymentStrategy> strategies;

    @PostMapping("/{channel}")
    public Result<?> pay(Authentication auth, @PathVariable String channel,
                         @RequestParam String orderNo, @RequestParam BigDecimal amount) {
        PaymentStrategy strategy = strategies.get(channel + "PayStrategy");
        if (strategy == null) return Result.error(400, "不支持的支付方式");
        return Result.success(strategy.pay(auth.getName(), orderNo, amount));
    }

    @PostMapping("/notify/{channel}")
    public String notify(@PathVariable String channel, @RequestBody String payload) {
        PaymentStrategy strategy = strategies.get(channel + "PayStrategy");
        if (strategy == null) return "fail";
        return strategy.handleNotify(payload);
    }
}
