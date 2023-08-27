package org.sunshiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunshiyi.entity.AddressBook;
import org.sunshiyi.entity.OrderDetail;
import org.sunshiyi.entity.Orders;
import org.sunshiyi.entity.ShoppingCart;
import org.sunshiyi.mapper.OrdersMapper;
import org.sunshiyi.service.AddressBookService;
import org.sunshiyi.service.OrderDetailService;
import org.sunshiyi.service.OrdersService;
import org.sunshiyi.service.ShoppingCartService;
import org.sunshiyi.utils.BaseContext;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Resource
    private AddressBookService addressBookService;
    @Resource
    private OrderDetailService orderDetailService;
    @Resource
    private ShoppingCartService shoppingCartService;

    @Override
    @Transactional
    public void saveOrderWithDetails(Orders orders) {
        Long userId = BaseContext.getCurrentId();
        //        获取shoppingCart的内容
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(queryWrapper);
        final BigDecimal[] amount = {new BigDecimal(0)};
        shoppingCarts.forEach(shoppingCart -> {
            amount[0] = amount[0].add(shoppingCart.getAmount().multiply(BigDecimal.valueOf(shoppingCart.getNumber())));
        });

//        插入一条orders记录
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setUserId(userId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now().plusHours(1));
        orders.setStatus(1);
        orders.setAmount(amount[0]);

//        获得address_book表中的信息
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        BeanUtils.copyProperties(addressBook, orders, "id", "");
        orders.setAddress(addressBook.getDetail());

        this.save(orders);

//      插入到order_detail表中
        List<OrderDetail> orderDetails = shoppingCarts.stream().map(shoppingCart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail, "id");
            orderDetail.setOrderId(orders.getId());
            return orderDetail;
        }).collect(Collectors.toList());
        orderDetailService.saveBatch(orderDetails);
//        删除shoppingCart中所有的购物记录
        shoppingCartService.remove(queryWrapper);
    }

    @Override
    public Page<Orders> pageQuery(Long page, Long pageSize) {
        Long userId = BaseContext.getCurrentId();
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, userId)
                .orderByDesc(Orders::getCheckoutTime);
        this.page(pageInfo, queryWrapper);
        return pageInfo;
    }
}
