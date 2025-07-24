package com.reservation.reserve.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireAuth {
    String[] roles() default {}; // 필요한 역할들 (선택사항)
    boolean required() default true; // 인증 필수 여부
}
