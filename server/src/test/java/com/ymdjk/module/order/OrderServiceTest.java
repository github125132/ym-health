package com.ymdjk.module.order;

import com.ymdjk.module.cart.mapper.CartMapper;
import com.ymdjk.module.finance.RebateService;
import com.ymdjk.module.order.dto.CreateOrderRequest;
import com.ymdjk.module.order.mapper.OrderItemMapper;
import com.ymdjk.module.order.mapper.OrderMapper;
import com.ymdjk.module.product.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private CartMapper cartMapper;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private RebateService rebateService;

    @Test
    void testCreateOrderWithEmptyCart() {
        when(cartMapper.selectList(any())).thenReturn(List.of());
        OrderService service = new OrderService(orderMapper, orderItemMapper, cartMapper, productMapper, rebateService);
        assertThrows(IllegalArgumentException.class,
            () -> service.createOrder("test", new CreateOrderRequest()));
    }
}
