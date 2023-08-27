package org.sunshiyi.dto;

import lombok.Data;
import org.sunshiyi.entity.Setmeal;
import org.sunshiyi.entity.SetmealDish;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
