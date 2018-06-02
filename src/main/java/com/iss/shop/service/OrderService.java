package com.iss.shop.service;

import com.iss.shop.domain.OrderQuery;
import com.iss.shop.domain.OrderVo;
import com.iss.shop.util.Result;

public interface OrderService {
    Result createOrder(Integer userId, Integer addressId);
    Result<String> cancel(Integer userId, String orderNo);
    Result getOrderCartProduct(Integer userId);
    Result<OrderVo> getOrderDetail(Integer userId, String orderNo);
    Result getOrderList(Integer userId, int pageNum, int pageSize);
    Result<OrderVo> searchOrderByConditions(OrderQuery query);
//    Result pay(Long orderNo, Integer userId, String path);
//    Result aliCallback(Map<String, String> params);
//    Result queryOrderPayStatus(Integer userId, Long orderNo);
//    Result<PageInfo> manageList(int pageNum, int pageSize);
//    Result<OrderVo> manageDetail(Long orderNo);
//    Result<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);
//    Result<String> manageSendGoods(Long orderNo);


}
