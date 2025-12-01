package com.eklps.tlias.mapper;

import com.eklps.tlias.pojo.Emp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface EmpMapper {
    //引入xml文件而非注解
    List<Emp> list(String name, Short gender, LocalDate begin,LocalDate end);

    //引入xml文件
    void delete(List<Integer> ids);

    //添加新员工,依旧动态XML
    void insert(Emp emp);

    @Select("select * from emp where id=#{id}")
    Emp getById(Integer id);

    //修改员工
    void update(Emp emp);


    // 根据部门ID删除该部门下的所有员工
    void deleteByDeptId(Integer deptId);

    // 根据用户名和密码查询员工
    @Select("select * from emp where username = #{username} and password = #{password}")
    Emp getByUsernameAndPassword(Emp emp);
}
