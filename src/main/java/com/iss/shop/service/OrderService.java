package com.iss.shop.service;

import com.iss.shop.domain.OrderQuery;
import com.iss.shop.domain.OrderVo;
import com.iss.shop.util.Result;

public interface OrderService {
    Result createOrder(Integer userId, Integer addressId);
    Result<String> cancel(Integer userId, String orderNo);
    Result getOrderCartProduct(Integer userId);
    Result<OrderVo> getOrderDetail(Integer userId, String orderNo);
    Result getOrderList(int pageNum, int pageSize);
    Result<OrderVo> searchOrderByConditions(OrderQuery query);
    Result getOrderByOrderId(OrderQuery query);
    Result updateOrder(OrderVo orderVo);
    Result getMyOrder(Integer userId);
    Result updateOrderStatus(Integer id);
    Result getOrderDetailByOrderNo(Integer userId, Integer orderId);

}
