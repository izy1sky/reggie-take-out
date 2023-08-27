package org.sunshiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunshiyi.entity.ShoppingCart;
import org.sunshiyi.mapper.ShoppingCartMapper;
import org.sunshiyi.service.ShoppingCartService;
import org.sunshiyi.utils.BaseContext;

import java.time.LocalDateTime;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Override
    @Transactional
    public void addShoppingCart(ShoppingCart shoppingCart) {
//        id自增，user_id从threadLocal获取，number自己计算+1，create_time通过MP的AOP实现自动填充
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId)
                .eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId())
                .eq(shoppingCart.getDishFlavor() != null, ShoppingCart::getDishFlavor, shoppingCart.getDishFlavor())
                .eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        ShoppingCart shoppingCart1 = this.getOne(queryWrapper);
        if (shoppingCart1 != null) {
//            重复添加一道菜
            shoppingCart1.setNumber(shoppingCart1.getNumber() + 1);
            shoppingCart1.setCreateTime(LocalDateTime.now());
            this.updateById(shoppingCart1);
        } else {
//            添加新的菜
            shoppingCart.setUserId(userId);
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            this.save(shoppingCart);
        }
    }

    @Override
    @Transactional
    public void subShoppingCart(ShoppingCart shoppingCart) {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId)
                .eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        ShoppingCart shoppingCart1 = this.list(queryWrapper).get(0);
//        如果只有1份，直接删除
        if (shoppingCart1.getNumber() == 1) {
            this.removeById(shoppingCart1.getId());
        } else {
//        如果有多分，数量减去1
            shoppingCart1.setNumber(shoppingCart1.getNumber() - 1);
            shoppingCart1.setCreateTime(LocalDateTime.now());
            this.updateById(shoppingCart1);
        }
    }
}
