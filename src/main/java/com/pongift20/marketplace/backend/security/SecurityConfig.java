package com.pongift20.marketplace.backend.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    @Value("${server.servlet.session.cookie.name}")
    String SESSION_ID;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                    .antMatchers("/error/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/order/purchase").permitAll()
                    .antMatchers("/internal/**").permitAll()
                    .antMatchers("/auth/**").permitAll()
                    .antMatchers("/**").hasRole("USER")
                    .anyRequest().authenticated()
                    .and()
                .headers()
                    .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                    .and()
                .formLogin()
                    .loginPage("/")
                    .loginProcessingUrl("/login")
                    .usernameParameter("id")
                    .passwordParameter("pw")
                    .successHandler(new CustomAuthenticationSuccessHandler())
                    .failureHandler(new CustomAuthenticationFailureHandler())
                    .permitAll()
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .and()
                .exceptionHandling()
                    .accessDeniedHandler(new CustomAccessDeniedHandler())
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                    .and()
                .httpBasic();
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthenticationProvider());
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(bCryptPasswordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
