package com.grad.service;

import com.grad.common.ServerResponse;
import com.grad.pojo.Category;

import java.util.List;

public interface ICategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> getChildrenParallerlCategory(Integer parentId);

    ServerResponse deleteCategory(List<Integer> ids);
}
