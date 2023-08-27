package org.sunshiyi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.sunshiyi.entity.OrderDetail;
import org.sunshiyi.mapper.OrderDetailMapper;
import org.sunshiyi.service.OrderDetailService;
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
