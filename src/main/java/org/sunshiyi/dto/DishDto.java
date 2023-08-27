package org.sunshiyi.dto;

import lombok.Data;
import org.sunshiyi.entity.Dish;
import org.sunshiyi.entity.DishFlavor;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
