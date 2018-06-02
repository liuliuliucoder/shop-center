package com.iss.shop.service.impl;

import com.iss.shop.dao.CategoryMapper;
import com.iss.shop.dao.ProductMapper;
import com.iss.shop.domain.*;
import com.iss.shop.service.CategoryService;
import com.iss.shop.service.ProductService;
import com.iss.shop.util.ProductListOrderBy;
import com.iss.shop.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("productService")
public class ProductServiceImpl implements ProductService {


    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryService categoryService;

    @Override
    public Result saveOrUpdateProduct(Product product) {
        Result result = new Result();
        result.setValue(false);
        if (product != null) {
            if (!StringUtils.isEmpty(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }

            if (product.getId() != null) {
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0) {
                    result.setValue(true);
                    result.setMessage("更新商品信息成功");
                    return result;
                }
                result.setMessage("更新商品信息失败");
                return result;
            } else {
                product.setDetail(product.getMainImage());
                product.setSubImages(product.getMainImage());
                int rowCount = productMapper.insert(product);
                if (rowCount > 0) {
                    result.setValue(true);
                    result.setMessage("新增商品成功");
                    return result;
                }
                result.setMessage("新增商品失败");
                return result;
            }
        }
        result.setMessage("新增或更新产品参数不正确");
        return result;
    }

    @Override
    public Result setSaleStatus(Integer productId, Integer status) {
        Result result = new Result();
        result.setValue(false);
        if (productId == null || status == null) {
            result.setMessage("入参productId或status为空");
            return result;
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            result.setMessage("修改产品销售状态成功");
            result.setValue(true);
            return result;
        }
        result.setMessage("修改产品销售状态失败");
        return result;
    }

    @Override
    public Result<ProductDetailVo> getProductDetail(Integer productId) {
        Result<ProductDetailVo> result = new Result();
        result.setValue(false);
        if (productId == null) {
            return result;
        }
        Product product = productMapper.selectByProductId(productId);
        if (product == null) {
            result.setMessage("产品已下架或者删除");
            return result;
        }
        if (product.getStatus() != ProductStatusEnum.ON_SALE.getCode()) {
            result.setMessage("产品已下架或者删除");
            return result;
        }
        ProductDetailVo productDetailVo = convert2ProductDetailVo(product);
        result.setValue(true);
        result.setData(productDetailVo);
        return result;
    }

    @Override
    public Result getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        Result result = new Result();
        if (StringUtils.isEmpty(keyword) && categoryId == null) {
            return result;
        }
        List<Integer> categoryIdList = new ArrayList<Integer>();

        if (categoryId != null) {
            Category category = categoryMapper.selectByCategoryId(categoryId);
            if (category == null && StringUtils.isEmpty(keyword)) {
                //没有该分类,并且还没有关键字,这个时候返回一个空的结果集,不报错
                List<ProductListVo> productListVoList = new ArrayList<ProductListVo>();

                return result;
            }
//            categoryIdList = categoryService.selectCategoryAndChildrenById(category.getId());
        }
        if (!StringUtils.isEmpty(keyword)) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }

        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isEmpty(keyword) ? null : keyword, categoryIdList.size() == 0 ? null : categoryIdList);

        List<ProductListVo> productListVoList = new ArrayList<ProductListVo>();
        for (Product product : productList) {
            ProductListVo productListVo = convert2ProductListVo(product);
            productListVoList.add(productListVo);
        }
        result.setContent(productListVoList);
        return result;
    }

    @Override
    public Result manageProductDetail(Integer productId) {
        Result result = new Result();
        result.setValue(false);
        if (productId == null) {
            result.setMessage("入参productId为空");
            return result;
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            result.setMessage("产品已下架或者删除");
            return result;
        }
        ProductDetailVo productDetailVo = convert2ProductDetailVo(product);
        result.setValue(true);
        result.setData(productDetailVo);
        return result;
    }

    private ProductDetailVo convert2ProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubTitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setProductName(product.getProductName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getCount());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setDetail(product.getDetail());

        Category category = categoryMapper.selectByCategoryId(product.getCategoryId());
        if (category == null) {
            productDetailVo.setParentCategoryId(0);//默认根节点
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(String.valueOf(product.getCreated()));
        return productDetailVo;
    }

    @Override
    public Result getProductList(int pageNum, int pageSize) {
        Result result = new Result();
        result.setValue(false);
        List<Product> productList = productMapper.selectList();

        List<ProductListVo> productListVoList = new ArrayList<ProductListVo>();

        for (Product productItem : productList) {
            ProductListVo productListVo = convert2ProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        if(null != productListVoList){
            result.setValue(true);
            result.setContent(productListVoList);
        }
        result.setMessage("获取商品列表失败！");
        return result;
    }

    private ProductListVo convert2ProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setProductName(product.getProductName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubTitle());
        productListVo.setStatus(product.getStatus());
        productListVo.setCount(product.getCount());
        ProductStatusEnum productStatusEnum = ProductStatusEnum.codeOf(product.getStatus());
        productListVo.setStatusName(productStatusEnum.getValue());
        return productListVo;
    }

    @Override
    public Result searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
        Result result = new Result();
        if (StringUtils.isEmpty(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByNameAndProductId(productName, productId);
        List<ProductListVo> productListVoList = new ArrayList<ProductListVo>();
        for (Product productItem : productList) {
            ProductListVo productListVo = convert2ProductListVo(productItem);
            productListVoList.add(productListVo);
        }

        result.setValue(true);
        result.setContent(productListVoList);
        return result;
    }

    @Override
    public Result searchByConditions(ProductQuery query) {
        Result result = new Result();
        Integer id = query.getId();
        Integer status = query.getStatus();
        Date created = query.getCreated();
        List<Product> productList = productMapper.searchByConditions(id, status, created);
        if(productList.size() == 0){
            result.setValue(false);
            result.setMessage("查询失败");
            return result;
        }
        List<ProductListVo> productListVoList = new ArrayList<ProductListVo>();
        for (Product productItem : productList) {
            ProductListVo productListVo = convert2ProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        result.setContent(productListVoList);
        result.setValue(true);
        return result;
    }

    @Override
    public Result findProductByCategory(Integer categoryId) {
        Result result = new Result();
        List<Product> productList = productMapper.findProductByCategory(categoryId);
        result.setContent(productList);
        return result;
    }

    @Override
    public Result getCategoryList(){
        Result result = new Result();
        result.setValue(false);
        List<Category> categoryList = categoryMapper.selectForSubCategory();
        if(null != categoryList){
            result.setValue(true);
            result.setContent(categoryList);
            return result;
        }
        result.setMessage("获取类别失败！");
        return result;
    }

    @Override
    public Result delProduct(ProductQuery query){
        Result result = new Result();
        result.setValue(false);
        Integer id = query.getId();
        if(productMapper.delProductById(id)>0){
            result.setValue(true);
            return result;
        }

        result.setMessage("删除商品失败！");
        return result;
    }
}
