package com.peggy.reggies.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peggy.reggies.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
