package com.reservation.reserve.filter;

import com.reservation.reserve.reserve.dto.UserInfoDTO;

public class UserContext {
    private static final ThreadLocal<UserInfoDTO> currentUser = new ThreadLocal<>();
    
    public static void setCurrentUser(String userId, String role) {
        currentUser.set(new UserInfoDTO(userId, role));
    }
    
    public static UserInfoDTO getCurrentUser() {
        return currentUser.get();
    }
    
    public static void clear() {
        currentUser.remove();
    }
}
