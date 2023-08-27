package org.sunshiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunshiyi.dto.DishDto;
import org.sunshiyi.entity.Dish;
import org.sunshiyi.entity.DishFlavor;
import org.sunshiyi.mapper.DishMapper;
import org.sunshiyi.service.DishFlavorService;
import org.sunshiyi.service.DishService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Resource
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void saveDishWithFlavor(DishDto dishDto) {
//        存储到dish表中
        this.save(dishDto);
        Long id = dishDto.getId();
        dishDto.getFlavors().forEach(dishFlavor -> {
            dishFlavor.setDishId(id);
        });
//        存储到dish_flavor表中
        dishFlavorService.saveBatch(dishDto.getFlavors());
    }

    @Override
    @Transactional
    public DishDto getDishWithFlavor(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateDishWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        dishDto.getFlavors().forEach(dishFlavor -> {
            dishFlavor.setId(null);
            dishFlavor.setDishId(dishDto.getId());
        });
        dishFlavorService.saveBatch(dishDto.getFlavors());
    }

    @Override
    @Transactional
    public void removeBatchDishWithFlavor(Long[] ids) {
        this.removeByIds(Arrays.asList(ids));
        Arrays.stream(ids).forEach(id -> {
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, id);
            dishFlavorService.remove(queryWrapper);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<DishDto> getDishByCategoryId(Long categoryId, Integer status) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, categoryId)
                .eq(status != null, Dish::getStatus, status);
        List<Dish> list = this.list(queryWrapper);
        return list.stream().map(dish -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId, dish.getId());
            List<DishFlavor> list1 = dishFlavorService.list(queryWrapper1);
            dishDto.setFlavors(list1);
            return dishDto;
        }).collect(Collectors.toList());
    }
}
