package com.reservation.reserve.event;

import java.time.LocalDateTime;


public record ReservationEventDto(
        Long reservationId,
        String email,
        String concertName,
        LocalDateTime concertDate,
        String seatNumber,
        String eventType,
        LocalDateTime timestamp
) { }
