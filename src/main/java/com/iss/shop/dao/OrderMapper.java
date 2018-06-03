package com.iss.shop.dao;

import com.iss.shop.domain.Order;
import com.iss.shop.domain.OrderQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int updateByPrimaryKeySelective(Order record);

    Order selectByUserIdAndOrderNo(@Param("userId")Integer userId, @Param("orderNo")String orderNo);

    List<Order> selectByUserId(Integer userId);

    List<Order> searchOrderByConditions(OrderQuery query);

    List<Order> getAllOrder();

    Order getOrderByOrderId(Integer id);

    int updateOrderStatus(@Param("id")Integer id, @Param("status")Integer status);

}