package com.iss.shop.controller;

import com.google.common.collect.Maps;
import com.iss.shop.domain.*;
import com.iss.shop.service.ProductService;
import com.iss.shop.service.UserService;
import com.iss.shop.util.Property;
import com.iss.shop.util.Result;
import com.iss.shop.util.ServerResponse;
import com.oracle.webservices.internal.api.message.PropertySet;
import org.apache.velocity.texen.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/manages/product")
public class ProductManageController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    @PostMapping("/save")
    @ResponseBody
    public Result productSave(HttpSession session, @RequestBody Product product){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user == null){
            result.setMessage("用户未登录,请登录管理员");
            return result;
        }
        if(userService.checkAdminRole(user).getValue()){
            return productService.saveOrUpdateProduct(product);
        }else{
            result.setMessage("无权限操作");
            return result;
        }
    }
    @PostMapping("/findProductByProductId")
    @ResponseBody
    public Result findProductByProductId(HttpSession session, @RequestBody Integer productId){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user == null){
            result.setMessage("用户未登录,请登录管理员");
            return result;
        }
        if(userService.checkAdminRole(user).getValue()){
            return productService.getProductDetail(productId);
        }else{
            result.setMessage("无权限操作");
            return result;
        }
    }

    @GetMapping("/setSaleStatus")
    @ResponseBody
    public Result setSaleStatus(HttpSession session, Integer productId,Integer status){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user == null){
            result.setMessage("用户未登录,请登录管理员");
            return result;
        }
        if(userService.checkAdminRole(user).getValue()){
            return productService.setSaleStatus(productId,status);
        }else{
            result.setMessage("无权限操作");
            return result;
        }
    }

    @GetMapping("/detail")
    @ResponseBody
    public Result getDetail(HttpSession session, Integer productId){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user == null){
            result.setMessage("用户未登录,请登录管理员");
            return result;
        }
        if(userService.checkAdminRole(user).getValue()){
            //填充业务
            return productService.manageProductDetail(productId);

        }else{
            result.setMessage("无权限操作");
            return result;
        }
    }

    @GetMapping("/list")
    @ResponseBody
    public Result getList(HttpSession session, int pageNum, int pageSize){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user == null){
            result.setMessage("用户未登录,请登录管理员");
            return result;
        }
        if(userService.checkAdminRole(user).getValue()){
            //填充业务
            return productService.getProductList(pageNum,pageSize);

        }else{
            result.setMessage("无权限操作");
            return result;
        }
    }

    @GetMapping("/search")
    @ResponseBody
    public Result productSearch(HttpSession session,String productName,Integer productId,Integer pageNum,Integer pageSize){

        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user == null){
            result.setMessage("用户未登录,请登录管理员");
            return result;
        }
        if(userService.checkAdminRole(user).getValue()){
            //填充业务
            return productService.searchProduct(productName,productId,pageNum,pageSize);

        }else{
            result.setMessage("无权限操作");
            return result;
        }
    }

    @PostMapping("/getStatusList")
    @ResponseBody
    public Result getStatusList(){
        Result result = new Result();
        List<Property> list = new ArrayList<Property>();

        for (ProductStatusEnum c : ProductStatusEnum.values()) {
            Property p = new Property(c.getCode(), c.getValue());
            list.add(p);
        }
        result.setValue(true);
        result.setContent(list);
        return result;
    }

    @PostMapping("/getCategoryList")
    @ResponseBody
    public Result getCategoryList(){
        Result result = new Result();
        List<Property> list = new ArrayList<Property>();
        result = productService.getCategoryList();
        List<Category> categoryList = result.getContent();
        for(Category category : categoryList){
            Property p = new Property(category.getId(), category.getCategoryName());
            list.add(p);
        }
        result.setValue(true);
        result.setContent(list);
        return result;
    }

    @PostMapping("/searchByConditions")
    @ResponseBody
    public Result productSearchByConditions(@RequestBody ProductQuery query,HttpSession session){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user == null){
            result.setMessage("用户未登录,请登录管理员");
            return result;
        }
        if(userService.checkAdminRole(user).getValue()){
            return productService.searchByConditions(query);

        }else{
            result.setMessage("无权限操作");
            return result;
        }
    }

    @PostMapping("/delProduct")
    @ResponseBody
    public Result delProduct(@RequestBody ProductQuery query,HttpSession session){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user == null){
            result.setMessage("用户未登录,请登录管理员");
            return result;
        }
        if(userService.checkAdminRole(user).getValue()){
            return productService.delProduct(query);

        }else{
            result.setMessage("无权限操作");
            return result;
        }
    }

    @PostMapping("/upload")
    @ResponseBody
    public Result upload(HttpSession session){
        User user = (User)session.getAttribute("currentUser");
        Result result = new Result();
        result.setValue(false);
        if(user == null){
            result.setMessage("用户未登录,请登录管理员");
            return result;
        }
        if(!userService.checkAdminRole(user).getValue()){
            result.setMessage("无权限操作");
            return result;
        }


        return result;
    }


//    @RequestMapping("richtext_img_upload.do")
//    @ResponseBody
//    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
//        Map resultMap = Maps.newHashMap();
//        User user = (User)session.getAttribute(Const.CURRENT_USER);
//        if(user == null){
//            resultMap.put("success",false);
//            resultMap.put("msg","请登录管理员");
//            return resultMap;
//        }
//        //富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
////        {
////            "success": true/false,
////                "msg": "error message", # optional
////            "file_path": "[real file path]"
////        }
//        if(iUserService.checkAdminRole(user).isSuccess()){
//            String path = request.getSession().getServletContext().getRealPath("upload");
//            String targetFileName = iFileService.upload(file,path);
//            if(StringUtils.isBlank(targetFileName)){
//                resultMap.put("success",false);
//                resultMap.put("msg","上传失败");
//                return resultMap;
//            }
//            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
//            resultMap.put("success",true);
//            resultMap.put("msg","上传成功");
//            resultMap.put("file_path",url);
//            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
//            return resultMap;
//        }else{
//            resultMap.put("success",false);
//            resultMap.put("msg","无权限操作");
//            return resultMap;
//        }
//    }
}
