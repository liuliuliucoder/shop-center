package com.iss.shop.controller;

import com.iss.shop.domain.Address;
import com.iss.shop.domain.PasswordReset;
import com.iss.shop.domain.User;
import com.iss.shop.service.AddressService;
import com.iss.shop.service.UserService;
import com.iss.shop.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @author liuxiaodan
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;

    @PostMapping("/loginAction")
    @ResponseBody
    public Result loginAction(@RequestBody User user ,HttpSession session){
        Result result = new Result();
        result = userService.getPasswordByUserName(user);
        if(result.getValue()){
            session.setAttribute("currentUser",result.getData());
        }
        return result;
    }

    @PostMapping("/registerAction")
    @ResponseBody
    public Result registerAction(@RequestBody User user){
        Result result = userService.insertUserInfo(user);
        return result;
    }
    @PostMapping("/getUserInfo")
    @ResponseBody
    public Result getUserInfo(HttpSession session){
        Result result = new Result();
        result.setValue(false);
        User user = (User) session.getAttribute("currentUser");
        if(user == null){
            result.setMessage("用户未登录,无法获取当前用户的信息");
            return result;
        }
        User userInfo = userService.getUserByUserName(user.getUserName());
        List<Address> addressList = addressService.selectByUserId(userInfo.getId());
        user.setAddressList(addressList);
        result.setData(user);
        result.setValue(true);
        return result;
    }

    @PostMapping("/selectQuestion")
    @ResponseBody
    public Result selectQuestion(HttpSession session){
        User user = (User) session.getAttribute("currentUser");
        String userName = "";
        if(null != user){
            userName = user.getUserName();
        }
        return userService.selectQuestion(userName);
    }

    @PostMapping("/checkQuestion")
    @ResponseBody
    public Result forgetCheckAnswer(@RequestBody PasswordReset passwordReset){
        User user = new User();
        user.setUserName(passwordReset.getUserName());
        user.setQuestion(passwordReset.getQuestion());
        user.setAnswer(passwordReset.getAnswer());
        user.setPassword(passwordReset.getPassword());
        return userService.updatePasswordByUserNameAndQuestionAndAnswer(user);
    }

    @PostMapping("/forgetResetPassword")
    @ResponseBody
    public Result forgetPassword(@RequestBody String userName,String password,String forgetToken){
        return userService.forgetPassword(userName,password,forgetToken);
    }

    /**
     * 登录状态的重置密码
     */
    @PostMapping("/resetPassword")
    @ResponseBody
    public Result resetPassword(@RequestBody PasswordReset passwordReset, HttpSession session){
        Result result = new Result();
        User userInfo = (User)session.getAttribute("currentUser");
        if(null == userInfo){
            result.setMessage("用户未登录！");
            result.setValue(false);
            return result;
        }
        if(null != passwordReset && !"".equals(passwordReset)){
            return userService.resetPassword(passwordReset.getOldPassword(),passwordReset.getPassword(),userInfo);
        }
        return result;

    }
    @PostMapping("/updateUserInfo")
    @ResponseBody
    public Result update_information(@RequestBody User user,HttpSession session){
        Result result = new Result();
        result.setValue(false);
        User currentUser = (User)session.getAttribute("currentUser");
        if(currentUser == null){
            result.setMessage("用户未登录");
            return result;
        }
        user.setId(currentUser.getId());
        user.setUserName(currentUser.getUserName());
        currentUser = userService.getUserByUserName(currentUser.getUserName());
        user.setPassword(currentUser.getPassword());
        user.setCreated(currentUser.getCreated());
        user.setUserName(currentUser.getUserName());
        user.setUuid(currentUser.getUuid());
        user.setModified(new Date());
        result = userService.updateInformation(user);
        if(result.getValue()){
            session.setAttribute("currentUser",result.getData());
        }
        return result;
    }

    public Result get_information(HttpSession session){
        Result result = new Result();
        result.setValue(false);
        User currentUser = (User)session.getAttribute("currentUser");
        if(currentUser == null){
            result.setMessage("未登录,需要强制登录");
            return result;
        }
        return userService.getInformation(currentUser.getId());
    }


}
