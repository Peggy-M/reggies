package com.peggy.reggies.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peggy.reggies.dto.SetmealDto;
import com.peggy.reggies.entity.Setmeal;
import com.peggy.reggies.entity.SetmealDish;
import com.peggy.reggies.mapper.SetmealMapper;
import com.peggy.reggies.service.SetmealDisService;
import com.peggy.reggies.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper,Setmeal> implements SetmealService {

    @Autowired
    SetmealDisService setmealDishService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal，执行insert操作
        save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void updateWithFlavor(SetmealDto setmealDto) {
        //根据id修改菜品的基本信息
        super.updateById(setmealDto);

        //通过dish_id,删除菜品的flavor
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getDishId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());


        //将数据批量保存到dish_flavor数据库
        setmealDishService.saveBatch(setmealDishes);
    }

    public SetmealDto getByIdWithFlavor(Long id) {
        //通过id查询菜品基本信息
        Setmeal setmeal = super.getById(id);

        //创建dto对象
        SetmealDto setmealDto = new SetmealDto();

        //对象拷贝
        BeanUtils.copyProperties(setmeal,setmealDto);

        //条件查询flavor
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getDishId,setmeal.getId());
        List<SetmealDish> list = setmealDishService.list(queryWrapper);

        //将查询到的flavor赋值到dto对象中
        setmealDto.setSetmealDishes(list);

        return setmealDto;
    }

}
