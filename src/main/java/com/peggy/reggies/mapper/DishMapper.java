package com.peggy.reggies.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peggy.reggies.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
