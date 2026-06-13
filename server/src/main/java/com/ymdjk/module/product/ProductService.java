package com.ymdjk.module.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ymdjk.common.PageResult;
import com.ymdjk.module.product.entity.Product;
import com.ymdjk.module.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;

    public PageResult<Product> list(int page, int pageSize, Integer categoryId, String keyword) {
        LambdaQueryWrapper<Product> q = new LambdaQueryWrapper<Product>()
                .eq(Product::getStatus, 1);
        if (categoryId != null) q.eq(Product::getCategoryId, categoryId);
        if (keyword != null && !keyword.isEmpty())
            q.like(Product::getTitle, keyword);
        q.orderByAsc(Product::getSortOrder);
        Page<Product> p = productMapper.selectPage(new Page<>(page, pageSize), q);
        PageResult<Product> r = new PageResult<>();
        r.setRecords(p.getRecords());
        r.setTotal(p.getTotal());
        r.setPage(p.getCurrent());
        r.setPageSize(p.getSize());
        return r;
    }

    public Product detail(Integer id) {
        return productMapper.selectById(id);
    }
}
