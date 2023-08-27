package org.sunshiyi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.sunshiyi.common.R;
import org.sunshiyi.dto.DishDto;
import org.sunshiyi.entity.Category;
import org.sunshiyi.entity.Dish;
import org.sunshiyi.service.CategoryService;
import org.sunshiyi.service.DishService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Resource
    private DishService dishService;
    @Resource
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page<DishDto>> pageQuery(Long page, Long pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dtoPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name), Dish::getName, name);
        dishService.page(pageInfo, queryWrapper);
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<DishDto> list = pageInfo.getRecords().stream().map(dish -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            Category category = categoryService.getById(dish.getCategoryId());
            dishDto.setCategoryName(category.getName());
            return dishDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    @PostMapping
    public R<String> saveDishWithFlavor(@RequestBody DishDto dishDto) {
        dishService.saveDishWithFlavor(dishDto);
        return R.success("添加成功！");
    }

    @GetMapping("/{id}")
    public R<DishDto> getDishWithFlaVor(@PathVariable Long id) {
        DishDto dishDto = dishService.getDishWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> updateDishWithFlavor(@RequestBody DishDto dishDto) {
        dishService.updateDishWithFlavor(dishDto);
        return R.success("修改成功！");
    }

    @PostMapping("/status/0")
    public R<String> stopBatch(Long[] ids) {
        Arrays.stream(ids).forEach(id -> {
            Dish dish = new Dish();
            dish.setId(id);
            dish.setStatus(0);
            dishService.updateById(dish);
        });
        return R.success("修改成功！");
    }

    @PostMapping("/status/1")
    public R<String> startBatch(Long[] ids) {
        Arrays.stream(ids).forEach(id -> {
            Dish dish = new Dish();
            dish.setId(id);
            dish.setStatus(1);
            dishService.updateById(dish);
        });
        return R.success("修改成功！");
    }

    @DeleteMapping
    public R<String> removeBatchDishWithFlavor(Long[] ids) {
        dishService.removeBatchDishWithFlavor(ids);
        return R.success("删除成功！");
    }

    @GetMapping("/list")
    public R<List<DishDto>> getDishByCategoryId(Long categoryId, Integer status) {
        List<DishDto> list = dishService.getDishByCategoryId(categoryId, status);
        return R.success(list);
    }
}
