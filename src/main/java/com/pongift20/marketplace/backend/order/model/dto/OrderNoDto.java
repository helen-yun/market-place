package com.pongift20.marketplace.backend.order.model.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Data
@Alias("orderNo")
public class OrderNoDto {
    private String randomNo;
    private String orderNo;
    private String resultCode;
    private String resultMsg;
}
