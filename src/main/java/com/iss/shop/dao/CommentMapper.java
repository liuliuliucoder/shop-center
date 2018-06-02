package com.iss.shop.dao;

import com.iss.shop.domain.Comment;

public interface CommentMapper {
    int insert(Comment record);

    int insertSelective(Comment record);
}