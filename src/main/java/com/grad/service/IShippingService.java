package com.grad.service;

import com.github.pagehelper.PageInfo;
import com.grad.common.ServerResponse;
import com.grad.pojo.Shipping;

public interface IShippingService {
    ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse delete(Integer userId, Integer shippingId);

    ServerResponse update(Integer userId, Shipping shipping);

    ServerResponse<PageInfo> select(Integer pageSize, Integer nowPage, Integer userId);
}
