package com.ymdjk.module.order;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ymdjk.common.PageResult;
import com.ymdjk.module.cart.entity.Cart;
import com.ymdjk.module.cart.mapper.CartMapper;
import com.ymdjk.module.order.dto.CreateOrderRequest;
import com.ymdjk.module.order.dto.ShipRequest;
import com.ymdjk.module.order.entity.Order;
import com.ymdjk.module.order.entity.OrderItem;
import com.ymdjk.module.order.mapper.OrderItemMapper;
import com.ymdjk.module.order.mapper.OrderMapper;
import com.ymdjk.module.product.entity.Product;
import com.ymdjk.module.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    @Transactional
    public Order createOrder(String userId, CreateOrderRequest req) {
        List<Cart> cartItems = cartMapper.selectList(
                new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, userId));
        if (cartItems.isEmpty()) throw new IllegalArgumentException("购物车为空");

        String orderNo = "ORD" + System.currentTimeMillis();
        BigDecimal total = BigDecimal.ZERO;

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setOrderType(0);
        order.setOrderStatus(0);
        order.setPayStatus(0);
        order.setDeliveryStatus(0);
        order.setReceiverName(req.getReceiverName());
        order.setReceiverPhone(req.getReceiverPhone());
        order.setReceiverAddr(req.getReceiverAddr());
        order.setRemark(req.getRemark());
        order.setCreatedAt(LocalDateTime.now());
        orderMapper.insert(order);

        for (Cart cart : cartItems) {
            Product product = productMapper.selectById(cart.getProductId());
            BigDecimal price = product != null ? product.getBasePrice() : BigDecimal.ZERO;
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(cart.getQuantity()));
            total = total.add(subtotal);

            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setProductId(cart.getProductId());
            item.setProductName(product != null ? product.getTitle() : "");
            item.setProductImage(product != null ? product.getPurl() : "");
            item.setSpec(cart.getSpec());
            item.setPrice(price);
            item.setQuantity(cart.getQuantity());
            item.setSubtotal(subtotal);
            orderItemMapper.insert(item);
        }

        order.setTotalAmount(total);
        order.setPayAmount(total);
        orderMapper.updateById(order);

        cartMapper.delete(new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, userId));

        return order;
    }

    public PageResult<Order> listOrders(String userId, Integer status, int page, int pageSize) {
        LambdaQueryWrapper<Order> q = new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)
                .orderByDesc(Order::getCreatedAt);
        if (status != null) q.eq(Order::getOrderStatus, status);
        Page<Order> p = orderMapper.selectPage(new Page<>(page, pageSize), q);
        PageResult<Order> r = new PageResult<>();
        r.setRecords(p.getRecords());
        r.setTotal(p.getTotal());
        r.setPage(p.getCurrent());
        r.setPageSize(p.getSize());
        return r;
    }

    public Order getOrder(String orderNo) {
        return orderMapper.selectOne(
                new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, orderNo));
    }

    public void cancelOrder(String orderNo) {
        Order order = getOrder(orderNo);
        if (order == null) throw new IllegalArgumentException("订单不存在");
        if (order.getOrderStatus() != 0) throw new IllegalArgumentException("只能取消待付款订单");
        order.setOrderStatus(4);
        orderMapper.updateById(order);
    }

    public void shipOrder(String orderNo, ShipRequest req) {
        Order order = getOrder(orderNo);
        if (order == null) throw new IllegalArgumentException("订单不存在");
        if (order.getOrderStatus() != 1) throw new IllegalArgumentException("只能发货已付款订单");
        order.setOrderStatus(2);
        order.setDeliveryStatus(1);
        order.setExpressCompany(req.getExpressCompany());
        order.setExpressNo(req.getExpressNo());
        order.setShippedAt(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    public void confirmReceipt(String orderNo) {
        Order order = getOrder(orderNo);
        if (order == null) throw new IllegalArgumentException("订单不存在");
        if (order.getOrderStatus() != 2) throw new IllegalArgumentException("只能确认已发货订单");
        order.setOrderStatus(3);
        order.setDeliveryStatus(2);
        orderMapper.updateById(order);
    }
}
