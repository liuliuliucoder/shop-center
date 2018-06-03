package com.iss.shop.service.impl;

import com.google.common.collect.Lists;
import com.iss.shop.dao.*;
import com.iss.shop.domain.*;
import com.iss.shop.service.OrderService;
import com.iss.shop.util.BigDecimalUtil;
import com.iss.shop.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private AddressMapper addressMapper;

    @Override
    public Result createOrder(Integer userId, Integer addressId){
        Result result = new Result();
        result.setValue(false);
        //从购物车中获取数据
        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(userId);

        //计算这个订单的总价
        result = getCartOrderItem(userId,cartList);
        if(!result.getValue()){
            return result;
        }
        List<OrderDetail> orderDetailList = (List<OrderDetail>)result.getContent();
        BigDecimal payment = getOrderTotalPrice(orderDetailList);


        //生成订单，默认从数据库取一条该用户的地址
        List<Address> addressList = addressMapper.selectByUserId(userId);
        Address address = new Address();
        if(null != addressList){
            address = addressList.get(0);
        }
        addressId = address.getId();
        Order order = convertOrder(userId,addressId,payment);
        if(order == null){
            result.setMessage("生成订单错误");
            return result;
        }
        if(null == orderDetailList){
            result.setMessage("购物车为空");
            return result;
        }
        for(OrderDetail orderDetail : orderDetailList){
            orderDetail.setOrderNo(order.getOrderNo());
        }
        //mybatis 批量插入
        orderDetailMapper.batchInsert(orderDetailList);

        //生成成功,我们要减少我们产品的库存
        reduceProductStock(orderDetailList);
        //清空一下购物车
        cleanCart(cartList);

        //返回给前端数据
        OrderVo orderVo = convertOrderVo(order,orderDetailList);
        result.setValue(true);
        result.setData(orderVo);
        return result;
    }

    private OrderVo convertOrderVo(Order order,List<OrderDetail> orderDetailList){
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPaymentType(order.getPayType());
        orderVo.setPaymentTypeDesc(PayTypeEnum.codeOf(order.getPayType()).getValue());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(OrderStatusEnum.codeOf(order.getStatus()).getValue());

        orderVo.setAddressId(order.getAddressId());
        Address address = addressMapper.selectByPrimaryKey(order.getAddressId());
        if(address != null){
            orderVo.setReceiverName(address.getReceiverName());
            orderVo.setAddressVo(convertAddressVo(address));
        }
        orderVo.setPaymentTime(order.getPayTime());
        orderVo.setSendTime(order.getSendTime());
        orderVo.setEndTime(order.getEndTime());
        orderVo.setCreated(order.getCreated());
        List<OrderDetailVo> orderDetailVoList = new ArrayList<OrderDetailVo>();

        for(OrderDetail orderDetail : orderDetailList){
            OrderDetailVo orderDetailVo = convertOrderDetailVo(orderDetail);
            orderDetailVoList.add(orderDetailVo);
        }
        orderVo.setOrderDetailVoList(orderDetailVoList);
        orderVo.setPayment(order.getBal());
        orderVo.setId(order.getId());

        return orderVo;
    }


    private OrderDetailVo convertOrderDetailVo(OrderDetail orderDetail){
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrderNo(orderDetail.getOrderNo());
        orderDetailVo.setProductId(orderDetail.getProductId());
        orderDetailVo.setProductName(orderDetail.getProductName());
        orderDetailVo.setProductImage(orderDetail.getProductImage());
        orderDetailVo.setQuantity(orderDetail.getQuantity());
        orderDetailVo.setTotalPrice(orderDetail.getTotalPrice());
        orderDetailVo.setCurrentUnitPrice(orderDetail.getCurrentPrice());

        orderDetailVo.setCreateTime(String.valueOf(orderDetail.getCreated()));
        return orderDetailVo;
    }



    private AddressVo convertAddressVo(Address address){
        AddressVo addressVo = new AddressVo();
        addressVo.setReceiverName(address.getReceiverName());
        addressVo.setProvinceId(address.getProvinceId());
        addressVo.setCityId(address.getCityId());
        addressVo.setCountryId(address.getCountryId());
        addressVo.setTownId(address.getTownId());
        addressVo.setReceiverMobile(address.getReceiverMobile());
        addressVo.setPostCode(address.getPostCode());
        addressVo.setReceiverPhone(address.getReceiverPhone());
        addressVo.setAddressDetail(address.getAddressDetail());
        addressVo.setPostCode(address.getPostCode());
        return addressVo;
    }

    private void cleanCart(List<Cart> cartList){
        for(Cart cart : cartList){
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
    }

    private void reduceProductStock(List<OrderDetail> orderDetailList){
        for(OrderDetail orderDetail : orderDetailList){
            Product product = productMapper.selectByPrimaryKey(orderDetail.getProductId());
            product.setCount(product.getCount()-orderDetail.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }
    }


    private Order convertOrder(Integer userId,Integer addressId,BigDecimal payment){
        Order order = new Order();
        String orderNo = generateOrderNo();
        order.setOrderNo(orderNo);
        order.setStatus(OrderStatusEnum.PAID.getCode());
        order.setPostage(0L);
        order.setPayType(PayTypeEnum.ONLINE_PAY.getCode());

        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setModified(new Date());
        order.setCreated(new Date());
        order.setPayTime(new Date());
        order.setBal(payment);
        order.setSendTime(new Date());
        //发货时间等等
        //付款时间等等
        int rowCount = orderMapper.insert(order);
        if(rowCount > 0){
            return order;
        }
        return null;
    }

    private String generateOrderNo(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private BigDecimal getOrderTotalPrice(List<OrderDetail> orderDetailList){
        BigDecimal payment = new BigDecimal("0");
        for(OrderDetail orderDetail : orderDetailList){
            payment = BigDecimalUtil.add(payment.doubleValue(),orderDetail.getTotalPrice().doubleValue());
        }
        return payment;
    }

    private Result getCartOrderItem(Integer userId,List<Cart> cartList){
        Result result = new Result();
        result.setValue(false);
        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
        if(null == cartList){
            result.setMessage("购物车为空");
            return result;
        }

        //校验购物车的数据,包括产品的状态和数量
        for(Cart cartItem : cartList){
            OrderDetail orderDetail = new OrderDetail();
            Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
            if(ProductStatusEnum.ON_SALE.getCode() != product.getStatus()){
                result.setMessage("不是在线售卖状态");
                return result;
            }

            //校验库存
            if(cartItem.getQuantity() > product.getCount()){
                result.setMessage("产品库存不足");
                return result;
            }

            orderDetail.setUserId(userId);
            orderDetail.setProductId(product.getId());
            orderDetail.setProductName(product.getProductName());
            orderDetail.setProductImage(product.getMainImage());
            orderDetail.setCurrentPrice(product.getPrice());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartItem.getQuantity()));
            orderDetailList.add(orderDetail);
        }
        result.setValue(true);
        result.setContent(orderDetailList);
        return result;
    }

    @Override
    public Result<String> cancel(Integer userId,String orderNo){
        Result result = new Result();
        result.setValue(false);
        Order order  = orderMapper.selectByUserIdAndOrderNo(userId,orderNo);
        if(order == null){
            result.setMessage("该用户此订单不存在");
            return result;
        }
        if(order.getStatus() != OrderStatusEnum.NO_PAY.getCode()){
            result.setMessage("已付款,无法取消订单");
            return result;
        }
        Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setStatus(OrderStatusEnum.CANCELED.getCode());

        int row = orderMapper.updateByPrimaryKeySelective(updateOrder);
        if(row < 0){
            result.setMessage("取消订单失败");
            return result;
        }
        result.setValue(true);
        return result;
    }

    @Override
    public Result getOrderCartProduct(Integer userId){
        Result result = new Result();
        result.setValue(false);
        OrderProductVo orderProductVo = new OrderProductVo();
        //从购物车中获取数据

        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(userId);
        result = getCartOrderItem(userId,cartList);
        if(!result.getValue()){
            return result;
        }
        List<OrderDetail> orderDetailList =( List<OrderDetail> ) result.getContent();

        List<OrderDetailVo> orderDetailVoList = Lists.newArrayList();

        BigDecimal payment = new BigDecimal("0");
        for(OrderDetail orderDetail : orderDetailList){
            payment = BigDecimalUtil.add(payment.doubleValue(),orderDetail.getTotalPrice().doubleValue());
            orderDetailVoList.add(convertOrderDetailVo(orderDetail));
        }
        orderProductVo.setProductTotalPrice(payment);
        orderProductVo.setOrderDetailVoList(orderDetailVoList);
        result.setValue(true);
        result.setData(orderProductVo);
        return result;
    }

    @Override
    public Result<OrderVo> getOrderDetail(Integer userId,String orderNo){
        Result result = new Result();
        result.setValue(false);
        Order order = new Order();
        order = orderMapper.selectByUserIdAndOrderNo(userId,orderNo);
        if(order == null){
            result.setMessage("没有找到该订单");
            return result;
        }
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderNoUserId(orderNo,userId);
        OrderVo orderVo = convertOrderVo(order,orderDetailList);
        result.setData(orderVo);
        result.setValue(true);
        return  result;
    }

    @Override
    public Result getOrderList(int pageNum,int pageSize){
        Result result = new Result();
        result.setValue(false);
        List<Order> orderList = orderMapper.getAllOrder();
        if(null == orderList){
            result.setMessage("获取订单列表失败！");
            return result;
        }
        result.setValue(true);
        List<OrderVo> orderVoList = convertOrderVoList(orderList);
        result.setContent(orderVoList);
        return result;
    }

    @Override
    public Result<OrderVo> searchOrderByConditions(OrderQuery query){
        Result result = new Result();
        result.setValue(false);
        List<Order> orderList = orderMapper.searchOrderByConditions(query);
        result.setValue(true);
        List<OrderVo> orderVoList = Lists.newArrayList();
        for(Order order : orderList){
            List<OrderDetail>  orderDetailList = new ArrayList<OrderDetail>();
            orderDetailList = orderDetailMapper.getByOrderNo(order.getOrderNo());
            OrderVo orderVo = convertOrderVo(order,orderDetailList);
            orderVoList.add(orderVo);
        }
        result.setContent(orderVoList);

        return result;
    }

    @Override
    public Result getOrderByOrderId(OrderQuery query){
        Result result = new Result();
        result.setValue(false);
        Order order = orderMapper.getOrderByOrderId(query.getId());
        if(null != order){
            OrderVo orderVo = new OrderVo();
            orderVo.setId(order.getId());
            orderVo.setCreated(order.getCreated());
            orderVo.setPayment(order.getBal());
            orderVo.setPaymentTime(order.getPayTime());
            orderVo.setPaymentType(order.getPayType());
            orderVo.setSendTime(order.getSendTime());
            orderVo.setStatus(order.getStatus());
            result.setValue(true);
            result.setData(orderVo);
        }
        return result;
    }

    @Override
    public Result updateOrder(OrderVo orderVo){
        Result result = new Result();
        result.setValue(false);
        Order order = new Order();
        order.setId(orderVo.getId());
        order.setCreated(orderVo.getCreated());
        order.setBal(orderVo.getPayment());
        order.setPayTime(orderVo.getPaymentTime());
        order.setPayType(orderVo.getPaymentType());
        order.setSendTime(orderVo.getSendTime());
        order.setStatus(orderVo.getStatus());
        if(orderMapper.updateByPrimaryKeySelective(order)>0){
            result.setValue(true);
            return result;
        }
        return result;
    }

    @Override
    public Result getMyOrder(Integer userId){
        Result result = new Result();
        result.setValue(false);
        List<Order> orderList = orderMapper.selectByUserId(userId);
        List<OrderVo> orderVoList = convertOrderVoList(orderList);
        result.setValue(true);
        result.setContent(orderVoList);
        return result;
    }
    @Override
    public Result updateOrderStatus(Integer id){
        Result result = new Result();
        result.setValue(false);
        Integer status = OrderStatusEnum.CANCELED.getCode();
        if(orderMapper.updateOrderStatus(id,status)>0){
            result.setValue(true);
            return result;
        }
        result.setMessage("取消订单失败！");
        return result;
    }
    @Override
    public Result getOrderDetailByOrderNo(Integer userId, Integer orderId){
        Result result = new Result();
        result.setValue(false);
        Order order = orderMapper.getOrderByOrderId(orderId);
        String orderNo = order.getOrderNo();
        return getOrderDetail(userId,orderNo);
    }

    private List<OrderVo> convertOrderVoList(List<Order> orderList){
        List<OrderVo> orderVoList = new ArrayList<OrderVo>();
        for(Order order : orderList){
            List<OrderDetail>  orderDetailList = orderDetailMapper.getByOrderNo(order.getOrderNo());
            OrderVo orderVo = convertOrderVo(order,orderDetailList);
            orderVoList.add(orderVo);
        }
        return orderVoList;
    }
}
