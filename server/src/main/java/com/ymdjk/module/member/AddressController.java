package com.ymdjk.module.member;
import com.ymdjk.common.Result;
import com.ymdjk.module.member.entity.UserAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    @GetMapping
    public Result<?> list(Authentication auth) { return Result.success(addressService.list(auth.getName())); }
    @PostMapping
    public Result<Void> add(Authentication auth, @RequestBody UserAddress addr) {
        addressService.add(auth.getName(), addr); return Result.success();
    }
    @PutMapping
    public Result<Void> update(Authentication auth, @RequestBody UserAddress addr) {
        addressService.update(auth.getName(), addr); return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) { addressService.delete(id); return Result.success(); }
    @PutMapping("/{id}/default")
    public Result<Void> setDefault(Authentication auth, @PathVariable Integer id) {
        addressService.setDefault(auth.getName(), id); return Result.success();
    }
}
