package com.ymdjk.module.member;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ymdjk.common.JwtUtil;
import com.ymdjk.module.member.entity.Member;
import com.ymdjk.module.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Member findByUserId(String userId) {
        return memberMapper.selectOne(new LambdaQueryWrapper<Member>()
                .eq(Member::getUserId, userId));
    }

    public Member findByPhone(String phone) {
        return memberMapper.selectOne(new LambdaQueryWrapper<Member>()
                .eq(Member::getPhone, phone));
    }

    public Map<String, String> login(String phone, String rawPassword) {
        Member member = findByPhone(phone);
        if (member == null || !passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new IllegalArgumentException("手机号或密码错误");
        }
        String accessToken = jwtUtil.generateAccessToken(member.getUserId(), "MEMBER");
        String refreshToken = jwtUtil.generateRefreshToken(member.getUserId());
        Map<String, String> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        return result;
    }

    public void register(String phone, String password, String realName, String recommendId) {
        if (findByPhone(phone) != null) {
            throw new IllegalArgumentException("手机号已注册");
        }
        Member member = new Member();
        member.setUserId("M" + System.currentTimeMillis());
        member.setPhone(phone);
        member.setPassword(passwordEncoder.encode(password));
        member.setRealName(realName);
        member.setRecommendId(recommendId);
        member.setAddDate(java.time.LocalDateTime.now());
        memberMapper.insert(member);
    }

    public void resetPassword(String phone) {
        Member member = findByPhone(phone);
        if (member == null) throw new IllegalArgumentException("手机号未注册");
        member.setPassword(passwordEncoder.encode("123456"));
        memberMapper.updateById(member);
    }

    public void changePassword(String userId, String oldPassword, String newPassword) {
        Member member = findByUserId(userId);
        if (member == null) throw new IllegalArgumentException("用户不存在");
        if (!passwordEncoder.matches(oldPassword, member.getPassword())) throw new IllegalArgumentException("旧密码错误");
        member.setPassword(passwordEncoder.encode(newPassword));
        memberMapper.updateById(member);
    }
}
