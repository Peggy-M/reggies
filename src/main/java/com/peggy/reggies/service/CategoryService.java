package com.peggy.reggies.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.peggy.reggies.entity.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
