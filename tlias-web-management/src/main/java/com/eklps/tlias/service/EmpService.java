package com.eklps.tlias.service;


import com.eklps.tlias.pojo.Emp;
import com.eklps.tlias.pojo.PageBean;

import java.time.LocalDate;
import java.util.List;

public interface EmpService {
    PageBean page(Integer page, Integer pageSize, String name, Short gender , LocalDate begin,LocalDate end);

    void delete(List<Integer> ids);

    void save(Emp emp);

    void update(Emp emp);

    Emp getById(Integer id);
}
