package com.pongift20.marketplace.backend.user.mapper;

import com.pongift20.marketplace.backend.user.model.dto.BaseDto;
import com.pongift20.marketplace.backend.user.model.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    UserDto findUserByLoginId(String loginId);
    UserDto selectUserByUserUid(String userUid);
    //users 등록
    long insertUsers(BaseDto param);
    //user_profile 등록
    long insertUserProfile(BaseDto param);
    //user_profile 업데이트
    long updateUserProfile(BaseDto param);
}
