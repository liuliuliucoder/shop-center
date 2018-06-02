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

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByUserIdAndOrderNo(@Param("userId")Integer userId, @Param("orderNo")String orderNo);

    List<Order> selectByUserId(Integer userId);

    List<Order> searchOrderByConditions(OrderQuery query);

}