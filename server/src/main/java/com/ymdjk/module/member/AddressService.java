package com.ymdjk.module.member;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ymdjk.module.member.entity.UserAddress;
import com.ymdjk.module.member.mapper.UserAddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AddressService {
    private final UserAddressMapper addressMapper;
    public List<UserAddress> list(String userId) {
        return addressMapper.selectList(new LambdaQueryWrapper<UserAddress>().eq(UserAddress::getUserId, userId));
    }
    public void add(String userId, UserAddress addr) {
        addr.setUserId(userId);
        if (addr.getIsDefault() == 1) clearDefault(userId);
        addressMapper.insert(addr);
    }
    public void update(String userId, UserAddress addr) {
        if (addr.getIsDefault() == 1) clearDefault(userId);
        addressMapper.updateById(addr);
    }
    public void delete(Integer id) { addressMapper.deleteById(id); }
    public void setDefault(String userId, Integer id) {
        clearDefault(userId);
        UserAddress addr = addressMapper.selectById(id);
        if (addr != null) { addr.setIsDefault(1); addressMapper.updateById(addr); }
    }
    private void clearDefault(String userId) {
        UserAddress defaultAddr = addressMapper.selectOne(
            new LambdaQueryWrapper<UserAddress>().eq(UserAddress::getUserId, userId).eq(UserAddress::getIsDefault, 1));
        if (defaultAddr != null) { defaultAddr.setIsDefault(0); addressMapper.updateById(defaultAddr); }
    }
}
