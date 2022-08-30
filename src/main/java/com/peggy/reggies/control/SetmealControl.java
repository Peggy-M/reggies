package com.peggy.reggies.control;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peggy.reggies.commom.R;
import com.peggy.reggies.dto.SetmealDto;
import com.peggy.reggies.entity.*;
import com.peggy.reggies.service.CategoryService;
import com.peggy.reggies.service.SetmealDisService;
import com.peggy.reggies.service.impl.SetmealServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealControl {


    @Autowired
    SetmealServiceImpl setmealService;

    @Autowired
    SetmealDisService setmealDisService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> list(int page,int pageSize,String name){
        //分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        //构造查询条件对象
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(name != null, Setmeal::getName, name);

        //操作数据库
        setmealService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            //获取categoryId
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("数据传输对象setmealDto:{}",setmealDto.toString());
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> getByIdWithFlavor(@PathVariable  Long id){
        SetmealDto setmealDto = setmealService.getByIdWithFlavor(id);
        return R.success(setmealDto);
    }

    @PutMapping
    public R<String>  updateWithFlavor(@RequestBody SetmealDto setmealDto){
        log.info("接收的dishDto数据：{}",setmealDto.toString());
        setmealService.updateWithFlavor(setmealDto);
        return R.success("新增菜品成功");
    }

    @PostMapping("/status/{status}")
    //这个参数这里一定记得加注解才能获取到参数，否则这里非常容易出问题
    public R<String> status(@PathVariable("status") Integer status,@RequestParam List<Long> ids){
        //log.info("status:{}",status);
        //log.info("ids:{}",ids);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(ids !=null,Setmeal::getId,ids);
        //根据数据进行批量查询
        List<Setmeal> list = setmealService.list(queryWrapper);

        for (Setmeal setmeal : list) {
            if (setmeal != null){
                setmeal.setStatus(status);
                setmealService.updateById(setmeal);
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

        LambdaQueryWrapper<Setmeal> DishQueryWrapper = new LambdaQueryWrapper<>();
        DishQueryWrapper.in(Setmeal::getId,ids);
        //删除菜品
        setmealService.remove(DishQueryWrapper);

        //删除菜品对应的口味  也是逻辑删除
        LambdaQueryWrapper<SetmealDish> dishFlavorQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorQueryWrapper.in(SetmealDish::getDishId,ids);
        setmealDisService.remove(dishFlavorQueryWrapper);
        return R.success("菜品删除成功");
    }
}
