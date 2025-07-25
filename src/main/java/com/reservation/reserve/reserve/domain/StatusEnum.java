package com.reservation.reserve.reserve.domain;

public enum StatusEnum {
    TEMP_RESERVED("임시예약"),    // 임시 예약 (5분간)
    CONFIRMED("예약확정"),        // 결제 완료
    CANCELLED("예약취소"),        // 취소됨
    EXPIRED("예약만료");          // 만료됨

    private final String description;

    StatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
