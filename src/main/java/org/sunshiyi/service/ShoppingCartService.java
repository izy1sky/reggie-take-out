package org.sunshiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sunshiyi.entity.ShoppingCart;

public interface ShoppingCartService extends IService<ShoppingCart> {
    void addShoppingCart(ShoppingCart shoppingCart);

    void subShoppingCart(ShoppingCart shoppingCart);
}
