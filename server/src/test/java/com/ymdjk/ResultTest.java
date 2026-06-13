package com.ymdjk;

import com.ymdjk.common.Result;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    void testSuccessWithData() {
        Result<String> r = Result.success("hello");
        assertEquals(200, r.getCode());
        assertEquals("success", r.getMessage());
        assertEquals("hello", r.getData());
    }

    @Test
    void testSuccessWithoutData() {
        Result<Void> r = Result.success();
        assertEquals(200, r.getCode());
    }

    @Test
    void testError() {
        Result<Void> r = Result.error(400, "参数错误");
        assertEquals(400, r.getCode());
        assertEquals("参数错误", r.getMessage());
    }
}
