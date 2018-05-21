package com.grad.service;

import com.github.pagehelper.PageInfo;
import com.grad.common.ServerResponse;
import com.grad.vo.OrderVo;

import java.util.Map;

public interface IOrderService {
    ServerResponse pay(Long orderNo, Integer userId, String path);

    ServerResponse aliCallBack(Map<String, String> params);

    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);

    ServerResponse create(Integer userId, Integer shipping);

    ServerResponse<String> cancel(Integer userId, Long orderNo);

    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);

    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);

    ServerResponse<PageInfo> manageList(int pageNum, int pageSize);

    ServerResponse<OrderVo> manageDetail(Long orderNo);

    ServerResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);

    ServerResponse<String> manageSendGoods(Long orderNo);

    ServerResponse<String> manageConfirmGetGoods(Long orderNo);

    ServerResponse delete(Integer userId, Long orderNo);
}
