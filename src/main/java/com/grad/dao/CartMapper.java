package com.grad.dao;

import com.grad.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<Cart> selectCartByUserID(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteProducts(@Param("userId") Integer userId, @Param("productList") List<String> productList);

    List<Cart> selectCheckedCartByUserId(Integer userId);

    void updateUnChecked(Integer userId);

    void updateCheckedByUserIdAndProductId(@Param("userId") Integer userId, @Param("productList") List<String> productList);
}