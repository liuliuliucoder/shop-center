package com.iss.shop.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class CartServiceTest {

    @Autowired
    private CartService cartService;
    @Test
    public void add(){
        int userId = 4;
        int productId = 4;
        int count=4;
        cartService.add(userId,productId,count);
    }

}
