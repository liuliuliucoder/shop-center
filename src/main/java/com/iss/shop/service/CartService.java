package com.iss.shop.service;

import com.iss.shop.domain.CartProductVo;
import com.iss.shop.domain.CartVo;
import com.iss.shop.util.Result;

public interface CartService {
    /**
     * 添加购物车
     */
    Result add(Integer userId, Integer productId, Integer count);
    Result<CartProductVo> list(Integer userId);
    Result<CartProductVo> update(Integer userId, Integer productId, Integer count);
    Result<CartProductVo> deleteOne(Integer userId, Integer productId);
    Result<CartProductVo> deleteProduct(Integer userId, String productIds);
    Result<CartProductVo> selectOrUnSelect(Integer userId, Integer productId, Integer checked);
    Result<Integer> getCartProductCount(Integer userId);
}
