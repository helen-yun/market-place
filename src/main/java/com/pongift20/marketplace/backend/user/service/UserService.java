package com.pongift20.marketplace.backend.user.service;

import com.pongift20.marketplace.backend.user.mapper.UserMapper;
import com.pongift20.marketplace.backend.user.model.dto.BaseDto;
import com.pongift20.marketplace.backend.user.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserMapper userMapper;

    public UserDto findUserByLoginId(String loginId) {
        UserDto userDto = userMapper.findUserByLoginId(loginId);
        String decodePhoneNumber = userDto.getDecodeUserPhoneNumber();
        userDto.setUserPhoneNumber(decodePhoneNumber);
        return userDto;
    }

    public UserDto selectUserByUserUid(String userUid) {
        UserDto userDto = userMapper.selectUserByUserUid(userUid);
        String decodePhoneNumber = userDto.getDecodeUserPhoneNumber();
        userDto.setUserPhoneNumber(decodePhoneNumber);
        return userDto;
    }

    /**
     * insert users
     * @param baseDto
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public long insertUsers(BaseDto baseDto){
        long resultUsers;
        try{
            resultUsers = userMapper.insertUsers(baseDto);
        }catch (Exception e){
            log.error("users insert error: {}", e.getMessage());
            throw new RuntimeException();
        }
        return resultUsers;
    }

    /**
     * insert userProfile
     * @param baseDto
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public long insertUserProfile(BaseDto baseDto){
        long resultUserProfile;
        try{
            resultUserProfile = userMapper.insertUserProfile(baseDto);
        }catch (Exception e){
            log.error("user_profile insert error: {}", e.getMessage());
            throw new RuntimeException();
        }
        return resultUserProfile;
    }


    /**
     * update userProfile
     * @param baseDto
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public long updateUserProfile(BaseDto baseDto){
        long resultUserProfile;
        try{
            resultUserProfile = userMapper.updateUserProfile(baseDto);
        }catch (Exception e){
            log.error("user_profile update error: {}", e.getMessage());
            throw new RuntimeException();
        }
        return resultUserProfile;
    }
}
