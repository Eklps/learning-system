package com.eklps.tlias.aop;

import com.alibaba.fastjson.JSONObject; // 用来把参数转JSON字符串 (需要引入fastjson依赖，如果没有可以用Jackson)
import com.eklps.tlias.mapper.OperateLogMapper;
import com.eklps.tlias.pojo.OperateLog;
import com.eklps.tlias.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Component // 交给 Spring 管理
@Aspect    // 声明这是一个切面类
public class LogAspect {

    @Autowired
    private HttpServletRequest request; // 注入请求对象，为了获取 token 中的用户ID

    @Autowired
    private OperateLogMapper operateLogMapper; // 注入 Mapper，为了插数据

    /**
     * 环绕通知：拦截所有加了 @Log 注解的方法
     */
    @Around("@annotation(com.eklps.tlias.anno.Log)")
    //简单来说，它的作用就是 “找到所有被 @Log 注解标记的方法，
    // 然后将我的通知（Advice）代码环绕（Around）在这些方法周围执行。”
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {

        // ------------------ 1. 方法运行前 (开始记录) ------------------
        long begin = System.currentTimeMillis();

        // 执行原始目标方法 (比如删除部门)
        Object result = joinPoint.proceed();

        // ------------------ 2. 方法运行后 (收集信息并入库) ------------------
        long end = System.currentTimeMillis();

        // 1. 操作人 ID (从 Request 的 token 中解析)
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        Integer operateUser = (Integer) claims.get("id");

        // 2. 操作时间
        LocalDateTime operateTime = LocalDateTime.now();

        // 3. 操作类名
        String className = joinPoint.getTarget().getClass().getName();

        // 4. 操作方法名
        String methodName = joinPoint.getSignature().getName();

        // 5. 方法参数 (转 JSON 字符串方便存储)
        Object[] args = joinPoint.getArgs();
        String methodParams = Arrays.toString(args);

        // 6. 返回值 (转 JSON 字符串)
        // 这里需要注意：如果返回值 Result 对象太长，数据库 varchar 可能存不下，实际开发中可以截取
        String returnValue = (result != null) ? result.toString() : "null";

        // 7. 耗时 (ms)
        long costTime = end - begin;

        // 构造日志对象
        OperateLog operateLog = new OperateLog(null, operateUser, operateTime, className, methodName, methodParams, returnValue, costTime);

        // 调用 Mapper 插入数据库
        operateLogMapper.insert(operateLog);

        log.info("AOP 记录操作日志: {}", operateLog);

        return result;
    }
}