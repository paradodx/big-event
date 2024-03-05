package com.ipp.service;

import com.ipp.pojo.Article;
import com.ipp.pojo.PageBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArticleService {
    // 新增
    void add(Article article);
    // 分页条件查询
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);
    // 获取文章信息
    Article getById(Integer id);
    // 更新文章信息
    void update(Article article);
    // 删除
    void delete(List<Integer> ids);


}
