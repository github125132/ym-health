package com.ymdjk.module.payment.strategy;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ymdjk.module.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component("wxpayPayStrategy")
@RequiredArgsConstructor
public class WxPayStrategy implements PaymentStrategy {
    private final WxPayService wxPayService;
    private final OrderMapper orderMapper;

    @Override
    public Map<String, String> pay(String userId, String orderNo, BigDecimal amount) {
        try {
            WxPayUnifiedOrderV3Request req = new WxPayUnifiedOrderV3Request();
            req.setOutTradeNo(orderNo);
            req.setDescription("有梦健康-商品购买");

            WxPayUnifiedOrderV3Request.Amount amountObj = new WxPayUnifiedOrderV3Request.Amount();
            amountObj.setTotal(amount.multiply(BigDecimal.valueOf(100)).intValue());
            amountObj.setCurrency("CNY");
            req.setAmount(amountObj);

            WxPayUnifiedOrderV3Request.SceneInfo sceneInfo = new WxPayUnifiedOrderV3Request.SceneInfo();
            sceneInfo.setPayerClientIp("127.0.0.1");
            req.setSceneInfo(sceneInfo);

            WxPayUnifiedOrderV3Result result = wxPayService.createOrderV3(TradeTypeEnum.NATIVE, req);
            return Map.of("codeUrl", result.getCodeUrl(), "status", "pending");
        } catch (Exception e) {
            throw new RuntimeException("微信支付调用失败: " + e.getMessage());
        }
    }

    @Override
    public String handleNotify(String payload) {
        try {
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }
}
