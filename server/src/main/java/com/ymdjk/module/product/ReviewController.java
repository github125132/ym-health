package com.ymdjk.module.product;

import com.ymdjk.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/api/v1/reviews")
    public Result<?> list(@RequestParam Integer productId,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "20") int pageSize) {
        return Result.success(reviewService.listByProduct(productId, page, pageSize));
    }

    @PostMapping("/api/v1/reviews")
    public Result<Void> create(Authentication auth,
                                @RequestParam Integer productId,
                                @RequestParam String orderNo,
                                @RequestParam(defaultValue = "5") Integer rating,
                                String content, String images) {
        reviewService.create(auth.getName(), productId, orderNo, rating, content, images);
        return Result.success();
    }
}
