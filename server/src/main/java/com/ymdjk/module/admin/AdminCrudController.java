package com.ymdjk.module.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ymdjk.common.PageResult;
import com.ymdjk.common.Result;
import com.ymdjk.module.admin.entity.Admin;
import com.ymdjk.module.admin.entity.Config;
import com.ymdjk.module.admin.entity.Role;
import com.ymdjk.module.admin.mapper.AdminMapper;
import com.ymdjk.module.admin.mapper.ConfigMapper;
import com.ymdjk.module.admin.mapper.RoleMapper;
import com.ymdjk.module.content.entity.Ad;
import com.ymdjk.module.content.entity.Content;
import com.ymdjk.module.content.entity.Message;
import com.ymdjk.module.content.mapper.AdMapper;
import com.ymdjk.module.content.mapper.ContentMapper;
import com.ymdjk.module.content.mapper.MessageMapper;
import com.ymdjk.module.product.entity.Category;
import com.ymdjk.module.product.mapper.CategoryMapper;
import com.ymdjk.module.finance.entity.PayLog;
import com.ymdjk.module.finance.entity.Withdraw;
import com.ymdjk.module.finance.mapper.PayLogMapper;
import com.ymdjk.module.finance.mapper.WithdrawMapper;
import com.ymdjk.module.member.entity.Member;
import com.ymdjk.module.member.mapper.MemberMapper;
import com.ymdjk.module.order.entity.Order;
import com.ymdjk.module.order.mapper.OrderMapper;
import com.ymdjk.module.product.entity.Product;
import com.ymdjk.module.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminCrudController {
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final MemberMapper memberMapper;
    private final PayLogMapper payLogMapper;
    private final WithdrawMapper withdrawMapper;
    private final ContentMapper contentMapper;
    private final CategoryMapper categoryMapper;
    private final AdMapper adMapper;
    private final AdminMapper adminMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;
    private final ConfigMapper configMapper;
    private final MessageMapper messageMapper;

    @GetMapping("/products")
    public Result<?> listProducts(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "20") int pageSize) {
        return Result.success(pageResult(productMapper.selectPage(new Page<>(page, pageSize), null)));
    }

    @PostMapping("/products")
    public Result<Void> saveProduct(@RequestBody Product product) {
        if (product.getId() != null) productMapper.updateById(product);
        else productMapper.insert(product);
        return Result.success();
    }

    @DeleteMapping("/products/{id}")
    public Result<Void> deleteProduct(@PathVariable Integer id) {
        productMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/orders")
    public Result<?> listOrders(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "20") int pageSize) {
        return Result.success(pageResult(orderMapper.selectPage(new Page<>(page, pageSize), null)));
    }

    @GetMapping("/members")
    public Result<?> listMembers(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "20") int pageSize) {
        return Result.success(pageResult(memberMapper.selectPage(new Page<>(page, pageSize), null)));
    }

    @GetMapping("/finance")
    public Result<?> listFinance(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "20") int pageSize) {
        return Result.success(pageResult(payLogMapper.selectPage(new Page<>(page, pageSize), null)));
    }

    @GetMapping("/withdraw")
    public Result<?> listWithdraw(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "20") int pageSize) {
        return Result.success(pageResult(withdrawMapper.selectPage(new Page<>(page, pageSize), null)));
    }

    @PostMapping("/withdraw/{id}/approve")
    public Result<Void> approveWithdraw(@PathVariable Integer id) {
        Withdraw w = withdrawMapper.selectById(id);
        if (w != null) { w.setStatus(1); withdrawMapper.updateById(w); }
        return Result.success();
    }

    @GetMapping("/content")
    public Result<?> listContent(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "20") int pageSize) {
        return Result.success(pageResult(contentMapper.selectPage(new Page<>(page, pageSize), null)));
    }

    @PostMapping("/content")
    public Result<Void> saveContent(@RequestBody Content content) {
        if (content.getId() != null) contentMapper.updateById(content);
        else contentMapper.insert(content);
        return Result.success();
    }

    @GetMapping("/categories")
    public Result<?> listCategories(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "20") int pageSize) {
        return Result.success(pageResult(categoryMapper.selectPage(new Page<>(page, pageSize), null)));
    }

    @PostMapping("/categories")
    public Result<Void> saveCategory(@RequestBody Category category) {
        if (category.getId() != null) categoryMapper.updateById(category);
        else categoryMapper.insert(category);
        return Result.success();
    }

    @DeleteMapping("/categories/{id}")
    public Result<Void> deleteCategory(@PathVariable Integer id) {
        categoryMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/ads")
    public Result<?> listAds(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "20") int pageSize) {
        return Result.success(pageResult(adMapper.selectPage(new Page<>(page, pageSize), null)));
    }

    @PostMapping("/ads")
    public Result<Void> saveAd(@RequestBody Ad ad) {
        if (ad.getId() != null) adMapper.updateById(ad);
        else adMapper.insert(ad);
        return Result.success();
    }

    @DeleteMapping("/ads/{id}")
    public Result<Void> deleteAd(@PathVariable Integer id) {
        adMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/admins")
    public Result<?> listAdmins(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "20") int pageSize) {
        return Result.success(pageResult(adminMapper.selectPage(new Page<>(page, pageSize), null)));
    }

    @PostMapping("/admins")
    public Result<Void> saveAdmin(@RequestBody Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        if (admin.getId() != null) adminMapper.updateById(admin);
        else adminMapper.insert(admin);
        return Result.success();
    }

    @DeleteMapping("/admins/{id}")
    public Result<Void> deleteAdmin(@PathVariable Integer id) {
        adminMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/roles")
    public Result<?> listRoles(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "20") int pageSize) {
        return Result.success(pageResult(roleMapper.selectPage(new Page<>(page, pageSize), null)));
    }

    @PostMapping("/roles")
    public Result<Void> saveRole(@RequestBody Role role) {
        if (role.getId() != null) roleMapper.updateById(role);
        else roleMapper.insert(role);
        return Result.success();
    }

    @DeleteMapping("/roles/{id}")
    public Result<Void> deleteRole(@PathVariable Integer id) {
        roleMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/config")
    public Result<?> listConfig() {
        return Result.success(configMapper.selectList(null));
    }

    @PutMapping("/config/{key}")
    public Result<Void> updateConfig(@PathVariable String key, @RequestParam String value) {
        Config cfg = configMapper.selectOne(new LambdaQueryWrapper<Config>().eq(Config::getConfigKey, key));
        if (cfg != null) { cfg.setValue(value); configMapper.updateById(cfg); }
        return Result.success();
    }

    @GetMapping("/messages")
    public Result<?> listMessages(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "20") int pageSize) {
        return Result.success(pageResult(messageMapper.selectPage(new Page<>(page, pageSize), null)));
    }

    private <T> PageResult<T> pageResult(Page<T> p) {
        PageResult<T> r = new PageResult<>();
        r.setRecords(p.getRecords()); r.setTotal(p.getTotal());
        r.setPage(p.getCurrent()); r.setPageSize(p.getSize());
        return r;
    }
}
