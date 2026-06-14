package com.ymdjk.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayConfig {

    @Bean
    public WxPayService wxPayService(@Value("${pay.wxpay.app-id:}") String appId,
                                      @Value("${pay.wxpay.mch-id:}") String mchId,
                                      @Value("${pay.wxpay.api-v3-key:}") String apiV3Key,
                                      @Value("${pay.wxpay.private-key-path:}") String privateKeyPath) {
        WxPayConfig config = new WxPayConfig();
        config.setAppId(appId);
        config.setMchId(mchId);
        config.setApiV3Key(apiV3Key);
        if (!privateKeyPath.isEmpty()) {
            config.setPrivateKeyPath(privateKeyPath);
        }
        WxPayService service = new WxPayServiceImpl();
        service.setConfig(config);
        return service;
    }

    @Bean
    public AlipayClient alipayClient(@Value("${pay.alipay.app-id:}") String appId,
                                      @Value("${pay.alipay.private-key:}") String privateKey,
                                      @Value("${pay.alipay.alipay-public-key:}") String alipayPublicKey) {
        return new DefaultAlipayClient(
            "https://openapi.alipay.com/gateway.do",
            appId, privateKey, "json", "UTF-8", alipayPublicKey, "RSA2");
    }
}
