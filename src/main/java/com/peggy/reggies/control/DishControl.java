package com.peggy.reggies.control;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peggy.reggies.commom.R;
import com.peggy.reggies.dto.DishDto;
import com.peggy.reggies.entity.Category;
import com.peggy.reggies.entity.Dish;
import com.peggy.reggies.entity.DishFlavor;
import com.peggy.reggies.service.CategoryService;
import com.peggy.reggies.service.DishFlavorService;
import com.peggy.reggies.service.impl.DishServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishControl {

    @Autowired
    DishServiceImpl dishService;

    @Autowired
    DishFlavorService dishFalvorService;

    @Autowired
    CategoryService categoryService;

    //列表
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){
        Page pageInfo = new Page(page, pageSize);
        Page dishDtoPage=new Page();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        //获取原records数据
        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();  //分类id
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }


    //添加菜品
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info("接收的dishDto数据：{}",dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("添加成功");
    }

    //获取菜品信息
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        //查询
        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info("接收的dishDto数据：{}",dishDto.toString());
        //更新数据库中的数据
        dishService.updateWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }



    @PostMapping("/status/{status}")
    //这个参数这里一定记得加注解才能获取到参数，否则这里非常容易出问题
    public R<String> status(@PathVariable("status") Integer status,@RequestParam List<Long> ids){
        //log.info("status:{}",status);
        //log.info("ids:{}",ids);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(ids !=null,Dish::getId,ids);
        //根据数据进行批量查询
        List<Dish> list = dishService.list(queryWrapper);

        for (Dish dish : list) {
            if (dish != null){
                dish.setStatus(status);
                dishService.updateById(dish);
            }
        }
        return R.success("售卖状态修改成功");
    }
    /**
     * 套餐批量删除和单个删除
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") List<Long> ids){

        LambdaQueryWrapper<Dish> DishQueryWrapper = new LambdaQueryWrapper<>();
        DishQueryWrapper.in(Dish::getId,ids);
        //删除菜品
        dishService.remove(DishQueryWrapper);

        //删除菜品对应的口味  也是逻辑删除
        LambdaQueryWrapper<DishFlavor> dishFlavorQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorQueryWrapper.in(DishFlavor::getDishId,ids);
        dishFalvorService.remove(dishFlavorQueryWrapper);
        return R.success("菜品删除成功");
    }
}
