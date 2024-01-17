package com.pongift20.marketplace.backend.partner.kyobo.mapper;

import com.pongift20.marketplace.backend.partner.kyobo.model.dto.AuthKyoboDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthKyoboMapper {
    //교보 회원 존재 체크
    AuthKyoboDto selectAuthKyobo(String userSub);
    //교보 회원 신규 등록
    long insertAuthKyobo(AuthKyoboDto authKyoboDto);
    //교보 회원 정보 업데이트
    long updateAuthKyobo(AuthKyoboDto authKyoboDto);
}
