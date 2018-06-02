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
        Order order = orderMapper.selectByUserIdAndOrderNo(userId,orderNo);
        if(order != null){
            result.setMessage("没有找到该订单");
            return result;
        }
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderNoUserId(orderNo,userId);
        OrderVo orderVo = convertOrderVo(order,orderDetailList);
        result.setData(orderVo);
        return  result;
    }

    @Override
    public Result getOrderList(Integer userId,int pageNum,int pageSize){
        Result result = new Result();
        result.setValue(false);
        List<Order> orderList = orderMapper.selectByUserId(userId);
        if(null == orderList){
            result.setMessage("获取订单列表失败！");
            return result;
        }
        result.setValue(true);
        List<OrderVo> orderVoList = convertOrderVoList(orderList,userId);
        result.setContent(orderVoList);
        return result;
    }

    @Override
    public Result<OrderVo> searchOrderByConditions(OrderQuery query){
        Result result = new Result();
        result.setValue(false);
        List<Order> orderList = orderMapper.searchOrderByConditions(query);
        if(null == orderList){
            result.setMessage("获取订单列表失败！");
            return result;
        }
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


    private List<OrderVo> convertOrderVoList(List<Order> orderList,Integer userId){
        List<OrderVo> orderVoList = Lists.newArrayList();
        for(Order order : orderList){
            List<OrderDetail>  orderDetailList = new ArrayList<OrderDetail>();
            if(userId == null){
                orderDetailList = orderDetailMapper.getByOrderNo(order.getOrderNo());
            }else{
                orderDetailList = orderDetailMapper.getByOrderNoUserId(order.getOrderNo(),userId);
            }
            OrderVo orderVo = convertOrderVo(order,orderDetailList);
            orderVoList.add(orderVo);
        }
        return orderVoList;
    }

//    public ServerResponse pay(Long orderNo,Integer userId,String path){
//        Map<String ,String> resultMap = Maps.newHashMap();
//        Order order = orderMapper.selectByUserIdAndOrderNo(userId,orderNo);
//        if(order == null){
//            return ServerResponse.createByErrorMessage("用户没有该订单");
//        }
//        resultMap.put("orderNo",String.valueOf(order.getOrderNo()));
//
//
//
//        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
//        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
//        String outTradeNo = order.getOrderNo().toString();
//
//
//        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
//        String subject = new StringBuilder().append("happymmall扫码支付,订单号:").append(outTradeNo).toString();
//
//
//        // (必填) 订单总金额，单位为元，不能超过1亿元
//        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
//        String totalAmount = order.getPayment().toString();
//
//
//        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
//        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
//        String undiscountableAmount = "0";
//
//
//
//        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
//        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
//        String sellerId = "";
//
//        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
//        String body = new StringBuilder().append("订单").append(outTradeNo).append("购买商品共").append(totalAmount).append("元").toString();
//
//
//        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
//        String operatorId = "test_operator_id";
//
//        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
//        String storeId = "test_store_id";
//
//        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
//        ExtendParams extendParams = new ExtendParams();
//        extendParams.setSysServiceProviderId("2088100200300400500");
//
//
//
//
//        // 支付超时，定义为120分钟
//        String timeoutExpress = "120m";
//
//        // 商品明细列表，需填写购买商品详细信息，
//        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
//
//        List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(orderNo,userId);
//        for(OrderItem orderItem : orderItemList){
//            GoodsDetail goods = GoodsDetail.newInstance(orderItem.getProductId().toString(), orderItem.getProductName(),
//                    BigDecimalUtil.mul(orderItem.getCurrentUnitPrice().doubleValue(),new Double(100).doubleValue()).longValue(),
//                    orderItem.getQuantity());
//            goodsDetailList.add(goods);
//        }
//
//        // 创建扫码支付请求builder，设置请求参数
//        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
//                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
//                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
//                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
//                .setTimeoutExpress(timeoutExpress)
//                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
//                .setGoodsDetailList(goodsDetailList);
//
//
//        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
//        switch (result.getTradeStatus()) {
//            case SUCCESS:
//                logger.info("支付宝预下单成功: )");
//
//                AlipayTradePrecreateResponse response = result.getResponse();
//                dumpResponse(response);
//
//                File folder = new File(path);
//                if(!folder.exists()){
//                    folder.setWritable(true);
//                    folder.mkdirs();
//                }
//
//                // 需要修改为运行机器上的路径
//                //细节细节细节
//                String qrPath = String.format(path+"/qr-%s.png",response.getOutTradeNo());
//                String qrFileName = String.format("qr-%s.png",response.getOutTradeNo());
//                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);
//
//                File targetFile = new File(path,qrFileName);
//                try {
//                    FTPUtil.uploadFile(Lists.newArrayList(targetFile));
//                } catch (IOException e) {
//                    logger.error("上传二维码异常",e);
//                }
//                logger.info("qrPath:" + qrPath);
//                String qrUrl = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFile.getName();
//                resultMap.put("qrUrl",qrUrl);
//                return ServerResponse.createBySuccess(resultMap);
//            case FAILED:
//                logger.error("支付宝预下单失败!!!");
//                return ServerResponse.createByErrorMessage("支付宝预下单失败!!!");
//
//            case UNKNOWN:
//                logger.error("系统异常，预下单状态未知!!!");
//                return ServerResponse.createByErrorMessage("系统异常，预下单状态未知!!!");
//
//            default:
//                logger.error("不支持的交易状态，交易返回异常!!!");
//                return ServerResponse.createByErrorMessage("不支持的交易状态，交易返回异常!!!");
//        }
//
//    }
//
//    // 简单打印应答
//    private void dumpResponse(AlipayResponse response) {
//        if (response != null) {
//            logger.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
//            if (StringUtils.isNotEmpty(response.getSubCode())) {
//                logger.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
//                        response.getSubMsg()));
//            }
//            logger.info("body:" + response.getBody());
//        }
//    }
//
//
//    public ServerResponse aliCallback(Map<String,String> params){
//        Long orderNo = Long.parseLong(params.get("out_trade_no"));
//        String tradeNo = params.get("trade_no");
//        String tradeStatus = params.get("trade_status");
//        Order order = orderMapper.selectByOrderNo(orderNo);
//        if(order == null){
//            return ServerResponse.createByErrorMessage("非快乐慕商城的订单,回调忽略");
//        }
//        if(order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()){
//            return ServerResponse.createBySuccess("支付宝重复调用");
//        }
//        if(Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)){
//            order.setPaymentTime(DateTimeUtil.strToDate(params.get("gmt_payment")));
//            order.setStatus(Const.OrderStatusEnum.PAID.getCode());
//            orderMapper.updateByPrimaryKeySelective(order);
//        }
//
//        PayInfo payInfo = new PayInfo();
//        payInfo.setUserId(order.getUserId());
//        payInfo.setOrderNo(order.getOrderNo());
//        payInfo.setPayPlatform(Const.PayPlatformEnum.ALIPAY.getCode());
//        payInfo.setPlatformNumber(tradeNo);
//        payInfo.setPlatformStatus(tradeStatus);
//
//        payInfoMapper.insert(payInfo);
//
//        return ServerResponse.createBySuccess();
//    }
//    public ServerResponse queryOrderPayStatus(Integer userId,Long orderNo){
//        Order order = orderMapper.selectByUserIdAndOrderNo(userId,orderNo);
//        if(order == null){
//            return ServerResponse.createByErrorMessage("用户没有该订单");
//        }
//        if(order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()){
//            return ServerResponse.createBySuccess();
//        }
//        return ServerResponse.createByError();
//    }
//
//    public ServerResponse<PageInfo> manageList(int pageNum,int pageSize){
//        PageHelper.startPage(pageNum,pageSize);
//        List<Order> orderList = orderMapper.selectAllOrder();
//        List<OrderVo> orderVoList = this.assembleOrderVoList(orderList,null);
//        PageInfo pageResult = new PageInfo(orderList);
//        pageResult.setList(orderVoList);
//        return ServerResponse.createBySuccess(pageResult);
//    }
//
//
//    public ServerResponse<OrderVo> manageDetail(Long orderNo){
//        Order order = orderMapper.selectByOrderNo(orderNo);
//        if(order != null){
//            List<OrderItem> orderItemList = orderItemMapper.getByOrderNo(orderNo);
//            OrderVo orderVo = assembleOrderVo(order,orderItemList);
//            return ServerResponse.createBySuccess(orderVo);
//        }
//        return ServerResponse.createByErrorMessage("订单不存在");
//    }
//
//    public ServerResponse<PageInfo> manageSearch(Long orderNo,int pageNum,int pageSize){
//        PageHelper.startPage(pageNum,pageSize);
//        Order order = orderMapper.selectByOrderNo(orderNo);
//        if(order != null){
//            List<OrderItem> orderItemList = orderItemMapper.getByOrderNo(orderNo);
//            OrderVo orderVo = assembleOrderVo(order,orderItemList);
//
//            PageInfo pageResult = new PageInfo(Lists.newArrayList(order));
//            pageResult.setList(Lists.newArrayList(orderVo));
//            return ServerResponse.createBySuccess(pageResult);
//        }
//        return ServerResponse.createByErrorMessage("订单不存在");
//    }
//
//
//    public ServerResponse<String> manageSendGoods(Long orderNo){
//        Order order= orderMapper.selectByOrderNo(orderNo);
//        if(order != null){
//            if(order.getStatus() == Const.OrderStatusEnum.PAID.getCode()){
//                order.setStatus(Const.OrderStatusEnum.SHIPPED.getCode());
//                order.setSendTime(new Date());
//                orderMapper.updateByPrimaryKeySelective(order);
//                return ServerResponse.createBySuccess("发货成功");
//            }
//        }
//        return ServerResponse.createByErrorMessage("订单不存在");
//    }
}
