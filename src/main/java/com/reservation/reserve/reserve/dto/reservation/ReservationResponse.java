package com.reservation.reserve.reserve.dto.reservation;

import com.reservation.reserve.reserve.domain.StatusEnum;
import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        SeatInfo seat,
        ConcertInfo concert,
        String reserverEmail,
        LocalDateTime createAt,
        LocalDateTime expiresAt,
        StatusEnum status
) {
    public record SeatInfo(
            Long id,
            String seatNumber,
            String section,
            String grade,
            long price
    ) {
    }

    public record ConcertInfo(
            Long id,
            String name,
            LocalDateTime date,
            String venueName,
            String addressInfo  // 전체 주소 문자열
    ) { }
}
