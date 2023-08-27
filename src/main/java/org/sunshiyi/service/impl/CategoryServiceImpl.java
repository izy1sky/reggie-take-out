package org.sunshiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.sunshiyi.entity.Category;
import org.sunshiyi.entity.Dish;
import org.sunshiyi.entity.Setmeal;
import org.sunshiyi.exceptions.CategoryDeleteException;
import org.sunshiyi.mapper.CategoryMapper;
import org.sunshiyi.service.CategoryService;
import org.sunshiyi.service.DishService;
import org.sunshiyi.service.SetmealService;

import javax.annotation.Resource;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Resource
    private DishService dishService;
    @Resource
    private SetmealService setmealService;
    @Override
    public void removeCategory(Long ids) {
//    检查是否关联的菜品
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, ids);
        int count1 = dishService.count(queryWrapper);
        if (count1 > 0) {
            throw new CategoryDeleteException("当前分类关联了菜品，不能删除！");
        }
//    检查是否关联了套餐
        LambdaQueryWrapper<Setmeal> queryWrapper1 = new LambdaQueryWrapper<Setmeal>();
        queryWrapper1.eq(Setmeal::getCategoryId, ids);
        int count2 = setmealService.count(queryWrapper1);
        if (count2 > 0) {
            throw new CategoryDeleteException("当前分类关联了套餐，不能删除！");
        }
        this.removeById(ids);
    }
}
