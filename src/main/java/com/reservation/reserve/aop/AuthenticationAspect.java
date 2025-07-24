package com.reservation.reserve.aop;

import com.reservation.reserve.filter.UserContext;
import com.reservation.reserve.reserve.dto.UserInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class AuthenticationAspect {

    @Before("@annotation(requireAuth)")
    public void checkAuthentication(JoinPoint joinPoint, RequireAuth requireAuth) {
        log.debug("Authentication check for method: {}", joinPoint.getSignature().getName());
        
        UserInfoDTO currentUser = UserContext.getCurrentUser();
        
        // 1. 사용자 인증 확인
        if (requireAuth.required() && currentUser == null) {
            log.warn("Unauthorized access attempt to method: {}", joinPoint.getSignature().getName());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
        }
        
        // 2. 역할 기반 권한 확인
        if (currentUser != null && requireAuth.roles().length > 0) {
            String userRole = currentUser.getRole();
            boolean hasRequiredRole = Arrays.asList(requireAuth.roles()).contains(userRole);
            
            if (!hasRequiredRole) {
                log.warn("Access denied for user {} with role {} to method: {}", 
                        currentUser.getUserId(), userRole, joinPoint.getSignature().getName());
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 부족합니다.");
            }
        }
        
        log.debug("Authentication successful for user: {}", 
                currentUser != null ? currentUser.getUserId() : "anonymous");
    }
}
