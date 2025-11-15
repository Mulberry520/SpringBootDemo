package com.mulberry.mapper;

import com.mulberry.dto.ArticleDTO;
import com.mulberry.dto.ArticleSimpleDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Select("select * from article where create_user = #{create_user}")
    List<ArticleSimpleDTO> selectAllByCreator(@Param("create_user") Integer userId);

    @Select("select * from article where create_user = #{create_user} limit #{offset}, #{size}")
    List<ArticleSimpleDTO> selectSomeByCreator(
            @Param("create_user") Integer usrId,
            @Param("offset") int offset,
            @Param("size") int size
    );

    @Select("select * from article where create_user = #{create_user} and id = #{id}")
    ArticleDTO selectDetailById(
            @Param("create_user") Integer userId,
            @Param("id") Integer articleId
    );

    @Insert("insert into article (title, content, cover_img, state, category_id, create_user) values (#{title}, #{content}, #{cover_img}, #{state}, #{category_id}, #{create_user})")
    int insertArticle(
            @Param("title") String title,
            @Param("content") String content,
            @Param("cover_img") String coverUrl,
            @Param("state") String state,
            @Param("category_id") Integer categoryId,
            @Param("create_user") Integer userId
    );

    @Update("update article set title = #{title}, content = #{content}, cover_img = #{cover_img}, state = #{state}, category_id = #{category_id} where id = #{id}")
    int updateArticle(
            @Param("title") String title,
            @Param("content") String content,
            @Param("cover_img") String coverUrl,
            @Param("state") String state,
            @Param("category_id") Integer categoryId,
            @Param("id") Integer articleId
    );

    @Select("select cover_img from article where id = #{id} and create_user = #{create_user}")
    String selectCoverByIdAndName(
            @Param("id") Integer articleId,
            @Param("create_user") Integer userId
    );

    @Update("update article set cover_img = #{cover_img} where id = #{id} and create_user = #{create_user}")
    int updateCoverByIdAndName(
            @Param("id") Integer articleId,
            @Param("create_user") Integer userId,
            @Param("cover_img") String coverUrl
    );
}
