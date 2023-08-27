package org.sunshiyi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.sunshiyi.entity.DishFlavor;
import org.sunshiyi.mapper.DishFlavorMapper;
import org.sunshiyi.service.DishFlavorService;
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
