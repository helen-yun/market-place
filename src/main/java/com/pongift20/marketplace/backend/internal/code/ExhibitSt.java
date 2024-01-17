package com.pongift20.marketplace.backend.internal.code;

public enum ExhibitSt {
    EXPOSURE("02"), //전시판매
    UNEXPOSED("03"), //전시중지
    SOLDOUT("04"); //전시품절

    private String code;

    ExhibitSt(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }

}
