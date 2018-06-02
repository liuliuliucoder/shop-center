package com.iss.shop.domain;

public enum RoleEnum {
    ROLE_CUSTOMER (0,"普通用户"), //普通用户
    ROLE_ADMIN (1,"管理员");//管理员
    private String value;
    private int code;

    RoleEnum(int code,String value){
        this.code = code;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }
}
