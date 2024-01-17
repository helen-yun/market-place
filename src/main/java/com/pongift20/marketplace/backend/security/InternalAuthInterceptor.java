package com.pongift20.marketplace.backend.security;

import com.pongift20.marketplace.backend.security.service.ChannelApiInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Internal API 인증 인터셉터
 * 추후 사용..
 * 현재 /internal API 를 80, 443 포트로 호출 시 웹서버에 의해 404 응답..
 * 내부에서만 사용하는 API로 현재는 내부망에서 포트로 직접 붙어야함.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class InternalAuthInterceptor implements HandlerInterceptor {
//    private final ChannelApiInfoService channelApiInfoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String authHeader = request.getHeader("Authorization");
//        String realIp = request.getHeader("X-Real-IP");
//        String forwardedIp = request.getHeader("X-Forwarded-For");
//        log.debug("#### (X-Real-IP) IP => {}", realIp);
//        log.debug("#### (X-Forwarded-For) IP => {}", forwardedIp);
//
//        Long providerSeq = channelApiInfoService.selectProviderSeqByApiKeyAndSecretAndIp("test", "1234", realIp);
//        log.debug("#### providerSeq => {}", providerSeq);
        return true;

        /**
         * Basic 인증 부분...
         */
//        if (authHeader != null && authHeader.startsWith("Basic ")) {
//            String credentials = authHeader.substring("Basic ".length()).trim();
//            String decodedCredentials = new String(Base64.getDecoder().decode(credentials));
//            String[] parts = decodedCredentials.split(":", 2);
//            String apiKey = parts[0];
//            String apiSecret = parts[1];
//
//            Long providerSeq = channelApiInfoService.selectProviderSeqByApiKeyAndSecretAndIp(apiKey, apiSecret, realIp);
//            if (providerSeq != null) {
//                request.setAttribute("providerSeq", providerSeq);
//                return true; // 인증 성공
//            }
//        }
//
//        // 인증 실패
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.setHeader("WWW-Authenticate", "Basic realm=\"Only After Auth\"");
//        return false;
    }
}
