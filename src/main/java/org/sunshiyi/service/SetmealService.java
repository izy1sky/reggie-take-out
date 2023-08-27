package org.sunshiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sunshiyi.dto.SetmealDto;
import org.sunshiyi.entity.Setmeal;
import org.sunshiyi.entity.SetmealDish;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveSetmealWithDishes(SetmealDto setmealDto);

    SetmealDto getSetmealWithDishes(Long id);

    void updateSetmealWithDishes(SetmealDto setmealDto);

    void stop(Long[] ids);

    void start(Long[] ids);

    void deleteWithDishes(Long[] ids);

    List<Setmeal> getSetmealWithDishByCategoryId(Long categoryId, Integer status);

    List<SetmealDish> getSetmealDishBySetmealId(Long setmealId);
}
