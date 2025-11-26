package com.eklps.tlias.service;

import com.eklps.tlias.mapper.EmpMapper;
import com.eklps.tlias.pojo.Emp;
import com.eklps.tlias.pojo.PageBean;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService{
    @Autowired
    private EmpMapper empMapper;

    @Override
    public PageBean page(Integer page, Integer pageSize,
                  String name, Short gender ,
                  LocalDate begin, LocalDate end){
        PageHelper.startPage(page,pageSize);//设置分页参数，开启分页
        List<Emp> empList=empMapper.list(name,gender,begin,end);
        Page<Emp> p=(Page<Emp>)empList;
        return new PageBean(p.getTotal(),p.getResult());
    }

    @Override
    public void delete(List<Integer> ids){
        empMapper.delete(ids);
    }

    @Override
    public void save(Emp emp){
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.insert(emp);
    }

    @Override
        public Emp getById(Integer id) {
            return empMapper.getById(id);
        }

    @Override
        public void update(Emp emp) {
            // 每次更新都要刷新“修改时间”
            emp.setUpdateTime(LocalDateTime.now());
            empMapper.update(emp);
        }

}
