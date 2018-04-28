package com.grad.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.grad.common.Const;
import com.grad.common.ServerResponse;
import com.grad.dao.CategoryMapper;
import com.grad.dao.ProductMapper;
import com.grad.pojo.Category;
import com.grad.pojo.Product;
import com.grad.service.ICategoryService;
import com.grad.service.IProductService;
import com.grad.util.DateTimeUtil;
import com.grad.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }
            if (product.getId() != null) {
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("更新产品成功");
                }
                return ServerResponse.createByErrorMessage("更新产品失败");
            } else {
                int rowCount = productMapper.insert(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("新增产品成功");
                }
                return ServerResponse.createByErrorMessage("新增产品失败");
            }
        }
        return ServerResponse.createByErrorMessage("参数错误");
    }

    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorMessage("参数不正确");
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新产品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("更新产品销售状态失败");
    }

    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorMessage("产品ID为空");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("产品已下架或删除");
        }
        ProductDetailVo productDetailVo = assembProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);

    }

    private ProductDetailVo assembProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubTitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setContent(product.getContent());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setSaleStock(product.getSaleStock());

        // imageHost
        productDetailVo.setImageHost("ftp.server.http.prefix", "http://img.yq.com/");
        // parentCategoryId
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());

        if (category == null) {
            productDetailVo.setCategoryId(0);
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        // createdTime
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        // updateTime
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    public ServerResponse<PageInfo> getProductList(Integer pageSize, Integer nowPage) {

        // startPage - start
        PageHelper.startPage(nowPage, pageSize);
        //填充自己的sql逻辑
        List<Product> productList = productMapper.getProductList();
        // 关闭自己的pageHelper
        PageInfo pageResult = new PageInfo(productList);

        return ServerResponse.createBySuccess(pageResult);

    }

    public ServerResponse<PageInfo> searchProduct(Integer pageSize, Integer nowPage, String name) {
        PageHelper.startPage(nowPage, pageSize);
        if (StringUtils.isNotBlank(name)) {
            name = new StringBuilder().append("%").append(name).append("%").toString();
        }
        List<Product> productList = productMapper.selectProductByName(name);
        PageInfo pageResult = new PageInfo(productList);
        return ServerResponse.createBySuccess(pageResult);
    }

    public ServerResponse<Product> getProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("产品已下架或删除");
        }
        if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            return ServerResponse.createByErrorMessage("产品已下架或删除");
        }
        return ServerResponse.createBySuccess(product);
    }

    public ServerResponse<PageInfo> getProductByKeywordCategoryId(String keyword, Integer categoryId, Integer pageSize, Integer nowPage, String orderBy) {

        Category category = new Category();
        if (categoryId != null) {
            category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                // 没有该内容
                PageHelper.startPage(nowPage, pageSize);
                List<Product> productList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productList);
                return ServerResponse.createBySuccess(pageInfo);
            }

        }
        if (StringUtils.isNotBlank(keyword)) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(nowPage, pageSize);
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.ProductListOrderBy.ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                if (orderByArray.length > 2) {
                    PageHelper.orderBy(orderByArray[0] + "_" + orderByArray[1] + " " + orderByArray[2]);
                } else {
                    PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
                }
            }
        }
        List<Product> productList = productMapper.selectByNameAndCategoryId(StringUtils.isBlank(keyword) ? null : keyword, category.getId() == null ? null : category.getId());
        PageInfo pageInfo = new PageInfo(productList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    public ServerResponse deleteProducts(List<Integer> ids) {
        if (ids.isEmpty()) {
            return ServerResponse.createByErrorMessage("请选择要删除的商品");
        }
        return ServerResponse.createBySuccess(productMapper.deleteProducts(ids));

    }
}
