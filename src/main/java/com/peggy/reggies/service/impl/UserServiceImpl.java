package com.peggy.reggies.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peggy.reggies.entity.User;
import com.peggy.reggies.mapper.UserMapper;
import com.peggy.reggies.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
