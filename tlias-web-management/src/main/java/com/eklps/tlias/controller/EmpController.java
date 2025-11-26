package com.eklps.tlias.controller;

import com.eklps.tlias.pojo.Emp;
import com.eklps.tlias.pojo.PageBean;
import com.eklps.tlias.pojo.Result;
import com.eklps.tlias.service.EmpServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {
    @Autowired
    private EmpServiceImpl empService;

    @GetMapping
    public Result page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String name,
            Short gender,
            @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ){
        PageBean pageBean=empService.page(page,pageSize,name,gender,begin,end);
        return Result.success(pageBean);
    }

    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable List<Integer> ids){
        empService.delete(ids);
        return Result.success();
    }

    @PostMapping
    public Result save(@RequestBody Emp emp){
        log.info("新增员工:{}",emp.getName());
        empService.save(emp);
        return Result.success();
    }


    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info("根据ID查询员工信息: {}", id);
        Emp emp = empService.getById(id);
        return Result.success(emp);
    }

    @PutMapping
    public Result update(@RequestBody Emp emp){
        log.info("更新员工信息: {}", emp);
        empService.update(emp);
        return Result.success();
    }
}
