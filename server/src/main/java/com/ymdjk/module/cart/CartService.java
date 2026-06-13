package com.ymdjk.module.cart;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ymdjk.module.cart.entity.Cart;
import com.ymdjk.module.cart.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartMapper cartMapper;

    public List<Cart> list(String userId) {
        return cartMapper.selectList(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId));
    }

    public void add(String userId, Integer productId, Integer quantity, String spec) {
        Cart existing = cartMapper.selectOne(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId).eq(Cart::getProductId, productId)
                .eq(spec != null, Cart::getSpec, spec));
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            cartMapper.updateById(existing);
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(quantity);
            cart.setSpec(spec);
            cartMapper.insert(cart);
        }
    }

    public void updateQuantity(Integer id, Integer quantity) {
        Cart cart = cartMapper.selectById(id);
        if (cart != null) { cart.setQuantity(quantity); cartMapper.updateById(cart); }
    }

    public void remove(Integer id) { cartMapper.deleteById(id); }
}
