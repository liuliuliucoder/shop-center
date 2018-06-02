package com.iss.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class OrderViewController {
    @RequestMapping("/order")
    public String order() {
        return "order";
    }
}
