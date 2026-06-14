package com.ymdjk.module.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ymdjk.common.PageResult;
import com.ymdjk.module.order.entity.Order;
import com.ymdjk.module.order.entity.OrderItem;
import com.ymdjk.module.order.mapper.OrderItemMapper;
import com.ymdjk.module.order.mapper.OrderMapper;
import com.ymdjk.module.product.entity.Review;
import com.ymdjk.module.product.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewMapper reviewMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    public PageResult<Review> listByProduct(Integer productId, int page, int pageSize) {
        Page<Review> p = reviewMapper.selectPage(new Page<>(page, pageSize),
            new LambdaQueryWrapper<Review>()
                .eq(Review::getProductId, productId)
                .orderByDesc(Review::getCreatedAt));
        PageResult<Review> r = new PageResult<>();
        r.setRecords(p.getRecords());
        r.setTotal(p.getTotal());
        r.setPage(p.getCurrent());
        r.setPageSize(p.getSize());
        return r;
    }

    public void create(String userId, Integer productId, String orderNo, Integer rating, String content, String images) {
        Order order = orderMapper.selectOne(
            new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, orderNo));
        if (order == null) throw new IllegalArgumentException("订单不存在");
        if (!order.getUserId().equals(userId)) throw new IllegalArgumentException("无权评价该订单");
        if (order.getOrderStatus() < 2) throw new IllegalArgumentException("订单未完成，无法评价");

        long existCount = reviewMapper.selectCount(
            new LambdaQueryWrapper<Review>()
                .eq(Review::getOrderNo, orderNo)
                .eq(Review::getProductId, productId));
        if (existCount > 0) throw new IllegalArgumentException("该商品已评价");

        Review review = new Review();
        review.setUserId(userId);
        review.setProductId(productId);
        review.setOrderNo(orderNo);
        review.setRating(rating);
        review.setContent(content);
        review.setImages(images);
        review.setCreatedAt(LocalDateTime.now());
        reviewMapper.insert(review);
    }
}
