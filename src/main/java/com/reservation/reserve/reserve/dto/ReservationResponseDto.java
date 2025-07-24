package com.reservation.reserve.reserve.dto;

import com.reservation.reserve.reserve.domain.StatusEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.reservation.reserve.reserve.domain.ReservationEntity}
 */
public record ReservationResponseDto(
        SeatDto seat,
        ConcertDto concert,
        String reserverEmail,
        LocalDateTime createAt,
        StatusEnum status
) implements Serializable {
}