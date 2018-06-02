package com.iss.shop.domain;

public enum ProductStatusEnum {
    OF_LINE(0,"下线"),
    ON_SALE(1,"在线");
        private String value;
        private int code;
        ProductStatusEnum(int code,String value){
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

    public static ProductStatusEnum codeOf(int code){
        for(ProductStatusEnum productStatusEnum : values()){
            if(productStatusEnum.getCode() == code){
                return productStatusEnum;
            }
        }
        throw new RuntimeException("么有找到对应的枚举");
    }
}
