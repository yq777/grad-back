package com.grad.dao;

import com.grad.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteByUserIdAndId(@Param("id") Integer id, @Param("userId") Integer userId);

    int updateByShipping(Shipping shipping);

    List<Shipping> selectByUserId(Integer userId);
}