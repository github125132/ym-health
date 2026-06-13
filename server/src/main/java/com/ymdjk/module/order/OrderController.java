package com.ymdjk.module.order;

import com.ymdjk.common.Result;
import com.ymdjk.module.order.dto.CreateOrderRequest;
import com.ymdjk.module.order.dto.ShipRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public Result<?> create(Authentication auth, @Valid @RequestBody CreateOrderRequest req) {
        return Result.success(orderService.createOrder(auth.getName(), req));
    }

    @GetMapping
    public Result<?> list(Authentication auth,
                          @RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "20") int pageSize,
                          Integer status) {
        return Result.success(orderService.listOrders(auth.getName(), status, page, pageSize));
    }

    @GetMapping("/{orderNo}")
    public Result<?> detail(@PathVariable String orderNo) {
        return Result.success(orderService.getOrder(orderNo));
    }

    @PutMapping("/{orderNo}/cancel")
    public Result<Void> cancel(@PathVariable String orderNo) {
        orderService.cancelOrder(orderNo);
        return Result.success();
    }

    @PutMapping("/{orderNo}/ship")
    public Result<Void> ship(@PathVariable String orderNo, @Valid @RequestBody ShipRequest req) {
        orderService.shipOrder(orderNo, req);
        return Result.success();
    }

    @PutMapping("/{orderNo}/confirm")
    public Result<Void> confirm(@PathVariable String orderNo) {
        orderService.confirmReceipt(orderNo);
        return Result.success();
    }
}
