package com.iss.shop.service;

import com.iss.shop.domain.Category;
import com.iss.shop.util.Result;

import java.util.List;

public interface CategoryService {
      Result addCategory(String categoryName, Integer parentId);

      boolean updateCategoryName(Integer categoryId, String categoryName);

      Result getChildrenParallelCategory(Integer categoryId);

      List<Category> selectCategoryAndChildrenById(Integer categoryId);

      List<Category> selectForParent();

      List<Category> selectForSubCategory();


}
