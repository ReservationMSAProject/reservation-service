package com.reservation.reserve.reserve.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.reservation.reserve.reserve.domain.SeatEntity}
 */
public record SeatDto(
        String seatNumber,
        String section,
        String grade,
        long price
) implements Serializable { }