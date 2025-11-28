package com.eklps.tlias.controller;

import com.eklps.tlias.pojo.Emp;
import com.eklps.tlias.pojo.Result;
import com.eklps.tlias.service.EmpService;
import com.eklps.tlias.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private EmpService empService;

    @PostMapping("/login")
    public Result login(@RequestBody Emp emp){
        log.info("员工登录: {}", emp);

        // 1. 调用 Service 查询数据库
        Emp e = empService.login(emp);

        // 2. 判断：登录成功，生成令牌，下发令牌
        if(e != null){
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", e.getId());
            claims.put("name", e.getName());
            claims.put("username", e.getUsername());

            // 生成 JWT 令牌
            String jwt = JwtUtils.generateJwt(claims);

            // 把令牌返回给前端
            return Result.success(jwt);
        }

        // 3. 登录失败
        return Result.error("用户名或密码错误");
    }
}