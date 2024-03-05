package com.ipp.controller;

import com.ipp.pojo.Category;
import com.ipp.pojo.Result;
import com.ipp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 新增文章分类
    @PostMapping
    public Result add(@RequestBody @Validated(Category.Add.class) Category category){
        categoryService.add(category);
        return Result.success();
    }

    @GetMapping
    // 查询文章分类列表(all)
    public Result<List<Category>> list(){
        List<Category> cs = categoryService.list();
        return Result.success(cs);
    }

    // 查询文章分类信息(One) -> 回写
    @GetMapping("/{id}")
    public Result<Category> detail(@PathVariable Integer id){
        Category category = categoryService.getById(id);
        return Result.success(category);
    }

    // 更新文章分类信息
    @PutMapping
    public Result update(@RequestBody @Validated(Category.Update.class) Category category){
        categoryService.update(category);
        return Result.success();
    }
    // 删除文章分类信息
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        categoryService.detele(id);
        return Result.success();
    }
}
