package com.reservation.reserve.reserve.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.reservation.reserve.reserve.domain.ConcertEntity}
 */
public record ConcertDto(
        String name,
        LocalDateTime date,
        String location
) implements Serializable { }