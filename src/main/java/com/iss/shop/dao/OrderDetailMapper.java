package com.iss.shop.dao;

import com.iss.shop.domain.OrderDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderDetail record);

    int insertSelective(OrderDetail record);

    OrderDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderDetail record);

    int updateByPrimaryKey(OrderDetail record);

    void batchInsert(@Param("orderDetailList") List<OrderDetail> orderDetailList);

    List<OrderDetail> getByOrderNoUserId(@Param("orderNo")String orderNo, @Param("userId")Integer userId);

    List<OrderDetail> getByOrderNo(@Param("orderNo")String orderNo);


}