package org.sunshiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunshiyi.dto.SetmealDto;
import org.sunshiyi.entity.Setmeal;
import org.sunshiyi.entity.SetmealDish;
import org.sunshiyi.mapper.SetmealMapper;
import org.sunshiyi.service.SetmealDishService;
import org.sunshiyi.service.SetmealService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Resource
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveSetmealWithDishes(SetmealDto setmealDto) {
        this.save(setmealDto);
        Long setmealId = setmealDto.getId();
        setmealDto.getSetmealDishes().forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        setmealDishService.saveBatch(setmealDto.getSetmealDishes());
    }

    @Override
    @Transactional(readOnly = true)
    public SetmealDto getSetmealWithDishes(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    @Override
    @Transactional
    public void updateSetmealWithDishes(SetmealDto setmealDto) {
        this.updateById(setmealDto);
        Long setmealId = setmealDto.getId();
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealId);
        setmealDishService.remove(queryWrapper);
        setmealDto.getSetmealDishes().forEach(setmealDish -> {
            setmealDish.setId(null);
            setmealDish.setSetmealId(setmealId);
        });
        setmealDishService.saveBatch(setmealDto.getSetmealDishes());
    }

    @Override
    @Transactional
    public void stop(Long[] ids) {
        Arrays.stream(ids).forEach(id -> {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(id);
            setmeal.setStatus(0);
            this.updateById(setmeal);
        });
    }

    @Override
    @Transactional
    public void start(Long[] ids) {
        Arrays.stream(ids).forEach(id -> {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(id);
            setmeal.setStatus(1);
            this.updateById(setmeal);
        });
    }

    @Override
    @Transactional
    public void deleteWithDishes(Long[] ids) {
//        第一步 删除setmeal表中的数据
        this.removeByIds(Arrays.asList(ids));
//        第二部 删除setmeal_dish表中的数据
        Arrays.stream(ids).forEach(id -> {
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId, id);
            setmealDishService.remove(queryWrapper);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Setmeal> getSetmealWithDishByCategoryId(Long categoryId, Integer status) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId, categoryId)
                .eq(status != null, Setmeal::getStatus, status);
        return this.list(queryWrapper);
    }

    @Override
    public List<SetmealDish> getSetmealDishBySetmealId(Long setmealId) {
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealId);
        return setmealDishService.list(queryWrapper);
    }


}
