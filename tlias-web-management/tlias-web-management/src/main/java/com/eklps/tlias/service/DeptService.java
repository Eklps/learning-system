package com.eklps.tlias.service;

import com.eklps.tlias.pojo.Dept;

import java.util.List;

public interface DeptService {
    List<Dept> list();

    void deleteById(Integer id);

    //保存dept
    void save(Dept dept);

    //获取特定id的dept信息
    Dept getById(Integer id);

    //修改指定id的dept信息
    void update(Dept dept);
}
