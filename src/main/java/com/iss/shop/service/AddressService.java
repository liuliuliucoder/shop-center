package com.iss.shop.service;

import com.iss.shop.domain.Address;
import com.iss.shop.util.Result;

import java.util.List;

public interface AddressService {
    Result add(Integer userId, Address address);
    Result<String> del(Integer userId,Integer addressId);
    Result update(Integer userId, Address address);
    Result<Address> select(Integer userId, Integer addressId);
    List<Address> selectByUserId(Integer userId);
//    Result<PageInfo> list(Integer userId, int pageNum, int pageSize);
}
