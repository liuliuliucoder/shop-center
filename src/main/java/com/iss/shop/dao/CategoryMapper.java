package com.iss.shop.dao;

import com.iss.shop.domain.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper {

    int insert(Category record);

    int updateByPrimaryKeySelective(Category record);

    Category selectByCategoryId(Integer id);

    List<Category> selectCategoryChildrenByParentId(Integer categoryId);

    List<Category> selectForParent();

    List<Category> selectForSubCategory();

}