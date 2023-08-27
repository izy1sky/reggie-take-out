package org.sunshiyi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.sunshiyi.common.R;
import org.sunshiyi.entity.Orders;
import org.sunshiyi.service.OrdersService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class OrdersController {
    @Resource
    private OrdersService ordersService;

    @PostMapping("/submit")
    public R<String> saveOrderWithDetails(@RequestBody Orders orders) {
        ordersService.saveOrderWithDetails(orders);
        return R.success("订单创建成功！");
    }

    @GetMapping("/userPage")
    public R<Page<Orders>> pageQuery(Long page, Long pageSize) {
        Page<Orders> orders = ordersService.pageQuery(page, pageSize);
        return R.success(orders);
    }
}
