package com.peggy.reggies;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peggy.reggies.commom.R;
import com.peggy.reggies.control.EmployeeControl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class ReggiesApplicationTests {

    @Autowired
    EmployeeControl employeeControl;

    @Test
    void contextLoads() {
        R<Page> page = employeeControl.page(1, 4, "null");
        System.out.println("================="+page);
    }
}
