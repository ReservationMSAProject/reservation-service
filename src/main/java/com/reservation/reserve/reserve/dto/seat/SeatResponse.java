package com.reservation.reserve.reserve.dto.seat;

public record SeatResponse(
    Long id,
    String seatNumber,
    String section,
    String grade,
    long price,
    VenueInfo venue
) {
    public record VenueInfo(
        Long id,
        String name
    ) {}
}
