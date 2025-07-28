package com.reservation.reserve.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationCreatedEvent {
    private Long reservationId;
    private String email;
    private Long concertId;
    private Long seatId;
}