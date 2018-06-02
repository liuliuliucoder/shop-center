package com.iss.shop.controller;

import com.iss.shop.domain.ProductQuery;
import com.iss.shop.service.ProductService;
import com.iss.shop.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    //    public Result<ProductDetailVo> getProductDetail( Integer productId){
//        return productService.getProductDetail(productId);
//   }
    @GetMapping("/detail")
    @ResponseBody
    public Result getProductDetail(Integer id) {
        Result result = productService.getProductDetail(id);
        return result;
    }

    /**
     * 商品搜索及动态排序
     */
//    @GetMapping("/list")
//    public String list(Model model, ProductQuery query){
//        String keyword = query.getKeyword();
//        Integer categoryId = query.getCategoryId();
//        String orderBy = query.getOrderBy();
//        Integer pageNum = query.getPageNum();
//        Integer pageSize = query.getPageSize();
//        Result result = productService.getProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
//        model.addAttribute("list",result.getContent());
//        return "list";
//   }

    /**
     * 商品搜索及动态排序
     */
    @GetMapping("/list")
    @ResponseBody
    public Result list(ProductQuery query) {
        String keyword = query.getKeyword();
        Integer categoryId = query.getCategoryId();
        String orderBy = query.getOrderBy();
        Integer pageNum = query.getPageNum();
        Integer pageSize = query.getPageSize();
        Result result = productService.getProductByKeywordCategory(keyword, categoryId, pageNum, pageSize, orderBy);
        return result;
    }

    @GetMapping("/findProductById")
    @ResponseBody
    public Result findProductById(Integer categoryId) {
        Result result = productService.findProductByCategory(categoryId);
        return result;
    }

}
