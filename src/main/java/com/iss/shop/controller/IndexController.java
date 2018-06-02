package com.iss.shop.controller;
import com.iss.shop.util.VerificationUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author liuxiaodan
 *
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String indexPage() {
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/loginOut")
    public String loginOut(HttpSession session) {
        session.removeAttribute("currentUser");
        return "index";
    }

    @RequestMapping("/register")
    public String register() {
        return "register";
    }

    @RequestMapping("/result")
    public String result() {
        return "result";
    }

    @RequestMapping("/resetPassword")
    public String resetPassword() {
        return "user-pass-reset";
    }

    @RequestMapping("/userCenter")
    public String userCenter() {
        return "user-center";
    }

    @RequestMapping("/updatePassword")
    public String updatePassword() {
        return "user-update-password";
    }

    @RequestMapping("/verifyCode")
    @ResponseBody
    public void verifyCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        VerificationUtil.createVerification(request, response);
    }
}
