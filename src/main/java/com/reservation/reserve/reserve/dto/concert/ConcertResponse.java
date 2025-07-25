package com.reservation.reserve.reserve.dto.concert;

import java.time.LocalDateTime;

public record ConcertResponse(
    Long id,
    String name,
    LocalDateTime date,
    VenueInfo venue
) {
    public record VenueInfo(
        Long id,
        String name,
        String address
    ) {}
}
