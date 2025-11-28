package com.eklps.tlias.config;

import com.eklps.tlias.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 配置跨域映射
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许对所有路径 ("/**") 开放 CORS
        registry.addMapping("/**")
                // 允许 Vue 前端端口 (5173) 访问
                .allowedOrigins("http://localhost:5173")
                // 允许所有常用的请求方法 (GET, POST, PUT, DELETE)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 允许携带认证信息，如 Cookie
                .allowCredentials(true)
                .maxAge(3600);
    }



    /**
     * 配置静态资源映射
     * 这里的逻辑是：当请求以 /images/ 开头时，去本地 F:/javaMain/firstCompleteProject/images/ 找文件
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:F:/javaMain/firstCompleteProject/images/");
    }


    @Autowired // 注入我们写好的拦截器
    private LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/login"); // 排除登录接口 (否则没人能登录了)
        // 如果你想排除静态资源，也可以加 .excludePathPatterns("/images/**")
    }
}