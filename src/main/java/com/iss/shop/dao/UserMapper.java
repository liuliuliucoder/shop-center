package com.iss.shop.dao;

import com.iss.shop.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import javax.xml.transform.sax.SAXTransformerFactory;

/**
 * @author liuxiaodan
 */
@Repository
public interface UserMapper {

    /**
     * 通过用户名获取加密后的密码
     * @param userName
     * @return String
     */
    String getPasswordByUserName(String userName);

    /**
     * 注册
     * @param user
     * @return int
     */
    int insertUserInfo(User user);

    /**
     * 修改
     * @param userName
     * @param password
     * @return boolean
     */
    boolean updatePassword(@Param("password") String password,@Param("userName") String userName);

    /**
     * 找回密码
     * @param userName
     * @return User
     */
    User getUserByUserName(String userName);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUserName(String userName);

    User getUserByUserNamePassword(@Param("userName") String userName, @Param("password") String password);

    int updatePasswordByUserName(@Param("userName") String userName,@Param("password") String password);

    String checkPassword(Integer userId);

    String selectQuestionByUserName(String userName);

    int checkAnswer(@Param("userName") String userName,@Param("question") String question,@Param("answer") String answer);

}