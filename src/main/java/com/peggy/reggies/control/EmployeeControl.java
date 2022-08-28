package com.peggy.reggies.control;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peggy.reggies.commom.R;
import com.peggy.reggies.entity.Employee;
import com.peggy.reggies.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeControl {
    @Autowired
    private EmployeeService employeeService;

    /**
     * <h3>员工登录</h3>
     * @param request 请求体中的数据
     * @param employee 登录成功员工的id
     * @return R<Employee> -返回结果集
     *
     * <li>将页面提交的密码进行MD5加密处理</li>
     * <li>根据页面提交的用户名username查询数据库</li>
     * <li>如果没有查询到则返回登录失败结果</li>
     * <li>密码比对，如果不一致则返回登录结果</li>
     * <li>查看员工状态，如果已为禁用状态，则返回员工已禁用结果</li>
     * <li>登录成功，将员工id存入session并返回登录成功结果</li>
     *
     */

    //@RequestBody 接收前端传递给后端的json字符串（请求体中的数据）
    //HttpServletRequest 登录成功，将员工对应的id存到session一份，这样想获取一份登录用户的信息就可以随时获取出来
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //- 将页面提交的密码进行MD5加密处理
        String password=employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        //- 根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //- 如果没有查询到则返回登录失败结果
        if(emp==null){
            return R.error("用户名不存在");
        }
        //- 密码比对，如果不一致则返回登录结果
        if(!password.equals(emp.getPassword())){
            log.info("登录界面密码:"+password);
            log.info("查询到的用户密码:"+emp.getPassword());
            return R.error("抱歉,用户【"+emp.getUsername()+"】您的登录密码错误");
        }
        //查看员工状态，如果已为禁用状态，则返回员工已禁用结果
        if(emp.getStatus()!=1){
            return R.error("抱歉,用户【"+emp.getUsername()+"】您的账户当前被禁用，请及时联系管理员处理");
        }
        //登录成功，将员工id存入session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * <h3>清除保存登录当前员工的id</h3>
     * @param request 用户id
     * @return - 返回结果集
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * <h3>添加员工</h3>
     * @param employee 员工对象
     * @return 返回结果集
     */
    @PostMapping
    public R<String> save(@RequestBody Employee employee){
        long id = Thread.currentThread().getId();
        log.info("当前执行save的线程id为{}",id);
        log.info("员工信息{}",employee.toString());
        //密码MD5加密处理设置新密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //添加到数据库
        employeeService.save(employee);
        return R.success("员工添加成功");
    }

    /**
     * <h3>添加员工分页查询</h3>
     */
    @RequestMapping("/page")
    public R<Page> page(int page, int pageSize,String name){
        log.info("员工信息分页查询page:{},pageSize:{},name:{}",page,pageSize,name);

        //构造分页构造器
        Page pageInfo =new Page(page,pageSize);
        //构造条件查询器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 员工的状态管理
     */

    @PutMapping
    public R<String> updte(@RequestBody Employee employee){
       //更新数据库
        employeeService.updateById(employee);
        return R.success("用户"+ employee.getName()+"修成功");
    }

    /**
     * 查询用户信息
     * @param id 查询用户的id
     * @return 查询结果集 （成功、失败）
     *
     * <li>点击编辑按钮时，页面跳转到add.html，并在url中携带参数[员工id]</li>
     * <li>在add.html页面获取url中的参数[员工id]</li>
     * <li>发送ajax请求，请求服务端，同时提交员工id参数</li>
     * <li>服务端接收请求，根据员工id查询员工信息，将员工信息以json形式响应给页面</li>
     * <li>页面接收服务端响应的json数据，通过VUE的数据绑定进行员工信息回显</li>
     * <li>点击保存按钮，发送ajax请求，将页面中的员工信息以json方式提交给服务端</li>
     * <li>服务端接收员工信息，并进行处理，完成后给页面响应</li>
     * <li>页面接收到服务端响应信息后进行相应处理</li>
     *
     */

    @GetMapping({"/{id}"})
    public R<Employee> getById(@PathVariable Long id){
        log.info("更加id获取员工的信息。。。。。");
        Employee employee=employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }else{
            return R.error("没有查询到该员工的信息");
        }
    }

}

