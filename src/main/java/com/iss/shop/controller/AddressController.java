package com.iss.shop.controller;

import com.iss.shop.domain.Address;
import com.iss.shop.domain.User;
import com.iss.shop.service.AddressService;
import com.iss.shop.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
@Controller
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping("/add")
    @ResponseBody
    public Result add(HttpSession session, Address address){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        return addressService.add(user.getId(),address);
    }


    @GetMapping("/del")
    @ResponseBody
    public Result del(HttpSession session,Integer addressId){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        return addressService.del(user.getId(),addressId);
    }

    @GetMapping("/update")
    @ResponseBody
    public Result update(HttpSession session,Address address){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        return addressService.update(user.getId(),address);
    }


    @GetMapping("/select")
    @ResponseBody
    public Result<Address> select(HttpSession session,Integer addressId){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        return addressService.select(user.getId(),addressId);
    }


//    @RequestMapping("list.do")
//    @ResponseBody
//    public Result<PageInfo> list(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
//                                         @RequestParam(value = "pageSize",defaultValue = "10")int pageSize,
//                                         HttpSession session){
//        User user = (User)session.getAttribute(Const.CURRENT_USER);
//        if(user ==null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
//        }
//        return iShippingService.list(user.getId(),pageNum,pageSize);
//    }
}
