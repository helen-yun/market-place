package com.pongift20.marketplace.backend.common.code;

public enum InternalApiCode {

    API_MSG_0001("0001", "성공 하였습니다."),
    API_MSG_0002("0002", "결과가 없습니다."),
    API_ERROR_MSG_0099("0099","오류가 발생하였습니다.");


    private String code;
    private String value;


    InternalApiCode(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public String getCode() {
        return code;
    }
}
