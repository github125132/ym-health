package com.ymdjk.module.finance;
import com.ymdjk.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
@RestController
@RequestMapping("/api/v1/finance")
@RequiredArgsConstructor
public class FinanceController {
    private final FinanceService financeService;
    @GetMapping("/paylog")
    public Result<?> payLog(Authentication auth,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "20") int pageSize) {
        return Result.success(financeService.listPayLog(auth.getName(), page, pageSize));
    }
    @PostMapping("/withdraw")
    public Result<Void> withdraw(Authentication auth,
                                 @RequestParam BigDecimal amount,
                                 @RequestParam String bankName,
                                 @RequestParam String bankCard) {
        financeService.submitWithdraw(auth.getName(), amount, bankName, bankCard);
        return Result.success();
    }

    @GetMapping("/withdraw/list")
    public Result<?> withdrawList(Authentication auth) {
        return Result.success(financeService.listWithdraw(auth.getName()));
    }
}
