package com.mulberry.mapper;

import com.mulberry.dto.CategoryDTO;
import com.mulberry.dto.CategorySimpleDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Insert("insert into category (category_name, category_alias, create_user) values (#{category_name}, #{category_alias}, #{create_user})")
    int insertCategory(
            @Param("category_name") String name,
            @Param("category_alias") String alias,
            @Param("create_user") Integer userId
    );

    @Select("select id, category_name, category_alias from category where create_user = #{create_user}")
    List<CategorySimpleDTO> selectByCreator(@Param("create_user") Integer userId);

    @Select("select * from category where id = #{id} and create_user = #{create_user}")
    CategoryDTO selectById(
            @Param("create_user") Integer userId,
            @Param("id") Integer categoryId
    );

    @Select("select id from category where id = #{id}")
    Integer selectIdById(@Param("id") Integer categoryId);

    @Update("update category set category_name = #{category_name}, category_alias = #{category_alias} where id = #{id} and create_user = #{create_user}")
    int updateCategory(
            @Param("category_name") String newName,
            @Param("category_alias") String newAlias,
            @Param("create_user") Integer userId,
            @Param("id") Integer categoryId
    );

    @Delete("delete from category where id = #{id} and create_user = #{create_user}")
    int deleteCategory(
            @Param("create_user") Integer userId,
            @Param("id") Integer categoryId
    );
}
