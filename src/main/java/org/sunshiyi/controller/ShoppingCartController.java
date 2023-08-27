package org.sunshiyi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.sunshiyi.common.R;
import org.sunshiyi.entity.ShoppingCart;
import org.sunshiyi.service.ShoppingCartService;
import org.sunshiyi.utils.BaseContext;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Resource
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R<List<ShoppingCart>> getShoppingCarts() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    @PostMapping("/add")
    public R<String> addShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        shoppingCartService.addShoppingCart(shoppingCart);
        return R.success("添加购物车成功！");
    }

    @PostMapping("/sub")
    public R<String> subShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        shoppingCartService.subShoppingCart(shoppingCart);
        return R.success("删除成功！");
    }

    @DeleteMapping("/clean")
    public R<String> cleanShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        shoppingCartService.remove(queryWrapper);
        return R.success("清除成功！");
    }
}
