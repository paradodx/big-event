package com.ipp.controller;

import com.ipp.pojo.Article;
import com.ipp.pojo.PageBean;
import com.ipp.pojo.Result;
import com.ipp.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    // 新增文章
    @PostMapping
    public Result add(@RequestBody @Validated Article article){
        articleService.add(article);
        return Result.success();
    }

    // 分页条件查询
    @GetMapping
    public Result<PageBean<Article>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "5") Integer pageSize,
                                          @RequestParam(required = false) Integer categoryId,
                                          @RequestParam(required = false) String state){
        PageBean<Article> pageBean =  articleService.list(pageNum, pageSize, categoryId, state);
        return Result.success(pageBean);
    }

    // 获取文章 -> One
    @GetMapping("/{id}")
    public Result<Article> detail(@PathVariable Integer id){
        Article article = articleService.getById(id);
        return Result.success(article);
    }

    // 修改文章
    @PutMapping
    public Result update(@RequestBody Article article){
        articleService.update(article);
        return Result.success();
    }

    // 删除文章
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable List<Integer> ids){
        articleService.delete(ids);
        return Result.success();
    }
}
