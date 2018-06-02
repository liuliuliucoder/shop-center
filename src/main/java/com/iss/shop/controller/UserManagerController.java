package com.iss.shop.controller;

import com.iss.shop.domain.RoleEnum;
import com.iss.shop.domain.User;
import com.iss.shop.service.UserService;
import com.iss.shop.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/managers")
public class UserManagerController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login() {
        return "manager-login";
    }

    @PostMapping("/loginAction")
    @ResponseBody
    public Result login(@RequestBody User user , HttpSession session) {
        Result result = new Result();
        result.setValue(false);
        result = userService.getPasswordByUserName(user);
        if (result.getValue()) {
            User userInfo = userService.getUserByUserName(user.getUserName());
            if (userInfo.getRole() == RoleEnum.ROLE_ADMIN.getCode()) {
                //说明登录的是管理员
                session.setAttribute("currentUser", userInfo);
                result.setData(user);
                result.setValue(true);
                return result;
            } else {
                result.setMessage("不是管理员,无法登录");
                return result;
            }
        }
        result.setMessage("用户名或密码错误");
        return result;
    }
}
