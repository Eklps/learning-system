package com.eklps.tlias.interceptor;

import com.eklps.tlias.pojo.Result;
import com.eklps.tlias.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper; // Jackson工具包，SpringBoot自带
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component // 交给 IOC 容器管理
public class LoginCheckInterceptor implements HandlerInterceptor {

    // 目标资源方法运行前运行
    // 返回 true: 放行; 返回 false: 拦截
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {

        // 1. 获取请求 URL
        String url = req.getRequestURL().toString();
        log.info("请求的 URL: {}", url);

        // 2. 判断请求 URL 中是否包含 login，如果包含，说明是登录操作，放行
        if(url.contains("login")){
            log.info("登录操作，放行...");
            return true;
        }

        // 3. 获取请求头中的令牌 (token)
        String jwt = req.getHeader("token");

        // 4. 判断令牌是否存在，如果不存在，返回错误结果 (未登录)
        if(!StringUtils.hasLength(jwt)){
            log.info("请求头 token 为空，返回未登录的信息");
            // 手动转换对象为 JSON 并响应
            Result error = Result.error("NOT_LOGIN");
            String notLogin = new ObjectMapper().writeValueAsString(error);
            //ObjectMapper这个库是专门把java的对象转换为Json语言的
            resp.getWriter().write(notLogin);
            //将生成的json字符串写入HTTP的响应体里
            return false;
        }

        // 5. 解析令牌，如果解析失败，返回错误结果 (未登录)
        try {
            JwtUtils.parseJWT(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解析令牌失败，返回未登录错误");
            Result error = Result.error("NOT_LOGIN");
            String notLogin = new ObjectMapper().writeValueAsString(error);
            resp.getWriter().write(notLogin);
            //这三行同上
            return false;
        }

        // 6. 放行
        log.info("令牌合法，放行");
        return true;
    }

    // 目标资源方法运行后运行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle ...");
    }

    // 视图渲染完毕后运行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion ...");
    }
}