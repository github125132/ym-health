package com.ymdjk.module.member;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ymdjk.module.member.entity.Favorite;
import com.ymdjk.module.member.mapper.FavoriteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteMapper favoriteMapper;
    public List<Favorite> list(String userId) {
        return favoriteMapper.selectList(new LambdaQueryWrapper<Favorite>().eq(Favorite::getUserId, userId));
    }
    public void add(String userId, Integer productId) {
        Favorite existing = favoriteMapper.selectOne(new LambdaQueryWrapper<Favorite>()
            .eq(Favorite::getUserId, userId).eq(Favorite::getTargetId, productId));
        if (existing != null) return;
        Favorite fav = new Favorite();
        fav.setUserId(userId); fav.setTargetType(0); fav.setTargetId(productId);
        fav.setCreatedAt(LocalDateTime.now());
        favoriteMapper.insert(fav);
    }
    public void remove(String userId, Integer productId) {
        favoriteMapper.delete(new LambdaQueryWrapper<Favorite>()
            .eq(Favorite::getUserId, userId).eq(Favorite::getTargetId, productId));
    }
}
