package com.peggy.reggies.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peggy.reggies.dto.DishDto;
import com.peggy.reggies.entity.Dish;
import com.peggy.reggies.entity.DishFlavor;
import com.peggy.reggies.mapper.DishMapper;
import com.peggy.reggies.service.DishService;
import com.peggy.reggies.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    DishFalvorServiceImpl disFlavorService;
    @Autowired
    SetmealService setmealService;


    public void saveWithFlavor(DishDto dishDto) {
        //根据id修改菜品的基本信息
        super.save(dishDto);


        //获取前端提交的flavor数据
        List<DishFlavor> flavors = dishDto.getFlavors();

        //将每条flavor的dishId赋值
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());


        //将数据批量保存到dish_flavor数据库
        disFlavorService.saveBatch(flavors);

    }


    public DishDto getByIdWithFlavor(Long id) {
        //通过id查询菜品基本信息
        Dish dish = super.getById(id);

        //创建dto对象
        DishDto dishDto = new DishDto();

        //对象拷贝
        BeanUtils.copyProperties(dish,dishDto);

        //条件查询flavor
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = disFlavorService.list(queryWrapper);

        //将查询到的flavor赋值到dto对象中
        dishDto.setFlavors(list);

        return dishDto;
    }

    public void updateWithFlavor(DishDto dishDto) {
        //根据id修改菜品的基本信息
        super.updateById(dishDto);

        //通过dish_id,删除菜品的flavor
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        disFlavorService.remove(queryWrapper);

        //获取前端提交的flavor数据
        List<DishFlavor> flavors = dishDto.getFlavors();

        //将每条flavor的dishId赋值
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());


        //将数据批量保存到dish_flavor数据库
        disFlavorService.saveBatch(flavors);
    }


}

