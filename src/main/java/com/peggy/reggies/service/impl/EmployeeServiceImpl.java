package com.peggy.reggies.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peggy.reggies.entity.Employee;
import com.peggy.reggies.mapper.EmployeeMapper;
import com.peggy.reggies.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
