package com.peggy.reggies.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peggy.reggies.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
