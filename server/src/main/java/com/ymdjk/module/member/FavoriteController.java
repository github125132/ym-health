package com.ymdjk.module.member;
import com.ymdjk.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;
    @GetMapping
    public Result<?> list(Authentication auth) { return Result.success(favoriteService.list(auth.getName())); }
    @PostMapping
    public Result<Void> add(Authentication auth, @RequestParam Integer productId) {
        favoriteService.add(auth.getName(), productId); return Result.success();
    }
    @DeleteMapping("/{productId}")
    public Result<Void> remove(Authentication auth, @PathVariable Integer productId) {
        favoriteService.remove(auth.getName(), productId); return Result.success();
    }
}
