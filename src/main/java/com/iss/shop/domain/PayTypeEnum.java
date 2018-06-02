package com.iss.shop.domain;

public enum PayTypeEnum {
    ONLINE_PAY(1,"在线支付"),
    OFFLINE_PAY(2,"线下支付");

    PayTypeEnum(int code, String value){
        this.code = code;
        this.value = value;
    }
    private String value;
    private int code;

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }

    public static PayTypeEnum codeOf(int code){
        for(PayTypeEnum orderStatusEnum : values()){
            if(orderStatusEnum.getCode() == code){
                return orderStatusEnum;
            }
        }
        throw new RuntimeException("么有找到对应的枚举");
    }
}
