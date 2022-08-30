package com.peggy.reggies.dto;

import com.peggy.reggies.entity.Setmeal;
import com.peggy.reggies.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;

    private Integer copies;

}
