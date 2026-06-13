package com.ymdjk.module.member;

import com.ymdjk.common.JwtUtil;
import com.ymdjk.module.member.entity.Member;
import com.ymdjk.module.member.mapper.MemberMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberMapper memberMapper;

    @Test
    void testRegisterSuccess() {
        when(memberMapper.selectOne(any())).thenReturn(null);
        MemberService service = new MemberService(memberMapper, new BCryptPasswordEncoder(),
                new JwtUtil("test-secret-key-which-is-long-enough-for-hmac-256", 3600, 604800));
        service.register("13800138000", "123456", "测试", null);
        verify(memberMapper, times(1)).insert(any());
    }

    @Test
    void testRegisterDuplicatePhone() {
        when(memberMapper.selectOne(any())).thenReturn(new Member());
        MemberService service = new MemberService(memberMapper, new BCryptPasswordEncoder(),
                new JwtUtil("test-secret-key-which-is-long-enough-for-hmac-256", 3600, 604800));
        assertThrows(IllegalArgumentException.class,
                () -> service.register("13800138000", "123456", "测试", null));
    }
}
