package com.peggy.reggies.control;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peggy.reggies.commom.R;
import com.peggy.reggies.entity.User;
import com.peggy.reggies.service.UserService;
import com.peggy.reggies.utils.SMSUtils;
import com.peggy.reggies.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserControl {

    @Autowired
    UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)){
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode4String(4);
            log.info("code={}",code);

            //调用阿里云提供的短信服务API完成短信发送
            SMSUtils.sendMessage("Peggy站点","SMS_250195025",phone,code);

            //需要将生成的验证码保存到session
            session.setAttribute(phone,code);
            return R.success("短信发送成功");
        }
        return R.error("短信发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从session中获取保存的验证码
        String codeInSession = session.getAttribute(phone).toString();
        //进行验证码的对比（页面提交的验证码和session中保存的验证码）
        if (code != null && code.equals(codeInSession)){
            //如果比对成功，则登录成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);

            if (user == null){
                //判断当前手机号是否为新用户，如果是新用户则自动完成注入
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user",user);
            return R.success(user);
        }
        return R.error("登录失败");
    }

    @GetMapping("/userPage")
    public R<Page> userPage(Integer page,Integer pageSize){
        Page<User> pageInfo=new Page<>();
        LambdaQueryWrapper lambdaQueryWrapper=new LambdaQueryWrapper();

        return R.success(pageInfo);
    }
}
