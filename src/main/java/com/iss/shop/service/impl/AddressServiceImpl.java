package com.iss.shop.service.impl;

import com.iss.shop.dao.AddressMapper;
import com.iss.shop.domain.Address;
import com.iss.shop.service.AddressService;
import com.iss.shop.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("addressService")
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public Result add(Integer userId, Address address){
        Result result = new Result();
        result.setValue(false);
        address.setUserId(userId);
        int rowCount = addressMapper.insert(address);
        if(rowCount < 0){
            result.setMessage("新建地址失败");
            return result;
        }
        result.setMessage("新建地址成功");
        result.setValue(true);
        return result;
    }

    @Override
    public Result<String> del(Integer userId,Integer addressId){
        Result result = new Result();
        result.setValue(false);
        int resultCount = addressMapper.deleteByAddressIdUserId(userId,addressId);
        if(resultCount < 0){
            result.setMessage("删除地址失败");
            return result;
        }
        result.setMessage("删除地址成功");
        result.setValue(true);
        return result;
    }

    @Override
    public Result update(Integer userId, Address address){
        Result result = new Result();
        result.setValue(false);
        address.setUserId(userId);
        int rowCount = addressMapper.updateByAddress(address);
        if(rowCount < 0){
            result.setMessage("更新地址失败");
            return result;
        }
        result.setMessage("更新地址成功");
        result.setValue(true);
        return result;
    }

    @Override
    public Result<Address> select(Integer userId, Integer addressId){
        Result result = new Result();
        result.setValue(false);
        Address address = addressMapper.selectByAddressIdUserId(userId,addressId);
        if(null == address){
            result.setMessage("无法查询到该地址");
            return result;
        }
        result.setMessage("更新地址成功");
        result.setValue(true);
        result.setData(address);
        return result;
    }


//    public Result<PageInfo> list(Integer userId,int pageNum,int pageSize){
//        PageHelper.startPage(pageNum,pageSize);
//        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
//        PageInfo pageInfo = new PageInfo(shippingList);
//        return ServerResponse.createBySuccess(pageInfo);
//    }







}
