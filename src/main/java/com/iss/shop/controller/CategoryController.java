package com.iss.shop.controller;

import com.iss.shop.domain.Category;
import com.iss.shop.service.CategoryService;
import com.iss.shop.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/find")
    @ResponseBody
    public Result findCategory() {
        Result result = new Result();
        List<Category> categoryList =categoryService.selectForParent();
        result.setContent(categoryList);
        return result;
    }

    @RequestMapping("/findById")
    @ResponseBody
    public Result findCategory(int id) {
        Result result = new Result();
        List<Category> categoryList =categoryService.selectCategoryAndChildrenById(id);
        result.setContent(categoryList);
        return result;
    }

    @RequestMapping("/findBySubCategory")
    @ResponseBody
    public Result findBySubCategory() {
        Result result = new Result();
        List<Category> categoryList =categoryService.selectForSubCategory();
        result.setContent(categoryList);
        return result;
    }

}
