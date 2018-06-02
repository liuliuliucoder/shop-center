package com.iss.shop.service;

import com.iss.shop.domain.Address;
import com.iss.shop.util.Result;

public interface AddressService {
    Result add(Integer userId, Address address);
    Result<String> del(Integer userId,Integer addressId);
    Result update(Integer userId, Address address);
    Result<Address> select(Integer userId, Integer addressId);
//    Result<PageInfo> list(Integer userId, int pageNum, int pageSize);
}
