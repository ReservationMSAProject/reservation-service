package com.reservation.reserve.filter;

import com.reservation.reserve.reserve.dto.UserInfo;

public class UserContext {
    private static final ThreadLocal<UserInfo> currentUser = new ThreadLocal<>();
    
    public static void setCurrentUser(String userId, String role) {
        currentUser.set(new UserInfo(userId, role));
    }
    
    public static UserInfo getCurrentUser() {
        return currentUser.get();
    }
    
    public static void clear() {
        currentUser.remove();
    }
}
