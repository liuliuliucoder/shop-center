package com.iss.shop.service.impl;

import com.iss.shop.dao.CategoryMapper;
import com.iss.shop.domain.Category;
import com.iss.shop.service.CategoryService;
import com.iss.shop.util.Result;
import com.iss.shop.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Result addCategory(String categoryName, Integer parentId) {
        Result result = new Result();
        result.setValue(false);
        if (parentId == null || StringUtils.isEmpty(categoryName)) {
            result.setMessage("添加品类参数错误");
            return result;
        }

        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setParentId(parentId);

        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            result.setValue(true);
            result.setMessage("添加品类成功");
            return result;
        }
        result.setMessage("添加品类失败");
        return result;
    }

    @Override
    public boolean updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isEmpty(categoryName)) {
            return false;
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setCategoryName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Result getChildrenParallelCategory(Integer categoryId) {
        Result result = new Result();
        result.setValue(false);
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (categoryList.size() == 0) {
            result.setMessage("未找到当前分类的子分类");
            return result;
        }
        result.setValue(true);
        result.setContent(categoryList);
        return result;
    }


    /**
     * 递归查询本节点的id及孩子节点的id
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<Category> selectCategoryAndChildrenById(Integer categoryId) {
        return categoryMapper.selectCategoryChildrenByParentId(categoryId);
    }

    @Override
    public List<Category> selectForParent() {
        return categoryMapper.selectForParent();
    }

    @Override
    public List<Category> selectForSubCategory() {
        return categoryMapper.selectForSubCategory();
    }
//
//    //递归算法,算出子节点
//    private Set<Category> findChildCategory(Set<Category> categorySet ,Integer categoryId){
//        Category category = categoryMapper.selectByCategoryId(categoryId);
//        if(category != null){
//            categorySet.add(category);
//        }
//        //查找子节点,递归算法一定要有一个退出的条件
//        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
//        for(Category categoryItem : categoryList){
//            findChildCategory(categorySet,categoryItem.getId());
//        }
//        return categorySet;
//    }
}
