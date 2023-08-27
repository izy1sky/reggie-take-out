package org.sunshiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sunshiyi.entity.Category;

public interface CategoryService extends IService<Category> {
    void removeCategory(Long ids);
}
