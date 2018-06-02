package com.iss.shop.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void add(){
        int userId = 1;
        int addressId = 0;
        orderService.createOrder(userId,addressId);
    }
}
