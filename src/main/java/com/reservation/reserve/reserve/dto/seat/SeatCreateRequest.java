package com.reservation.reserve.reserve.dto.seat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SeatCreateRequest {
    @NotBlank(message = "좌석 번호는 필수입니다")
    private String seatNumber;
    
    private String section;
    
    private String grade;
    
    @Positive(message = "가격은 0보다 커야 합니다")
    private long price;
    
    @NotNull(message = "콘서트장 ID는 필수입니다")
    private Long venueId;
}
