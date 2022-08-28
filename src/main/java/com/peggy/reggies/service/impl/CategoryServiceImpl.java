package com.peggy.reggies.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peggy.reggies.commom.CustomException;
import com.peggy.reggies.entity.Category;
import com.peggy.reggies.entity.Dish;
import com.peggy.reggies.entity.Setmeal;
import com.peggy.reggies.mapper.CategoryMapper;
import com.peggy.reggies.service.CategoryService;
import com.peggy.reggies.service.DishService;
import com.peggy.reggies.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;

    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int dishCount = dishService.count(dishLambdaQueryWrapper);
        log.info("查询的到的菜品数量：{}",dishCount);
        if(dishCount>0){
            throw new CustomException("当前的分类关联了菜品，不可删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int setmealCount = setmealService.count(setmealLambdaQueryWrapper);
        log.info("查询的到的菜品数量：{}",setmealCount);
        if(setmealCount>0){
            throw new CustomException("当前的分类关联了套餐，不可删除");
        }
        //正常删除
        super.removeById(id);
    }
}
