package com.eklps.tlias.mapper;

import com.eklps.tlias.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {

    @Select("select * from dept")
    List<Dept> list();

    @Delete("delete from dept where id = #{id}")
    void deleteById(Integer id);

    @Insert("insert into dept (name,create_time,update_time) values(#{name},#{createTime},#{updateTime})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void insert(Dept dept);

    //查询具体的单个dept
    @Select("select * from dept where id = #{id}")
    Dept getByID(Integer id);

    //根据id修改dept(的名字)
    @Update("update dept set name= #{name} ,update_time=#{updateTime} where id =#{id}")
    void update(Dept dept);
}
