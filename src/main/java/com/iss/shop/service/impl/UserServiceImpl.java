package com.iss.shop.service.impl;

import com.iss.shop.dao.AddressMapper;
import com.iss.shop.dao.UserMapper;
import com.iss.shop.domain.Address;
import com.iss.shop.domain.RoleEnum;
import com.iss.shop.domain.User;
import com.iss.shop.service.AddressService;
import com.iss.shop.service.UserService;
import com.iss.shop.util.MD5Util;
import com.iss.shop.util.Result;
import com.iss.shop.util.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author liuxiaodan
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Autowired
    private AddressMapper addressMapper;

    private static final String KEY="2";

    @Autowired
    private AddressService addressService;

    /**
     * 登录
     * @param user
     * @return result
     */
    @Override
    public Result getPasswordByUserName(User user) {
        Result result = new Result();
        String userName = user.getUserName();
        String password = user.getPassword();
        //判断用户存不存在
        int resultCount = userMapper.checkUserName(userName);
        if(resultCount == 0){
            result.setValue(false);
            result.setMessage("用户名不存在！");
            return result;
        }
        try {
            password = MD5Util.md5(password,KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        User userInfo = userMapper.getUserByUserNamePassword(userName,password);
        if(null == userInfo){
            result.setMessage("密码错误！");
            result.setValue(false);
            return result;
        }
        userInfo.setPassword(null);
        result.setValue(true);
        result.setMessage("登陆成功！");
        result.setData(userInfo);
        return result;
    }

    /**
     * 注册
     * @param user
     * @return int
     */
    @Override
    public Result insertUserInfo(User user){
        Result result = new Result();
        result = checkValid(user.getUserName());
        if(result.getValue()){
            return result;
        }
        user.setRole(RoleEnum.ROLE_CUSTOMER.getCode());
        //MD5加密
        try {
            user.setPassword(MD5Util.md5(user.getPassword(),KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int resultCount = userMapper.insertUserInfo(user);
        if(resultCount == 0){
            result.setValue(false);
            result.setMessage("注册失败");
            return result;
        }
        result.setValue(true);
        result.setMessage("注册成功");
        return result;
    }

    @Override
    public User getUserByUserName(String userName){
        User user = userMapper.getUserByUserName(userName);
        return user;
    }

    @Override
    public Result selectQuestion(String userName){

        Result result = checkValid(userName);
        result.setValue(false);
        if(result.getValue()){
            //用户不存在
            result.setMessage("用户不存在");
            return result;
        }
        String question = userMapper.selectQuestionByUserName(userName);
        if(StringUtils.isEmpty(question)){
            result.setMessage("找回密码的问题是空的");
            return result;
        }
        result.setData(question);
        result.setValue(true);
        return result;
    }

    @Override
    public Result checkAnswer(String userName,String question,String answer,String password){
        Result result = new Result();
        result.setValue(false);
        int resultCount = userMapper.checkAnswer(userName,question,answer);
        if(resultCount>0){

            result.setValue(true);
            return result;
        }
        result.setMessage("问题的答案错误");
        return result;
    }
    /**
     * 找回密码
     *
     * @param user
     * @return int
     */
    @Override
    public Result updatePasswordByUserNameAndQuestionAndAnswer(User user){
        Result result = new Result();
        result.setValue(false);
        User userInfo = userMapper.getUserByUserName(user.getUserName());
        if(!StringUtils.isEmpty(user.getAnswer())){
            if((userInfo.getQuestion()).equals(user.getQuestion()) && (userInfo.getAnswer()).equals(user.getAnswer())){
                if(!StringUtils.isEmpty(user.getPassword())){
                    try {
                        String password = MD5Util.md5(user.getPassword(),KEY);
                        user.setPassword(password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    result.setValue(userMapper.updatePassword(user.getPassword(),user.getUserName()));
                    return result;
                }
            }
        }
        result.setValue(false);
        result.setMessage("密保问题答案错误！");
        return result;
    }

    @Override
    public Result resetPassword(String oldPassword,String newPassword,User user){
        Result result = new Result();
        try {
            String sourcePassword = userMapper.checkPassword(user.getId());
            String md5OldPassword =MD5Util.md5(oldPassword,KEY);
            if(!sourcePassword.equals(md5OldPassword)){
                result.setMessage("旧密码错误！");
                result.setValue(false);
                return result;
            }
            user.setPassword(MD5Util.md5(newPassword, KEY));
            if(!userMapper.updatePassword(user.getPassword(),user.getUserName())){
                result.setMessage("密码修改失败！");
                result.setValue(false);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setValue(true);
        result.setMessage("修改密码成功！");
        return result;
    }

    @Override
    public Result forgetPassword(String userName,String passwordNew,String forgetToken){
        Result result = new Result();
        if(StringUtils.isEmpty(forgetToken)){
            result.setMessage("参数错误,token需要传递");
            return result;
        }
        result = checkValid(userName);
        if(!result.getValue()){
            return result;
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+userName);
        if(StringUtils.isEmpty(token)){
            result.setMessage("token无效或者过期");
            result.setValue(false);
            return result;
        }
        User user = userMapper.getUserByUserName(userName);
        Integer id = user.getId();
        try {
            if(forgetToken.equals(token)) {
                String md5Password = null;
                md5Password = MD5Util.md5(passwordNew, KEY);
                int rowCount = userMapper.updatePasswordByUserName(userName, md5Password);
                if (rowCount < 0) {
                    result.setMessage("修改密码失败！");
                    result.setValue(false);
                    return result;
                }
            }else{
                result.setMessage("token错误,请重新获取重置密码的token");
                result.setValue(false);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setValue(true);
        result.setMessage("修改密码成功！");
        return result;
    }

    private Result checkValid(String str){
        Result result = new Result();
        //开始校验
        int resultCount = userMapper.checkUserName(str);
        if(resultCount == 0){
            result.setValue(false);
            result.setMessage("用户名不存在！");
            return result;
        }
        result.setValue(true);
        result.setMessage("参数校验成功！");
        return result;
    }

    @Override
    public Result updateInformation(User user){
        Result result = new Result();
        User updateUser = userMapper.getUserByUserName(user.getUserName());
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setModified(new Date());

        List<Address> addressList = new ArrayList<Address>();
        for(Address address : user.getAddressList()){
            Address ad = address;
            ad.setUserId(user.getId());
            addressList.add(ad);
        }
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        int updateAddress = addressMapper.batchUpdateByUserId(addressList);

        if(updateCount > 0 && updateAddress > 0){
            result.setValue(true);
            result.setMessage("更新个人信息成功");
            return result;
        }
        result.setValue(false);
        result.setMessage("更新个人信息失败");
        return result;
    }

    @Override
    public Result getInformation(Integer userId){
        Result result = new Result();
        result.setValue(false);
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            result.setMessage("找不到当前用户");
            return result;
        }
        user.setPassword(null);
        result.setValue(true);
        result.setData(user);
        return result;
    }

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    @Override
    public Result checkAdminRole(User user){
        Result result = new Result();
        result.setValue(false);
        if(user != null && user.getRole().intValue() == RoleEnum.ROLE_ADMIN.getCode()){
            result.setValue(true);
            return result;
        }
        result.setMessage("不是管理员");
        return result;
    }
}
