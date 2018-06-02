package com.iss.shop.service;

import com.iss.shop.domain.User;
import com.iss.shop.util.Result;

/**
 * @author liuxiaodan
 */
public interface UserService {

    /**
     * 登录
     * @param user
     * @return result
     */
     Result getPasswordByUserName(User user);

    /**
     * 注册
     * @param user
     * @return boolean
     */
     Result insertUserInfo(User user);

    /**
     * 根据用户名获取用户密码
     * @param userName
     * @return
     */
    User getUserByUserName(String userName);


    /**
     * 找回密码
     * @param user
     * @return boolean
     */
    boolean updatePasswordByUserNameAndQuestionAndAnswer(User user);

    Result resetPassword(String oldPassword,String newPassword,User user);

    Result forgetPassword(String username,String passwordNew,String forgetToken);

    Result updateInformation(User user);

    Result selectQuestion(String userName);

    Result checkAnswer(String userName,String question,String answer);

    Result getInformation(Integer userId);

    Result checkAdminRole(User user);
}
