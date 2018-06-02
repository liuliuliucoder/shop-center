package com.iss.shop.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class CategoryServiceTest {

    @Autowired CategoryService categoryService;
    @Test
    public void addCategory(){
        String categoryName = "吸尘器";
        Integer parentId = 1;
        categoryService.addCategory(categoryName,parentId);
    }

    @Test
    public void updateCategoryName(){
        String categoryName = "扫地机器人";
        Integer categoryId = 31;
        categoryService.updateCategoryName(categoryId,categoryName);
    }

    @Test
    public void getChildrenParallelCategory(){
        Integer categoryId = 1;
        categoryService.getChildrenParallelCategory(categoryId);
    }

    @Test
    public void selectCategoryAndChildrenById(){
        Integer categoryId = 1;
        categoryService.selectCategoryAndChildrenById(categoryId);
    }
}
