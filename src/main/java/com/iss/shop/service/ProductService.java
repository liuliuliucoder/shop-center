package com.iss.shop.service;

import com.iss.shop.domain.Product;
import com.iss.shop.domain.ProductDetailVo;
import com.iss.shop.domain.ProductQuery;
import com.iss.shop.util.Result;

public interface ProductService {

     Result saveOrUpdateProduct(Product product);

     Result<ProductDetailVo> getProductDetail(Integer productId);

     Result getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);

     Result setSaleStatus(Integer productId, Integer status);

     Result manageProductDetail(Integer productId);

     Result getProductList(int pageNum, int pageSize);

     Result searchProduct(String productName, Integer productId, int pageNum, int pageSize);

     Result searchByConditions(ProductQuery query);

     Result findProductByCategory(Integer categoryId);

     Result getCategoryList();

     Result delProduct(ProductQuery query);

}
