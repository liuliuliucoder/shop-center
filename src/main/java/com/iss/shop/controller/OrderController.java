package com.iss.shop.controller;

import com.iss.shop.domain.*;
import com.iss.shop.service.OrderService;
import com.iss.shop.service.UserService;
import com.iss.shop.util.Property;
import com.iss.shop.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @GetMapping("/create")
    @ResponseBody
    public Result create(HttpSession session, Integer addressId){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        return orderService.createOrder(user.getId(),addressId);
    }

    @PostMapping("/getOrderDetail")
    @ResponseBody
    public Result getOrderDetailByOrderNo(@RequestBody OrderVo orderVo, HttpSession session){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        return orderService.getOrderDetailByOrderNo(user.getId(),orderVo.getId());
    }

    @PostMapping("/getOrderPrice")
    @ResponseBody
    public Result getOrderPrice(@RequestBody OrderPrice orderPrice , HttpSession session){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        Result<OrderVo> orderVoResult = orderService.getOrderDetail(user.getId(),orderPrice.getOrderNo());
        List<OrderDetailVo> orderDetailVoList = orderVoResult.getData().getOrderDetailVoList();
        BigDecimal totalPrice = BigDecimal.valueOf(0);
        for(OrderDetailVo orderVo : orderDetailVoList){
            totalPrice = totalPrice.add(orderVo.getTotalPrice());
        }
        result.setData(totalPrice);
        result.setValue(true);
        return result;
    }

    @GetMapping("/cancel")
    @ResponseBody
    public Result cancel(HttpSession session, String orderNo){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        return orderService.cancel(user.getId(),orderNo);
    }

    @GetMapping("/getOrderCartProduct")
    @ResponseBody
    public Result getOrderCartProduct(HttpSession session){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        return orderService.getOrderCartProduct(user.getId());
    }

    @GetMapping("/detail")
    @ResponseBody
    public Result detail(HttpSession session,String orderNo){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        return orderService.getOrderDetail(user.getId(),orderNo);
    }

    @GetMapping("/list")
    @ResponseBody
    public Result list(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user ==null){
            result.setMessage("用户未登录,请登录");
            return result;
        }
        return orderService.getOrderList(pageNum,pageSize);
    }

    @PostMapping("/searchOrderByConditions")
    @ResponseBody
    public Result searchOrderByConditions(@RequestBody OrderQuery query, HttpSession session){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user == null){
            result.setMessage("用户未登录,请登录管理员");
            return result;
        }
        if(userService.checkAdminRole(user).getValue()){
            return orderService.searchOrderByConditions(query);

        }else{
            result.setMessage("无权限操作");
            return result;
        }
    }

    @PostMapping("/getMyOrder")
    @ResponseBody
    public Result getMyOrder(HttpSession session){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user == null){
            result.setMessage("用户未登录,请先登录！");
            return result;
        }
        return orderService.getMyOrder(user.getId());
    }

    @PostMapping("/cancelOrder")
    @ResponseBody
    public Result cancelOrder(@RequestBody OrderVo orderVo, HttpSession session){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user == null){
            result.setMessage("用户未登录,请登录管理员");
            return result;
        }
        return orderService.updateOrderStatus(orderVo.getId());
    }

    @PostMapping("/getOrderByOrderId")
    @ResponseBody
    public Result getOrderByOrderId(@RequestBody OrderQuery query, HttpSession session){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user == null){
            result.setMessage("用户未登录,请登录管理员");
            return result;
        }
        if(userService.checkAdminRole(user).getValue()){
            return orderService.getOrderByOrderId(query);

        }else{
            result.setMessage("无权限操作");
            return result;
        }
    }

    @PostMapping("/updateOrder")
    @ResponseBody
    public Result updateOrder(@RequestBody OrderVo orderVo, HttpSession session){
        Result result = new Result();
        result.setValue(false);
        User user = (User)session.getAttribute("currentUser");
        if(user == null){
            result.setMessage("用户未登录,请登录管理员");
            return result;
        }
        if(userService.checkAdminRole(user).getValue()){
            return orderService.updateOrder(orderVo);

        }else{
            result.setMessage("无权限操作");
            return result;
        }
    }

    @PostMapping("/getOrderStatusList")
    @ResponseBody
    public Result getOrderStatusList(){
        Result result = new Result();
        List<Property> list = new ArrayList<Property>();

        for (OrderStatusEnum c : OrderStatusEnum.values()) {
            Property p = new Property(c.getCode(), c.getValue());
            list.add(p);
        }
        result.setValue(true);
        result.setContent(list);
        return result;
    }

    @PostMapping("/getPayTypeList")
    @ResponseBody
    public Result getPayTypeList(){
        Result result = new Result();
        List<Property> list = new ArrayList<Property>();

        for (PayTypeEnum c : PayTypeEnum.values()) {
            Property p = new Property(c.getCode(), c.getValue());
            list.add(p);
        }
        result.setValue(true);
        result.setContent(list);
        return result;
    }

//    @RequestMapping("/pay")
//    @ResponseBody
//    public Result pay(HttpSession session, Long orderNo, HttpServletRequest request){
//        User user = (User)session.getAttribute(Const.CURRENT_USER);
//        if(user ==null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
//        }
//        String path = request.getSession().getServletContext().getRealPath("upload");
//        return iOrderService.pay(orderNo,user.getId(),path);
//    }
//
//    @RequestMapping("alipay_callback.do")
//    @ResponseBody
//    public Object alipayCallback(HttpServletRequest request){
//        Map<String,String> params = Maps.newHashMap();
//
//        Map requestParams = request.getParameterMap();
//        for(Iterator iter = requestParams.keySet().iterator();iter.hasNext();){
//            String name = (String)iter.next();
//            String[] values = (String[]) requestParams.get(name);
//            String valueStr = "";
//            for(int i = 0 ; i <values.length;i++){
//
//                valueStr = (i == values.length -1)?valueStr + values[i]:valueStr + values[i]+",";
//            }
//            params.put(name,valueStr);
//        }
//        logger.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());
//
//        //非常重要,验证回调的正确性,是不是支付宝发的.并且呢还要避免重复通知.
//
//        params.remove("sign_type");
//        try {
//            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
//
//            if(!alipayRSACheckedV2){
//                return ServerResponse.createByErrorMessage("非法请求,验证不通过,再恶意请求我就报警找网警了");
//            }
//        } catch (AlipayApiException e) {
//            logger.error("支付宝验证回调异常",e);
//        }
//
//        //todo 验证各种数据
//
//
//        //
//        ServerResponse serverResponse = iOrderService.aliCallback(params);
//        if(serverResponse.isSuccess()){
//            return Const.AlipayCallback.RESPONSE_SUCCESS;
//        }
//        return Const.AlipayCallback.RESPONSE_FAILED;
//    }
//
//    @RequestMapping("query_order_pay_status.do")
//    @ResponseBody
//    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo){
//        User user = (User)session.getAttribute(Const.CURRENT_USER);
//        if(user ==null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
//        }
//
//        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(),orderNo);
//        if(serverResponse.isSuccess()){
//            return ServerResponse.createBySuccess(true);
//        }
//        return ServerResponse.createBySuccess(false);
//    }
}
