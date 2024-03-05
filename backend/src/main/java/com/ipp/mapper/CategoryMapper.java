package com.ipp.mapper;

import com.ipp.pojo.Category;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface CategoryMapper {

    // 新增
    @Insert("insert into category(category_name, category_alias, create_user, create_time, update_time) VALUES " +
            "(#{categoryName},#{categoryAlias},#{createUser},#{createTime},#{updateTime})")
    void add(Category category);
    // 查询All
    @Select("select id, category_name, category_alias, create_user, create_time, update_time from category where " +
            "create_user = #{userId}")
    List<Category> list(Integer userId);
    // 根据id查询
    @Select("select id, category_name, category_alias, create_user, create_time, update_time from category where id =" +
            " #{id}")
    Category getById(Integer id);
    // 更新
    @Update("update category set category_name = #{categoryName}, category_alias = #{categoryAlias}, update_time " +
            "= #{updateTime} where id = #{id}")
    void update(Category category);
    // 删除
    @Delete("delete from category where id = #{id}")
    void delete(Integer id);
}
