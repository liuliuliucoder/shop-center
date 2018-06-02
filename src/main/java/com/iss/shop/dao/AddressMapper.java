package com.iss.shop.dao;

import com.iss.shop.domain.Address;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressMapper {
    int insert(Address record);

    int updateByPrimaryKeySelective(Address record);

    int deleteByAddressIdUserId(@Param("userId")Integer userId, @Param("addressId") Integer addressId);

    int updateByAddress(Address record);

    Address selectByAddressIdUserId(@Param("userId")Integer userId,@Param("addressId") Integer addressId);

    List<Address> selectByUserId(@Param("userId")Integer userId);

    Address selectByPrimaryKey(Integer id);

}