package com.peggy.reggies.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peggy.reggies.entity.Setmeal;
import com.peggy.reggies.mapper.SetmealMapper;
import com.peggy.reggies.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper,Setmeal> implements SetmealService {
}
