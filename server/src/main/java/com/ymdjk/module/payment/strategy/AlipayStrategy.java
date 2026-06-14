package com.ymdjk.module.payment.strategy;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Component("alipayPayStrategy")
@RequiredArgsConstructor
public class AlipayStrategy implements PaymentStrategy {
    private final AlipayClient alipayClient;

    @Override
    public Map<String, String> pay(String userId, String orderNo, BigDecimal amount) {
        try {
            AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
            request.setNotifyUrl("/api/v1/payment/notify/alipay");
            request.setBizContent("{" +
                "\"out_trade_no\":\"" + orderNo + "\"," +
                "\"total_amount\":\"" + amount.setScale(2, BigDecimal.ROUND_HALF_UP) + "\"," +
                "\"subject\":\"有梦健康-商品购买\"," +
                "\"timeout_express\":\"30m\"}");

            AlipayTradePrecreateResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return Map.of("qrCode", response.getQrCode(), "status", "pending");
            }
            log.error("支付宝下单失败: {}", response.getSubMsg());
            throw new RuntimeException("支付宝下单失败: " + response.getSubMsg());
        } catch (Exception e) {
            throw new RuntimeException("支付宝支付调用失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String handleNotify(String payload) {
        try {
            log.info("支付宝回调接收: {}", payload);
            return "success";
        } catch (Exception e) {
            log.error("支付宝回调处理失败", e);
            return "failure";
        }
    }
}
