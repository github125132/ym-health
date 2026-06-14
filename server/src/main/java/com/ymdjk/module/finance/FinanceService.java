package com.ymdjk.module.finance;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ymdjk.common.PageResult;
import com.ymdjk.module.finance.entity.PayLog;
import com.ymdjk.module.finance.entity.Withdraw;
import com.ymdjk.module.finance.mapper.PayLogMapper;
import com.ymdjk.module.finance.mapper.WithdrawMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class FinanceService {
    private final PayLogMapper payLogMapper;
    private final WithdrawMapper withdrawMapper;
    public PageResult<PayLog> listPayLog(String userId, int page, int pageSize) {
        Page<PayLog> p = payLogMapper.selectPage(new Page<>(page, pageSize),
                new LambdaQueryWrapper<PayLog>().eq(PayLog::getUserId, userId).orderByDesc(PayLog::getCreatedAt));
        PageResult<PayLog> r = new PageResult<>();
        r.setRecords(p.getRecords()); r.setTotal(p.getTotal());
        r.setPage(p.getCurrent()); r.setPageSize(p.getSize());
        return r;
    }
    public void submitWithdraw(String userId, BigDecimal amount, String bankName, String bankCard) {
        Withdraw w = new Withdraw();
        w.setUserId(userId); w.setAmount(amount);
        w.setBankName(bankName); w.setBankCard(bankCard);
        w.setStatus(0); w.setCreatedAt(LocalDateTime.now());
        withdrawMapper.insert(w);
    }

    public List<Withdraw> listWithdraw(String userId) {
        return withdrawMapper.selectList(
            new LambdaQueryWrapper<Withdraw>().eq(Withdraw::getUserId, userId)
                .orderByDesc(Withdraw::getCreatedAt));
    }
}
