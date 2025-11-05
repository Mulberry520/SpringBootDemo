package com.mulberry.mapper;

import com.mulberry.dto.UserDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from users where username = #{username}")
    UserDTO selectByName(@Param("username") String name);

    @Insert("insert into users (username, password, nickname, email, user_pic) values (#{username}, #{password}, #{nickname}, #{email}, #{user_pic})")
    int insertWithBasicInfo(
            @Param("username") String name,
            @Param("password") String passwd,
            @Param("nickname") String nickname,
            @Param("email") String email,
            @Param("user_pic") String userPic
    );
}
