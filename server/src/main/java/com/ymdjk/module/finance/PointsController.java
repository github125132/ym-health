package com.ymdjk.module.finance;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ymdjk.common.Result;
import com.ymdjk.module.finance.entity.UserBalance;
import com.ymdjk.module.finance.mapper.UserBalanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/finance")
@RequiredArgsConstructor
public class PointsController {
    private final UserBalanceMapper balanceMapper;

    @GetMapping("/points")
    public Result<?> points(Authentication auth) {
        UserBalance ub = balanceMapper.selectOne(
                new LambdaQueryWrapper<UserBalance>().eq(UserBalance::getUserId, auth.getName()));
        return Result.success(ub != null ? ub : new UserBalance());
    }

    @PostMapping("/points/convert")
    public Result<Void> convertPoints(Authentication auth, @RequestParam BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("转换数量必须大于0");
        }
        UserBalance ub = balanceMapper.selectOne(
                new LambdaQueryWrapper<UserBalance>().eq(UserBalance::getUserId, auth.getName()));
        if (ub == null || ub.getPoints() == null || ub.getPoints().compareTo(amount) < 0) {
            throw new IllegalArgumentException("积分不足");
        }
        ub.setPoints(ub.getPoints().subtract(amount));
        ub.setBalance(ub.getBalance() != null ? ub.getBalance().add(amount) : amount);
        balanceMapper.updateById(ub);
        return Result.success();
    }
}
