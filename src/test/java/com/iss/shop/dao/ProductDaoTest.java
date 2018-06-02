package com.iss.shop.dao;

import com.iss.shop.domain.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ProductDaoTest {

    @Autowired
    private ProductMapper  productMapper;
    @Test
    public void selectByProductId(){
        Integer productId = 3;
        Product product= productMapper.selectByProductId(productId);
    }
}
