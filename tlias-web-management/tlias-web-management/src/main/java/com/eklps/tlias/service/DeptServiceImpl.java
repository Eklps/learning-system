package com.eklps.tlias.service;

import com.eklps.tlias.mapper.DeptMapper;
import com.eklps.tlias.mapper.EmpMapper;
import com.eklps.tlias.pojo.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService{

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private EmpMapper empMapper;

    @Override
    public List<Dept> list(){
        return deptMapper.list();
    }

//    @Override
//    public void deleteById(Integer id){
//        deptMapper.deleteById(id);
//    }

    /**
     * 根据ID删除部门 (包含级联删除员工)
     * @Transactional: 开启事务。方法运行前开启事务，成功则提交，报错则回滚。
     * rollbackFor: 指定遇到什么异常才回滚。Exception.class 表示遇到任何异常都回滚。
     */
    @Transactional(rollbackFor = Exception.class) // 4. 加上这个注解！
    @Override
    public void deleteById(Integer id) {

        // 步骤1: 先删除该部门下的所有员工
        empMapper.deleteByDeptId(id);


        // 步骤2: 再删除部门本身
        deptMapper.deleteById(id);
    }

    @Override
    public void save(Dept dept){
        LocalDateTime now=LocalDateTime.now();
        dept.setCreateTime(now);
        dept.setUpdateTime(now);
        deptMapper.insert(dept);
    }

    @Override
    public Dept getById(Integer id){
        return deptMapper.getByID(id);
    }

    @Override
    public void update(Dept dept){
        LocalDateTime now=LocalDateTime.now();
        dept.setUpdateTime(now);
        deptMapper.update(dept);
    }
}
