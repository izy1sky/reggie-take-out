package org.sunshiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sunshiyi.dto.DishDto;
import org.sunshiyi.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    void saveDishWithFlavor(DishDto dishDto);

    DishDto getDishWithFlavor(Long id);

    void updateDishWithFlavor(DishDto dishDto);

    void removeBatchDishWithFlavor(Long[] ids);

    List<DishDto> getDishByCategoryId(Long categoryId, Integer status);
}
