package org.sunshiyi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.sunshiyi.common.R;
import org.sunshiyi.entity.Category;
import org.sunshiyi.service.CategoryService;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @PostMapping
    public R<String> addCategory(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("添加成功！");
    }

    @GetMapping("/page")
    public R<Page<Category>> pageQuery(Long page, Long pageSize) {
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> deleteCategory(Long ids) {
        categoryService.removeCategory(ids);
        return R.success("删除成功！");
    }

    @PutMapping
    public R<String> updateCategory(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("修改成功！");
    }

    @GetMapping("/list")
    public R<List<Category>> getCategoriesByType(Integer type) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(type != null, Category::getType, type);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
