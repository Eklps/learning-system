package com.eklps.tlias.controller;

import com.eklps.tlias.anno.Log;
import com.eklps.tlias.pojo.Dept;
import com.eklps.tlias.pojo.Result;
import com.eklps.tlias.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//部门管理controller
@Slf4j
@RestController
@RequestMapping("/depts") // <--- 建议在类上使用 @RequestMapping 统一前缀
public class DeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping
    public Result list(){
        log.info("查询全部部门数据");
        List<Dept> deptList=deptService.list();
        return Result.success(deptList);
    }
    @Log
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id){
        log.info("根据id删除部门：",id);
        deptService.deleteById(id);
        return Result.success();
    }
    @Log
    @PostMapping
    public Result add(@RequestBody Dept dept){
        log.info("正在添加dept:{}",dept.getName());
        deptService.save(dept);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info("正在根据id：{}查询",id);
        Dept d=deptService.getById(id);
        return Result.success(d);
    }
    @Log
    @PutMapping()
    public Result update(@RequestBody Dept dept){
        log.info("正在修改dept{}数据",dept.getName());
        deptService.update(dept);
        return Result.success();
    }



}
