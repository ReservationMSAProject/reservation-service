package com.reservation.reserve.reserve.dto.reservation;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationCreateRequest {
    @NotNull(message = "좌석 ID는 필수입니다")
    private Long seatId;
    
    @NotNull(message = "콘서트 ID는 필수입니다")
    private Long concertId;
}
