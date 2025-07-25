package com.reservation.reserve.reserve.dto.concert;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConcertSearchRequest {
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long venueId;
    private int page = 0;
    private int size = 10;
}
