package com.ipp.mapper;

import com.ipp.pojo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    // 新增
    @Insert("insert into article(title, content, cover_img, state, category_id, create_user, create_time, update_time) " +
            "VALUES(#{title},#{content},#{coverImg},#{state},#{categoryId},#{createUser},#{createTime},#{updateTime})")
    void add(Article article);

    // 查询
    List<Article> list(Integer userId, Integer categoryId, String state);

    // 根据id获取数据
    @Select("select * from article where id = #{id}")
    Article getById(Integer id);

    // 更新
    void update(Article article);

    // 删除
    void delete(List<Integer> ids);



}
