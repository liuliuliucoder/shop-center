package com.iss.shop.dao;

import com.iss.shop.domain.Product;
import com.iss.shop.domain.ProductWithBLOBs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductMapper {

    int insert(Product record);

    int updateByPrimaryKey(Product record);

    int updateByPrimaryKeySelective(Product record);

    Product selectByPrimaryKey(Integer id);

    List<Product> selectList();

    Product selectByProductId(Integer id);

    List<Product> selectByNameAndProductId(@Param("productName")String productName,@Param("productId") Integer productId);

    List<Product> selectByNameAndCategoryIds(@Param("productName")String productName, @Param("categoryIdList")List<Integer> categoryIdList);

    List<Product> searchByConditions(@Param("productId") Integer productId,@Param("status")Integer status,@Param("created") Date created);

    List<Product> findProductByCategory(Integer categoryId);

    int delProductById(Integer id);
}