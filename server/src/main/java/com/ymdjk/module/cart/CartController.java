package com.ymdjk.module.cart;

import com.ymdjk.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public Result<?> list(Authentication auth) {
        return Result.success(cartService.list(auth.getName()));
    }

    @PostMapping
    public Result<Void> add(Authentication auth, @RequestParam Integer productId,
                            @RequestParam(defaultValue = "1") Integer quantity, String spec) {
        cartService.add(auth.getName(), productId, quantity, spec);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Integer id, @RequestParam Integer quantity) {
        cartService.updateQuantity(id, quantity);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Integer id) {
        cartService.remove(id);
        return Result.success();
    }
}
