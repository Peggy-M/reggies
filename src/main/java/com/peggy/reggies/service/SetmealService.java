package com.peggy.reggies.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.peggy.reggies.dto.DishDto;
import com.peggy.reggies.dto.SetmealDto;
import com.peggy.reggies.entity.Setmeal;
import com.peggy.reggies.entity.SetmealDish;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);
    void updateWithFlavor(SetmealDto setmealDto);
    SetmealDto getByIdWithFlavor(Long id);
}
