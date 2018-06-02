package com.iss.shop.service;

import com.iss.shop.domain.ProductDetailVo;
import com.iss.shop.util.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Test
    public void detail(){
        Result<ProductDetailVo> result = new Result();
        Integer productId = 1;
        result = productService.getProductDetail(productId);
    }
}
