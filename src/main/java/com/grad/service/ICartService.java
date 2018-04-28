package com.grad.service;

import com.grad.common.ServerResponse;
import com.grad.vo.CartVo;

import java.util.List;

public interface ICartService {
    ServerResponse<CartVo> add(Integer userId, Integer count, Integer productId);

    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> delete(Integer userId, String cartIds);

    ServerResponse<CartVo> list(Integer userId);

    ServerResponse confirm(Integer userId, String productIds);

    ServerResponse<CartVo> getCheckedProduct(Integer userId);
}
