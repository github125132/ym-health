package com.ymdjk.module.finance;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ymdjk.module.finance.entity.Rebate;
import com.ymdjk.module.finance.entity.UserBalance;
import com.ymdjk.module.finance.mapper.RebateMapper;
import com.ymdjk.module.finance.mapper.UserBalanceMapper;
import com.ymdjk.module.member.entity.Member;
import com.ymdjk.module.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RebateService {
    private final RebateMapper rebateMapper;
    private final MemberMapper memberMapper;
    private final UserBalanceMapper balanceMapper;

    public void distribute(String orderNo, String userId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return;
        Member member = memberMapper.selectOne(
                new LambdaQueryWrapper<Member>().eq(Member::getUserId, userId));
        if (member == null || member.getUpId() == null) return;

        giveRebate(orderNo, member.getUpId(), amount, new BigDecimal("0.10"), 1);

        Member upMember = memberMapper.selectOne(
                new LambdaQueryWrapper<Member>().eq(Member::getUserId, member.getUpId()));
        if (upMember != null && upMember.getUpId() != null) {
            giveRebate(orderNo, upMember.getUpId(), amount, new BigDecimal("0.05"), 2);
        }
    }

    private void giveRebate(String orderNo, String toUser, BigDecimal orderAmount, BigDecimal rate, int level) {
        BigDecimal rebateAmount = orderAmount.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP);
        if (rebateAmount.compareTo(BigDecimal.ZERO) <= 0) return;

        Rebate r = new Rebate();
        r.setOrderNo(orderNo);
        r.setToUser(toUser);
        r.setAmount(rebateAmount);
        r.setLevel(level);
        r.setCreatedAt(LocalDateTime.now());
        rebateMapper.insert(r);

        UserBalance ub = balanceMapper.selectOne(
                new LambdaQueryWrapper<UserBalance>().eq(UserBalance::getUserId, toUser));
        if (ub == null) {
            ub = new UserBalance();
            ub.setUserId(toUser);
            ub.setBalance(rebateAmount);
            balanceMapper.insert(ub);
        } else {
            ub.setBalance(ub.getBalance() != null ? ub.getBalance().add(rebateAmount) : rebateAmount);
            balanceMapper.updateById(ub);
        }
    }

    public List<Rebate> listByUser(String userId) {
        return rebateMapper.selectList(
                new LambdaQueryWrapper<Rebate>().eq(Rebate::getToUser, userId)
                        .orderByDesc(Rebate::getCreatedAt));
    }
}
