package com.eklps.tlias.exception;

import com.eklps.tlias.pojo.Result;
import lombok.extern.slf4j.Slf4j; // 导入日志注解
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice // 核心注解：表示这是一个全局处理类，对所有 Controller 生效
public class GlobalExceptionHandler {

    /**
     * 捕获所有异常 (Exception)
     */
    @ExceptionHandler(Exception.class) // 指定要捕获的异常类型
    public Result ex(Exception e){
        // 1. 打印堆栈信息到控制台 (方便我们开发人员调试)
        e.printStackTrace();
        log.error("程序运行报错: {}", e.getMessage());

        // 2. 返回统一的错误结果给前端
        return Result.error("对不起，操作失败，请联系管理员");
    }
}