package com.grad.dao;

import com.google.common.collect.Lists;
import com.grad.common.ServerResponse;
import com.grad.pojo.Product;
import com.grad.vo.ProductDetailVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> getProductList();

    List<Product> selectProductByName(String name);

    List<Product> selectByNameAndCategoryId(@Param("name") String name, @Param("categoryId") Integer categoryId);

    int deleteProducts(List<Integer> ids);

    void updateBatch(@Param("productList") List<Product> productList);
}