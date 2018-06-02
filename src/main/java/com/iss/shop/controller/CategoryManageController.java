package com.iss.shop.controller;

import com.iss.shop.domain.User;
import com.iss.shop.service.CategoryService;
import com.iss.shop.service.UserService;
import com.iss.shop.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/manager/category")
public class CategoryManageController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;
    /**
     * 添加分类
     */
    @RequestMapping("addCategory")
    @ResponseBody
    public Result addCategory(HttpSession session, String categoryName, int parentId){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user == null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        //校验一下是否是管理员
        if(userService.checkAdminRole(user).getValue()){
            //是管理员
            //增加我们处理分类的逻辑
            return categoryService.addCategory(categoryName,parentId);

        }else{
            result.setMessage("无权限操作,需要管理员权限");
            return result;
        }
    }

    /**
     * 修改分类名称
     */
    @GetMapping("/setCategoryName")
    @ResponseBody
    public Result setCategoryName(HttpSession session, Integer categoryId, String categoryName){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user == null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        if(userService.checkAdminRole(user).getValue()){
            //更新categoryName
            if(categoryService.updateCategoryName(categoryId,categoryName)){
                result.setValue(true);
                return result;
            }
        }else{
            result.setMessage("无权限操作,需要管理员权限");
            return result;
        }
        return result;
    }

    @GetMapping("/getCategory")
    @ResponseBody
    public Result getChildrenParallelCategory(HttpSession session,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user == null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        if(userService.checkAdminRole(user).getValue()){
            //查询子节点的category信息,并且不递归,保持平级
            return categoryService.getChildrenParallelCategory(categoryId);
        }else{
            result.setMessage("无权限操作,需要管理员权限");
            return result;
        }
    }

    @GetMapping("/getDeepCategory")
    @ResponseBody
    public Result getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user == null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        if(userService.checkAdminRole(user).getValue()){
            //查询当前节点的id和递归子节点的id
//            0->10000->100000
//            List<Ca> list = categoryService.selectCategoryAndChildrenById(categoryId);
//            result.setContent(list);
//            result.setValue(true);
            return result;
        }else{
            result.setMessage("无权限操作,需要管理员权限");
            return result;
        }
    }
}
