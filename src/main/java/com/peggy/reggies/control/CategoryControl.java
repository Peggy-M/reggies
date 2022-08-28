package com.peggy.reggies.control;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peggy.reggies.commom.R;
import com.peggy.reggies.entity.Category;
import com.peggy.reggies.service.CategoryService;
import com.peggy.reggies.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryControl {

    @Autowired
    CategoryService categoryService;

    @Autowired
    DishService dishService;



    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize){

        //创建分页构造器
        Page pageInfo = new Page(page, pageSize);
        //添加条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.orderByAsc(Category::getSort);
        Page pages = categoryService.page(pageInfo,queryWrapper);
        log.info("查询到的用户的信息{}",pages.getRecords());
        return R.success(pages);
    }

    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("添加用户"+category.getName()+"成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("菜品的id是{}",category.getId());
        boolean b = categoryService.updateById(category);
        log.info("修改的信息结果是:{}",b);
        return R.success("菜品更新成功");
    }

    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("菜品的id:{}",ids);
        categoryService.remove(ids);
        return R.success("删除成功");
    }
}
