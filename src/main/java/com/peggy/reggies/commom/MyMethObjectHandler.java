package com.peggy.reggies.commom;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.peggy.reggies.commom.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@Slf4j
public class MyMethObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        long id = Thread.currentThread().getId();
        log.info("当前执行MyMethObjectHandler的线程id为{}",id);
        log.info("公共字段填充【insert】");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser",  BaseContext.threadLocal.get());
        metaObject.setValue("updateUser", BaseContext.threadLocal.get());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段填充【update】");
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.threadLocal.get());
    }
}
