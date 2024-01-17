package com.pongift20.marketplace.backend.security;

import com.pongift20.marketplace.backend.user.model.dto.UserDto;
import com.pongift20.marketplace.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @NonNull
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String id = String.valueOf(authentication.getPrincipal());
        String password = String.valueOf(authentication.getCredentials());

        UserDto userDto = userService.findUserByLoginId(id);
        if (userDto == null) throw new BadCredentialsException("Invalid username or password");
        if (!passwordEncoder.matches(password, userDto.getLoginPassword())) throw new BadCredentialsException("Invalid username or password");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(id, null, getUserRoles());
        token.setDetails(userDto);
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


    // TODO: 권한 설정 추가 구현 필요
    private List<GrantedAuthority> getUserRoles() {
        List<GrantedAuthority> roles = new ArrayList<>();
        String roleName = "ROLE_USER";
        roles.add(new SimpleGrantedAuthority(roleName));
        return roles;
    }
}
