package com.reservation.reserve.reserve.dto.reservation;

import com.reservation.reserve.reserve.domain.StatusEnum;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationSearchRequest {
    private String reserverEmail;
    private StatusEnum status;
    private Long concertId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int page = 0;
    private int size = 10;
}
