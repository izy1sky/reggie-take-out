package org.sunshiyi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sunshiyi.entity.Orders;

public interface OrdersService extends IService<Orders> {
    void saveOrderWithDetails(Orders orders);

    Page<Orders> pageQuery(Long page, Long pageSize);
}
