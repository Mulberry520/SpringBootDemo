package com.mulberry.mapper;

import com.mulberry.entity.TbAccount;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface TbAccountMapper {
    @Select("select * from tb_account")
    List<TbAccount> selectAll();

    @Select("select * from tb_account where id = #{id}")
    TbAccount selectById(@Param("id") Integer id);

    @Select("select * from tb_acount wehre name = #{name}")
    Map<String, Object> selectByName1(@Param("name") String name);

    @Select("select * from tb_account where name = #{name} limit 1")
    TbAccount selectByName2(@Param("name") String name);

    @Select("select * from tb_account where name = #{name} and passwd = #{passwd}")
    TbAccount selectByNameAndPasswd(
            @Param("name") String name,
            @Param("passwd") String passwd
    );

    @Delete("delete from tb_account where id = #{id}")
    boolean deleteById(@Param("id") Integer id);

    @Insert("insert into tb_account values (null, #{name}, #{sex}, #{passwd}, #{email})")
    boolean insertAccount(
            @Param("name") String name,
            @Param("sex") String sex,
            @Param("passwd") String passwd,
            @Param("email") String email
    );

    @Insert("insert into tb_account values (#{id}, #{name}, #{sex}, #{passwd}, #{email})")
    boolean insertAccountWithId(
            @Param("id") Integer id,
            @Param("name") String name,
            @Param("sex") String sex,
            @Param("passwd") String passwd,
            @Param("email") String email
    );

    @Update("update tb_account set name = #{name} where id = #{id}")
    boolean updateNameById(
            @Param("name") String name,
            @Param("id") Integer id
    );

    @Update("update tb_account set email = #{email} where id = #{id}")
    boolean updateEmailById(
            @Param("email") String email,
            @Param("id") Integer id
    );
}
