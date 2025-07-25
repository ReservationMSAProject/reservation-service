package com.reservation.reserve.reserve.dto.reservation;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationCancelRequest {
    @NotNull(message = "예약 ID는 필수입니다")
    private Long reservationId;
    
    private String cancelReason;
}
