package com.iss.shop.service.impl;

import com.google.common.base.Splitter;
import com.iss.shop.dao.CartMapper;
import com.iss.shop.dao.ProductMapper;
import com.iss.shop.domain.Cart;
import com.iss.shop.domain.CartProductVo;
import com.iss.shop.domain.CartVo;
import com.iss.shop.domain.Product;
import com.iss.shop.service.CartService;
import com.iss.shop.util.BigDecimalUtil;
import com.iss.shop.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service("cartService")
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public Result add(Integer userId, Integer productId, Integer count) {
        Result result = new Result();
        result.setValue(false);
        if (productId == null || count == null) {
            result.setMessage("添加购物车失败，入参为空");
            return result;
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);

        if (cart == null) {
            //这个产品不在这个购物车里,需要新增一个这个产品的记录
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            cartItem.setChecked(1);
            if(cartMapper.insert(cartItem)>0){
                result.setValue(true);
                result.setMessage("加入购物车成功！");
            }
        } else {
            //这个产品已经在购物车里了.
            //查询库存
            List<Cart> cartList = cartMapper.selectCartByUserId(userId);
            if(null != cartList) {
                for (Cart cartItem : cartList) {
                    Product product = productMapper.selectByProductId(cartItem.getProductId());
                    if (product != null) {
                        if(cartItem.getQuantity() > product.getCount()){
                            count = cartItem.getQuantity();
                            cart.setQuantity(count);
                        }
                        if(cartItem.getQuantity() < product.getCount()){
                            count = cartItem.getQuantity() + count;
                            cart.setQuantity(count);
                        }
                    }
                    if (cartMapper.updateByPrimaryKeySelective(cart) > 0) {
                        result.setValue(true);
                        result.setMessage("修改购物车数量成功！");
                    }
                }
            }
        }
        return list(userId);
    }
    @Override
    public Result<CartProductVo> list (Integer userId){
        Result<CartProductVo> result = new Result();
        List<CartProductVo> cartProductVoList = getCartVoLimit(userId);
        result.setContent(cartProductVoList);
        result.setValue(true);
        return result;
    }

    @Override
    public Result<CartProductVo> update(Integer userId,Integer productId,Integer count){
        Result result = new Result();
        if(productId == null || count == null){
            result.setValue(false);
            result.setMessage("添加购物车失败，入参为空");
            return result;
       }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if(cart != null){
            cart.setQuantity(count);
       }
        cartMapper.updateByPrimaryKey(cart);
       return list(userId);
    }

    @Override
    public Result<CartProductVo> deleteProduct(Integer userId,String productIds){
        Result result = new Result();
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if(productList != null){
            result.setValue(false);
            result.setMessage("添加购物车失败，入参为空");
           return result;
        }
        cartMapper.deleteByUserIdProductIds(userId,productList);
        return list(userId);
    }

    @Override
    public Result<CartProductVo> deleteOne(Integer userId,Integer productId){
        Result result = new Result();
        result.setValue(false);
        if(cartMapper.deleteByUserIdProductId(userId,productId)<0){
            result.setMessage("删除失败");
            return result;
        }
        return list(userId);
    }

    @Override
    public Result<CartProductVo> selectOrUnSelect (Integer userId,Integer productId,Integer checked){
       cartMapper.checkedOrUncheckedProduct(userId,productId,checked);
        return list(userId);
    }

    @Override
    public Result<Integer> getCartProductCount(Integer userId){
        Result result = new Result();
        result.setValue(false);
        if(userId == null){
            result.setMessage("入参为空");
            return result;
        }
        result.setValue(true);
        result.setData(cartMapper.selectCartProductCount(userId));
        return result;
    }

    private List<CartProductVo> getCartVoLimit(Integer userId){
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVo> cartProductVoList = new ArrayList<CartProductVo>();

       BigDecimal cartTotalPrice = new BigDecimal("0");

        if(null != cartList){
            for(Cart cartItem : cartList){
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(cartItem.getProductId());
                cartProductVo.setQuantity(cartItem.getQuantity());

                Product product = productMapper.selectByProductId(cartItem.getProductId());
                if(product != null){
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getProductName());
                    cartProductVo.setProductSubtitle(product.getSubTitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getCount());
                    Cart cartForQuantity = cartItem;
                    //判断库存
//                   int buyLimitCount = 0;
//                    //库存充足的时候
//                    if(product.getCount() >= cartItem.getQuantity()){
//                       buyLimitCount = cartItem.getQuantity();
//                    }else{
//                        buyLimitCount = product.getCount();
//                    }
//                    cartMapper.updateByPrimaryKeySelective(cartForQuantity);

//                    cartProductVo.setQuantity(buyLimitCount);
                    //计算总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cartItem.getChecked());
               }
               //1表示勾选
                if(cartItem.getChecked() == 0){
                   //如果已经勾选,增加到整个的购物车总价中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                }

                cartProductVoList.add(cartProductVo);
            }
        }
//        cartVo.setCartTotalPrice(cartTotalPrice);
//        cartVo.setCartProductVoList(cartProductVoList);
//        cartVo.setAllChecked(getAllCheckedStatus(userId));
        return cartProductVoList;
    }

    private boolean getAllCheckedStatus(Integer userId){
        if(userId == null){
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
    }
}


