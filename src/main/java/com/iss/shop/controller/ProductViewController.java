package com.iss.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProductViewController {
    @RequestMapping("/list")
    public String list() {
        return "list";
    }
    @RequestMapping("/detail")
    public String detail() {
        return "detail";
    }
}
