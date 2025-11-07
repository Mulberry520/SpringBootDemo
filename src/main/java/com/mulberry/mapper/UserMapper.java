package com.mulberry.mapper;

import com.mulberry.dto.UserDTO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("select * from users where username = #{username}")
    UserDTO selectByName(@Param("username") String name);

    @Insert("insert into users (username, password, nickname, email) values (#{username}, #{password}, #{nickname}, #{email})")
    int insertWithBasicInfo(
            @Param("username") String name,
            @Param("password") String passwd,
            @Param("nickname") String nickname,
            @Param("email") String email
    );

    @Update("update users set password = #{password}, nickname = #{nickname}, email = #{email} where username = #{username}")
    int updateBasicInfo(
            @Param("username") String name,
            @Param("password") String passwd,
            @Param("nickname") String nickname,
            @Param("email") String email
    );
}
