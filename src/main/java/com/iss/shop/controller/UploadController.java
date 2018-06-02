package com.iss.shop.controller;

import com.iss.shop.util.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @RequestMapping
    public Result upload(HttpServletRequest request,
                         @RequestParam("file") MultipartFile file){
        Result result = new Result();
        //如果文件不为空，写入上传路径
        if(!file.isEmpty()) {
            //上传文件路径
            String pathRoot = request.getServletContext().getRealPath("");
            //上传文件名
            String filename = file.getOriginalFilename();
            String path="/static/imgs/"+filename;
            File filepath = new File(pathRoot+path);
            //判断路径是否存在，如果不存在就创建一个
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            }
            //将上传文件保存到一个目标文件当中
            try {
                file.transferTo(new File(pathRoot+path));
            } catch (IOException e) {
                result.setValue(false);
                result.setMessage(e.getMessage());
                return result;
            }
            result.setValue(true);
            result.setData(path);

        } else {
            result.setValue(false);
            result.setMessage("上传文件为空!");
        }
        return result;
    }
}
