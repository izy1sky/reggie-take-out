package org.sunshiyi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.sunshiyi.common.R;
import org.sunshiyi.dto.SetmealDto;
import org.sunshiyi.entity.Category;
import org.sunshiyi.entity.Setmeal;
import org.sunshiyi.entity.SetmealDish;
import org.sunshiyi.service.CategoryService;
import org.sunshiyi.service.SetmealDishService;
import org.sunshiyi.service.SetmealService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Resource
    private SetmealService setmealService;
    @Resource
    private SetmealDishService setmealDishService;
    @Resource
    private CategoryService categoryService;

    @GetMapping("/page")
    @Transactional(readOnly = true)
    public R<Page<SetmealDto>> pageQuery(Long page, Long pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name), Setmeal::getName, name);
        setmealService.page(pageInfo, queryWrapper);
        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");
        List<SetmealDto> list = pageInfo.getRecords().stream().map(setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            Category category = categoryService.getById(setmeal.getCategoryId());
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }

    @PostMapping
    public R<String> saveSetmealWithDishes(@RequestBody SetmealDto setmealDto) {
        setmealService.saveSetmealWithDishes(setmealDto);
        return R.success("保存成功！");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> getSetmealWithDishes(@PathVariable Long id) {
        SetmealDto setmealDto = setmealService.getSetmealWithDishes(id);
        return R.success(setmealDto);
    }

    @PutMapping
    public R<String> updateSetmealWithDishes(@RequestBody SetmealDto setmealDto) {
        setmealService.updateSetmealWithDishes(setmealDto);
        return R.success("修改成功！");
    }

    @PostMapping("/status/0")
    public R<String> stop(Long[] ids) {
        setmealService.stop(ids);
        return R.success("修改成功！");
    }

    @PostMapping("/status/1")
    public R<String> start(Long[] ids) {
        setmealService.start(ids);
        return R.success("修改成功！");
    }

    @DeleteMapping
    public R<String> deleteWithDishes(Long[] ids) {
        setmealService.deleteWithDishes(ids);
        return R.success("删除成功！");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> getSetmealWithDishByCategoryId(Long categoryId, Integer status) {
        List<Setmeal> setmealDtoList = setmealService.getSetmealWithDishByCategoryId(categoryId, status);
        return R.success(setmealDtoList);
    }

    @GetMapping("/dish/{setmealId}")
    public R<List<SetmealDish>> getSetmealDishBySetmealId(@PathVariable Long setmealId) {
        List<SetmealDish> setmealDishList = setmealService.getSetmealDishBySetmealId(setmealId);
        return R.success(setmealDishList);
    }
}
