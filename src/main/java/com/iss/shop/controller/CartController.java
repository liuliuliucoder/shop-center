package com.iss.shop.controller;

import com.iss.shop.domain.CartProductVo;
import com.iss.shop.domain.CartVo;
import com.iss.shop.domain.User;
import com.iss.shop.service.CartService;
import com.iss.shop.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/add")
    @ResponseBody
    public Result add(HttpSession session, Integer count, Integer id){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user == null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        result = cartService.add(user.getId(),id,count);
        return result;
    }

    @GetMapping("/list")
    @ResponseBody
    public Result list(HttpSession session) {
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        result = cartService.list(user.getId());
        return result;
    }

    @RequestMapping("/update")
    @ResponseBody
    public Result<CartProductVo> update(HttpSession session, Integer count, Integer id){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        return cartService.update(user.getId(),id,count);
    }

    @GetMapping("/deleteProduct")
    @ResponseBody
    public Result<CartProductVo> deleteProduct(String productIds,HttpSession session){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        return cartService.deleteProduct(user.getId(),productIds);
    }

    @GetMapping("/deleteOne")
    @ResponseBody
    public Result<CartProductVo> deleteOne(Integer productId,HttpSession session){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        if(null == productId && "".equals(productId)){
            result.setMessage("入参productId为空");
            return result;
        }
        return cartService.deleteOne(user.getId(),productId);
    }

    @GetMapping("/getTotalPrice")
    @ResponseBody
    public Result getTotalPrice(HttpSession session){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }

        Result<CartProductVo> cartProductVoResult = cartService.list(user.getId());
        List<CartProductVo> cartProductVoList = cartProductVoResult.getContent();
        BigDecimal totalAmount = BigDecimal.valueOf(0);
        for(CartProductVo cartProductVo : cartProductVoList){
            totalAmount = totalAmount.add(cartProductVo.getProductTotalPrice());
        }
        result.setData(totalAmount);
        result.setValue(true);
        return result;
    }

    @RequestMapping("/selectAll")
    @ResponseBody
    public Result<CartProductVo> selectAll(HttpSession session){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        return cartService.selectOrUnSelect(user.getId(),null,0);
    }

    @RequestMapping("/unSelectAll")
    @ResponseBody
    public Result<CartProductVo> unSelectAll(HttpSession session){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        return cartService.selectOrUnSelect(user.getId(),null,1);
    }

    @RequestMapping("/select")
    @ResponseBody
    public Result<CartProductVo> select(HttpSession session,Integer productId){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        return cartService.selectOrUnSelect(user.getId(),productId,0);
    }

    @RequestMapping("/unSelect")
    @ResponseBody
    public Result<CartProductVo> unSelect(HttpSession session,Integer productId){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        return cartService.selectOrUnSelect(user.getId(),productId,1);
    }

    /**
     * 获取当前购物车产品数量
     */
    @GetMapping("/getCartProductCount")
    @ResponseBody
    public Result<Integer> getCartProductCount(HttpSession session){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        return cartService.getCartProductCount(user.getId());
    }
}
