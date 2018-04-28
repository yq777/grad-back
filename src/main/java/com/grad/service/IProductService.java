package com.grad.service;

import com.github.pagehelper.PageInfo;
import com.grad.common.ServerResponse;
import com.grad.pojo.Product;
import com.grad.vo.ProductDetailVo;

import java.util.List;

public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(Integer pageSize, Integer nowPage);

    ServerResponse<PageInfo> searchProduct(Integer pageSize, Integer nowPage, String name);

    ServerResponse<Product> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordCategoryId(String keyword, Integer categoryId, Integer pageSize, Integer nowPage, String orderBy);

    ServerResponse deleteProducts(List<Integer> ids);
}
