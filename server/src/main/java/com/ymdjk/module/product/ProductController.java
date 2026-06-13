package com.ymdjk.module.product;

import com.ymdjk.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "20") int pageSize,
                          Integer categoryId, String keywords) {
        return Result.success(productService.list(page, pageSize, categoryId, keywords));
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Integer id) {
        return Result.success(productService.detail(id));
    }
}
