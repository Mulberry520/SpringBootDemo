package com.mulberry.mapper;

import com.mulberry.entity.Car;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CarMapper {
    @Select("select * from car")
    List<Car> selectAll();

    @Select("select * from car where id = #{id}")
    Car selectById(@Param("id") Integer id);
}
