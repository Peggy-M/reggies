package com.peggy.reggies.filter;

import com.alibaba.fastjson.JSON;
import com.peggy.reggies.commom.R;
import com.peggy.reggies.commom.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    /**
     * PATH_MATCHER 定义静态方法AntPathMatcher对象用于路径匹配调用
     */

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();



    /**
     * <h3>非法访问拦截<h3>
     *
     * @param servletRequest 请求
     * @param servletResponse 响应
     * @param filterChain   过滤器
     * @throws IOException
     * @throws ServletException
     *
     *<li>判断本次请求是否需要处理</li>
     *<li>如果不需要处理，则直接放行</li>
     *<li>判断登录状态，如果已登录，则直接放行</li>
     *<li>如果未登录则返回未登录结果</li>
     *
     */

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        long id = Thread.currentThread().getId();
        log.info("当前执行LoginCheckFilter的线程id为{}",id);

        //将ServletRequest转换为 Http 获取Session
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;
        log.info("拦截器请求:{}",request.getRequestURI());

        //获取当前请求的URL
        String requestURI = request.getRequestURI();

        //定义不需要处理的请求
        String[] urls=new String[]{
                "/employee/login",   //登录
                "/employee/logout",  //注销
                "/backend/**",       //静态资源目录
<<<<<<< HEAD
                "/front/**"          //静态资源目录
=======
                "/front/**",          //静态资源目录
                "/common/**",
>>>>>>> 1f273fc... 菜品套餐分类管理部分完成
        };

        //判断本次请求是否需要处理
        if(check(urls,requestURI)){
            log.info("本次请求不需要处理{}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        //判断当前的登录状态，如果已经登录，放行
        if(request.getSession().getAttribute("employee")!=null){
            log.info("用户{}已经登录",request.getSession().getAttribute("employee"));
            BaseContext.threadLocal.set((Long) request.getSession().getAttribute("employee"));
            filterChain.doFilter(request,response);
            return;
        }
        //如果未登录则返回未登录结果
        log.info("未登录");
        //将结果集R中的数据与当前的状态码进行返回到前段交给与js处理
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }


    /**
     * <h3>路径匹配校验</h3>
     * @param urls 预设放行路径
     * @param requestURI 当前前端请求路径
     * @return 路径匹配结果
     *<h3>
     * 如果在请求中，预设放行行路径与请求路径匹配，则返回 ture 负责返回 flash
     *</h3>
     */

    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean math=PATH_MATCHER.match(url,requestURI);
            if(math)
                //路径请求与预设放行路径匹配
                return true;
        }
        //不匹配
        return false;
    }
}





