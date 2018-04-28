package com.grad.service.impl;

import com.grad.common.ServerResponse;
import com.grad.dao.CategoryMapper;
import com.grad.pojo.Category;
import com.grad.service.ICategoryService;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("更新品类名成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名错误");
    }

    public ServerResponse<List<Category>> getChildrenParallerlCategory(Integer parentId) {
        return ServerResponse.createBySuccess(categoryMapper.selectCategoryChildrenByParentId(parentId));
    }

    public ServerResponse deleteCategory(List<Integer> ids) {
        if (ids.isEmpty()) {
            return ServerResponse.createByErrorMessage("请选择要删除的品类");
        }
        return ServerResponse.createBySuccess(categoryMapper.deleteCategoryById(ids));
    }
}
