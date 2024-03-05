package com.ipp.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ipp.mapper.ArticleMapper;
import com.ipp.pojo.Article;
import com.ipp.pojo.PageBean;
import com.ipp.service.ArticleService;
import com.ipp.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void add(Article article) {
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        article.setCreateUser(userId);
        articleMapper.add(article);
    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        // 创建pageBean对象
        PageBean<Article> pageBean = new PageBean<>();
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        // 调用Mapper
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        List<Article> as = articleMapper.list(userId, categoryId, state);
        // Page中提供了 可以获取PageHelper分页查询后 得到的总记录条数和当页数据 的方法
        Page<Article> p = (Page<Article>) as;
        // 把数据填充到PageBean对象中
        pageBean.setTotal(p.getTotal());
        pageBean.setItems(p.getResult());
        return pageBean;
    }

    @Override
    public Article getById(Integer id) {
        return articleMapper.getById(id);
    }

    @Override
    public void update(Article article) {
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.update(article);
    }

    @Override
    public void delete(List<Integer> ids) {
        articleMapper.delete(ids);
    }


}
