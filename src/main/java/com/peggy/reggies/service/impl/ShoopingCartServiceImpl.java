package com.peggy.reggies.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peggy.reggies.entity.ShoppingCart;
import com.peggy.reggies.mapper.ShoopingCartMapper;
import com.peggy.reggies.service.ShoopingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoopingCartServiceImpl extends ServiceImpl<ShoopingCartMapper,ShoppingCart> implements ShoopingCartService{

}