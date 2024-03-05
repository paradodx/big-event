package com.ipp.service;

import com.ipp.pojo.Category;

import java.util.List;

public interface CategoryService {
    // 新增文章分类
    void add(Category category);

    // 查询文章分类列表
    List<Category> list();

    // 查询单个文章分类信息
    Category getById(Integer id);

    // 更新文章分类信息
    void update(Category category);

    // 删除文章分类信息
    void detele(Integer id);
}
