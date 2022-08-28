package com.peggy.reggies.commom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * <h3>全局异常处理</h3>
 *
 * 自定义需要处理的内部异常
 */

@ResponseBody
@ControllerAdvice(annotations = {RestController.class, Conditional.class})
@Slf4j
public class GlobalExceptionHandler {

    /**
     * <h3>添加员工字段冲突异常处理</h3>
     * @param exception sql约束异常信息
     * @return 结果集R
     *
     * <br>
     * 对员工登录界面的用户名唯一字段的约束异常进行处理，处理结果信息结果通过R返回集
     * </br>
     */

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> excptionHandler(SQLIntegrityConstraintViolationException exception){
        //日志信息 Duplicate entry 'xxxxxx' for key 'idx_username'
        log.error(exception.getMessage());
        //判断异常的信息是否是数据库的字段重复 contains()判断元素是否在动态数组中
        if(exception.getMessage().contains("Duplicate entry")){
            //分割处理数据异常数据只保留 冲突字段
            String[] split=exception.getMessage().split(" ");
            String msg=split[2]+"该用户已存在";
            return R.error(msg);
        }
        return R.error("unknown error");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler (CustomException exception){
        log.error(exception.getMessage());

        return R.error(exception.getMessage());
    }


}
